package micronet.hardware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import micronet.hardware.exception.MicronetHardwareException;
import org.junit.Before;
import org.junit.Test;

public class MicronetHardwareAnalogTesting {

    private static final String TAG = "MicronetHardwareTest";

    private static MicronetHardware micronetHardware = null;

    @Before
    public void setUp() throws Exception {
        micronetHardware = MicronetHardware.getInstance();
    }

    @Test
    public void multiThreadedSingleInstanceTest() {
        ArrayList<Thread> list = new ArrayList<Thread>();

        for(int i = 0; i < 1; i++){
            final int threadNumber = i;

            list.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Thread " + threadNumber + " started.");
                    getAllAnalogInput();
                    getAnalogInput();
                    Log.d(TAG, "Thread " + threadNumber + " ended.");
                }
            }));
        }

        for(int i = 0; i < list.size(); i++){
            list.get(i).start();
        }

        // Delay to make sure threads start
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean continueChecking = true;
        while(continueChecking){
            for(int i = 0; i < list.size(); i++){
                if(!list.get(i).isAlive()){
                    list.remove(i);
                }

                if(list.size() == 0){
                    continueChecking = false;
                    break;
                }
            }
        }

        Log.d(TAG, "Multi-Threaded test finished.");
    }

    @Test
    public void getAnalogInput() {
        int analog_in1 = micronetHardware.getAnalogInput(MicronetHardware.kADC_ANALOG_IN1);
        int gpio_in1 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN1);
        int gpio_in2 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN2);
        int gpio_in3 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN3);
        int gpio_in4 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN4);
        int gpio_in5 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN5);
        int gpio_in6 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN6);
        int gpio_in7 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN7);
        int power_in = micronetHardware.getAnalogInput(MicronetHardware.kADC_POWER_IN);
        int power_vcap = micronetHardware.getAnalogInput(MicronetHardware.kADC_POWER_VCAP);
        int temperature = micronetHardware.getAnalogInput(MicronetHardware.kADC_TEMPERATURE);
        int cable_type = micronetHardware.getAnalogInput(MicronetHardware.kADC_CABLE_TYPE);

        // Check that values are in correct range
        // Correct range depends on input voltages to the device and the state of the device
//        assertTrue("Return value: " + analog_in1, analog_in1 > 11000 && analog_in1 < 23000);
//        assertTrue("Return value: " + gpio_in1,gpio_in1 > 2700 && gpio_in1 < 3300);
//        assertTrue("Return value: " + gpio_in2,gpio_in2 > 2700 && gpio_in2 < 3300);
//        assertTrue("Return value: " + gpio_in3,gpio_in3 > 2700 && gpio_in3 < 3300);
//        assertTrue("Return value: " + gpio_in4,gpio_in4 > 2700 && gpio_in4 < 3300);
//        assertTrue("Return value: " + gpio_in5,gpio_in5 > 2700 && gpio_in5 < 3300);
//        assertTrue("Return value: " + gpio_in6,gpio_in6 > 2700 && gpio_in6 < 3300);
//        assertTrue("Return value: " + gpio_in7,gpio_in7 > 2700 && gpio_in7 < 3300);
//        assertTrue("Return value: " + power_in,power_in > 11000 && power_in < 23000);
//        assertTrue("Return value: " + power_vcap,power_vcap >= 0 && power_vcap < 6000);
//        assertTrue("Return value: " + temperature,temperature > 500 && temperature < 1500);
//        assertTrue("Return value: " + cable_type,cable_type > 2000 && cable_type < 4000);
    }

    @Test
    public void getAllAnalogInput() {
        ArrayList<ArrayList<Integer>> allCalls =  new ArrayList<>();

        for(int i = 0; i < 300; i++){
            int[] returnArray = micronetHardware.getAllAnalogInput();

            ArrayList<Integer> values = new ArrayList<>();
            for(int k: returnArray) {
                values.add(k);
            }

            allCalls.add(values);

            Log.d(TAG, Arrays.toString(returnArray));
        }

        // Check that values are in correct range
        // Correct range depends on input voltages to the device and the state of the device
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[0] > 11000 && returnArray[0] < 23000);
//        assertTrue("Return array value: " + Arrays.toString(returnArray), returnArray[1] > 2700 && returnArray[1] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[2] > 2700 && returnArray[2] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[3] > 2700 && returnArray[3] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[4] > 2700 && returnArray[4] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[5] > 2700 && returnArray[5] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[6] > 2700 && returnArray[6] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[7] > 2700 && returnArray[7] < 3300);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[8] > 11000 && returnArray[8] < 23000);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[9] >= 0 && returnArray[9] < 6000);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[10] > 500 && returnArray[10] < 1500);
//        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[11] > 2000 && returnArray[11] < 4000);
    }
}