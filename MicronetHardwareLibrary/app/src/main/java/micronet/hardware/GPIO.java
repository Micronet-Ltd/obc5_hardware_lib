package micronet.hardware;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class GPIO {
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
        Log.e("GPIO", "Error getting GPIO value.");
        return -1;
    }

    protected boolean setValue(int gpioNum, boolean state, boolean validateOutputStateAfterSet) {
        int gpioState = state ? 1: 0;

        try {
            // getExternalStorageDirectory() points to /storage/emulated/0/ which actually ends up in /mnt/shell/emulated/0/
            String fileString =  Environment.getExternalStorageDirectory() + "/outputs" + gpioNum + ".sh";

            // Delete existing shell script
            File file = new File(fileString);
            if(file.exists()){
                boolean result = file.delete();
                if(!result){
                    Log.e("GPIO", "Error deleting file while setting gpio value.");
                    return false;
                }
            }

            // If validating, then check if results file exists and delete it
            File resultFile = new File(Environment.getExternalStorageDirectory() + "/result.txt");
            if(validateOutputStateAfterSet){
                if(resultFile.exists()){
                    boolean result = resultFile.delete();
                    if(!result){
                        Log.e("GPIO", "Error deleting file while setting gpio value.");
                        return false;
                    }
                }
            }

            // Write shell script to file
            FileOutputStream fileOutputStream = new FileOutputStream(fileString);
            fileOutputStream.write("#!system/bin/sh\n".getBytes());
            fileOutputStream.write(("echo " + gpioState + " > sys/class/gpio/gpio" + gpioNum + "/value\n").getBytes());
            if(validateOutputStateAfterSet){
                fileOutputStream.write(("echo $? > /mnt/shell/emulated/0/result.txt\n").getBytes());
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            // Run shell script with op.se_dom_ex
            Runtime.getRuntime().exec(new String[]{"setprop", "op.se_dom_ex", "/mnt/shell/emulated/0/outputs" + gpioNum + ".sh"});

            // This sleep is only used for testing if we aren't validating
            if(!validateOutputStateAfterSet){
                Thread.sleep(90);
            }

            if(validateOutputStateAfterSet){
                // Sleep initial 50ms
                Thread.sleep(50);

                // Loop a max of 20 times, sleeping for 25ms between iterations
                for(int i = 0; i < 20; i++){

                    // If result file doesn't exist then continue, if last iteration, then fail
                    if(!resultFile.exists()){
                        Log.e("GPIO", "Result file doesn't exist.");
                        if(i == 19){
                            return false;
                        }
                        Thread.sleep(25);
                        continue;
                    }

                    // Read result code
                    char[] charBuffer = new char[8];
                    FileReader fileReader = new FileReader(resultFile);
                    int charsRead = fileReader.read(charBuffer);
                    if(charsRead < 1){
                        Log.e("GPIO", "Error setting gpio value. Bad result file read.");
                        if(i == 19){
                            return false;
                        }
                        Thread.sleep(25);
                        continue;
                    }
                    fileReader.close();
                    int resultCode = Integer.valueOf(String.valueOf(charBuffer).replace("\"", "").substring(0, charsRead-1));
                    Log.d("GPIO", "Result from setting gpio " + gpioNum + " " + (state ? "high": "low") + " is: " + resultCode);

                    // If the result code is not 0 then fail
                    if(resultCode != 0){
                        Log.e("GPIO", "Error setting output value. Bad result code: " + resultCode);
                        return false;
                    }

                    boolean deletionResult = resultFile.delete();
                    boolean deletion = file.delete();
                    if(!deletion || !deletionResult){
                        Log.e("GPIO", "Error deleting file while setting gpio value.");
                    }
                }
            }
        } catch (IOException e) {
            Log.e("GPIO", e.toString());
            return false;
        } catch (InterruptedException e) {
            Log.e("GPIO", "Error while sleeping.");
        }
        return true;
    }
}
