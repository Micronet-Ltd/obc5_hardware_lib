package micronet.hardware;

import android.app.ActivityManager.RunningTaskInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class GPIO {

    public static final String TAG = "GPIO";
    protected int gpioNumber;

    protected GPIO(int gpioNum){
        this.gpioNumber = gpioNum;

        // If GPIO hasn't already been exported then export it
        File tempFile = new File("/sys/class/gpio/gpio"+gpioNum+"/value");
        if(!tempFile.exists()){
            // Export GPIO
            exportGPIO();
        }
    }

    private void exportGPIO(){
        try {
            File file = new File("/sys/class/gpio/export");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(String.valueOf(gpioNumber).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            Log.e(this.toString(), e.getMessage());
        }
    }

    protected int getValue() {
        try {
            File file = new File("/sys/class/gpio/gpio"+gpioNumber+"/value");

            byte[] b = new byte[1];
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead = fileInputStream.read(b);
            fileInputStream.close();

            String value = new String(b);

            return Integer.valueOf(String.valueOf(value));
        }catch (Exception e){
            Log.e(this.toString(), e.getMessage());
        }
        Log.e(TAG, "Error getting GPIO value.");
        return -1;
    }

    protected boolean setValue(int gpioNum, boolean state, boolean validateOutputStateAfterSet) {
        int gpioState = state ? 1: 0;

        try {
            // getExternalStorageDirectory() points to /storage/emulated/0/ which actually ends up in /mnt/shell/emulated/0/
            String fileString =  Environment.getExternalStorageDirectory() + "/outputs" + gpioNum + ".sh";
            File file = new File(fileString);
            File resultFile = new File(Environment.getExternalStorageDirectory() + "/result.txt");

            // If there is an error deleting old files then return false
            if (!deleteOldFiles(validateOutputStateAfterSet, file, resultFile)) {
                return false;
            }

            // Write shell script to file
            writeShellScript(gpioNum, validateOutputStateAfterSet, gpioState, fileString);

            // Run shell script with op.se_dom_ex
            Runtime.getRuntime().exec(new String[]{"setprop", "op.se_dom_ex", "/mnt/shell/emulated/0/outputs" + gpioNum + ".sh"});
//            Log.d(TAG, "setprop op.se_dom_ex");

            // If you are validating check result file
            if(validateOutputStateAfterSet){
                // Sleep initial 40ms
                Thread.sleep(40);

                // Create state variables
                boolean resultFileChecked = false;
                boolean resultCodeChecked = false;

                // Loop a max of 40 times, sleeping for 5ms between iterations, total time of around 440 ms plus overhead
                for(int i = 0; i < 40; i++){

                    // If result file doesn't exist then continue, if last iteration, then fail
                    if(!resultFileChecked && !resultFile.exists()){
//                        Log.e(TAG, "Result file doesn't exist.");
                        if(i == 39){
                            return false;
                        }
                        Thread.sleep(10);
                        continue;
                    }

                    // Set result file checked to true
                    resultFileChecked = true;

                    // Read and check result code from echo
                    if(!resultCodeChecked && !checkResultCode(resultFile, gpioNum, state)){
                        if(i == 39){
                            return false;
                        }
                        Thread.sleep(10);
                        continue;
                    }

                    // Set result code checked to true
                    resultCodeChecked = true;

                    // Check op.se_dom_ex value
                    if(!checkSeDomExValue()){
//                        Log.e(TAG, "op.se_dom_ex hasn't finish yet");
                        if(i == 39){
                            return false;
                        }
                        Thread.sleep(10);
                        continue;
                    }

                    // Delete files
                    boolean deletionResult = resultFile.delete();
                    boolean deletion = file.delete();
                    if(!deletion || !deletionResult){
                        Log.e(TAG, "Error deleting file while setting gpio value.");
                    }

                    // Result code is 0 and op.se_dom_ex value is also 0
                    break;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return false;
        } catch (InterruptedException e) {
            Log.e(TAG, "Error while sleeping.");
        }
        return true;
    }

    private boolean checkResultCode(File resultFile, int gpioNum, boolean state) throws IOException{
        // Read result code
        char[] charBuffer = new char[8];
        FileReader fileReader = new FileReader(resultFile);
        int charsRead = fileReader.read(charBuffer);
        fileReader.close();
        if(charsRead < 1){
            Log.e(TAG, "Error setting gpio value. Bad result file read.");
            return false;
        }
        int resultCode = Integer.valueOf(String.valueOf(charBuffer).replace("\"", "").trim().substring(0, charsRead-1));
//        Log.d(TAG, "Result from setting gpio " + gpioNum + " " + (state ? "high": "low") + " is: " + resultCode);

        // Make sure result code equal to 0
        if(resultCode != 0){
            Log.e(TAG, "Error setting output value. Bad result code: " + resultCode);
            return false;
        }else{
            return true;
        }
    }

    @NonNull
    private boolean checkSeDomExValue() throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"getprop", "op.se_dom_ex"});
        BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
        byte[] byteArr = new byte[8];
        int bytesRead = bufferedInputStream.read(byteArr);
        bufferedInputStream.close();

        if(bytesRead < 1){
            Log.e(TAG,"No bytes read from op.se_dom_ex value in system properties.");
            return false;
        }

        String result = (new String(byteArr, 0 , bytesRead)).replace("\"", "").trim();

//        Log.d(TAG, "Value of op.se_dom_ex: " + result);

        // Return whether the value is 0
        return result.equalsIgnoreCase("0");
    }

    private void writeShellScript(int gpioNum, boolean validateOutputStateAfterSet, int gpioState, String fileString) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileString);
        fileOutputStream.write("#!system/bin/sh\n".getBytes());
        fileOutputStream.write(("echo " + gpioState + " > sys/class/gpio/gpio" + gpioNum + "/value\n").getBytes());
        // Only write result to file if validating
        if(validateOutputStateAfterSet){
            fileOutputStream.write(("echo $? > /mnt/shell/emulated/0/result.txt\n").getBytes());
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private boolean deleteOldFiles(boolean validateOutputStateAfterSet, File file, File resultFile) {
        // Delete existing shell script
        if(file.exists()){
            boolean result = file.delete();
            if(!result){
                Log.e(TAG, "Error deleting file while setting gpio value.");
                return false;
            }
        }

        // If validating, then check if results file exists and delete it
        if(validateOutputStateAfterSet){
            if(resultFile.exists()){
                boolean result = resultFile.delete();
                if(!result){
                    Log.e(TAG, "Error deleting file while setting gpio value.");
                    return false;
                }
            }
        }

        return true;
    }
}
