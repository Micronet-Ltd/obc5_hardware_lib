package micronet.hardware;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GPIO {
    public int gpioNumber;

    public GPIO(int gpioNum){
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

    public int getValue() {
        try {
            File file = new File("/sys/class/gpio/gpio"+gpioNumber+"/value");

            byte[] b = new byte[1];
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead = fileInputStream.read(b);
            fileInputStream.close();

            String value = new String(b);
            Log.d("GPIO", "GPIO " + gpioNumber + " Value: " + value);

            return Integer.valueOf(String.valueOf(value));
        }catch (Exception e){
            Log.e(this.toString(), e.getMessage());
        }
        Log.e("GPIO", "Error getting GPIO value.");
        return -1;
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean setValue(int value) {
        try {
            File file = new File("/sys/class/gpio/gpio"+gpioNumber+"/value");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(String.valueOf(value).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        }catch (Exception e){
            Log.e(this.toString(), e.getMessage());
        }
        Log.e("GPIO", "Error setting GPIO value.");
        return false;
    }
}
