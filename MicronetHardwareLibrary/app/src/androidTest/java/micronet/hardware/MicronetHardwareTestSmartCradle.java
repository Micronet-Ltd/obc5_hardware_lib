package micronet.hardware;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import micronet.hardware.exception.MicronetHardwareException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MicronetHardwareTestSmartCradle {

    private static final String TAG = "MicronetHardwareTest";

    private static MicronetHardware micronetHardware = null;

    @Before
    public void setUp() throws Exception {
        micronetHardware = MicronetHardware.getInstance();
    }

    @Test
    public void multiThreadedSingleInstanceTest() {

        ArrayList<Thread> list = new ArrayList<Thread>();

        for(int i = 0; i < 10; i++){
            final int threadNumber = i;

            list.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Thread " + threadNumber + " started.");
                    getAllAnalogInput();
                    getAllPinInState();
                    getAnalogInput();
                    getFPGAVersion();
                    getInputState();
                    getLedStatus();
                    getMCUVersion();
                    getPowerUpIgnitionState();
                    getRTCCalReg();
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
        assertTrue("Return array value: " + analog_in1, analog_in1 > 11000 && analog_in1 < 23000);
        assertTrue("Return array value: " + gpio_in1,gpio_in1 > 2700 && gpio_in1 < 3300);
        assertTrue("Return array value: " + gpio_in2,gpio_in2 > 2700 && gpio_in2 < 3300);
        assertTrue("Return array value: " + gpio_in3,gpio_in3 > 2700 && gpio_in3 < 3300);
        assertTrue("Return array value: " + gpio_in4,gpio_in4 > 2700 && gpio_in4 < 3300);
        assertTrue("Return array value: " + gpio_in5,gpio_in5 > 2700 && gpio_in5 < 3300);
        assertTrue("Return array value: " + gpio_in6,gpio_in6 > 2700 && gpio_in6 < 3300);
        assertTrue("Return array value: " + gpio_in7,gpio_in7 > 2700 && gpio_in7 < 3300);
        assertTrue("Return array value: " + power_in,power_in > 11000 && power_in < 23000);
        assertTrue("Return array value: " + power_vcap,power_vcap >= 0 && power_vcap < 6000);
        assertTrue("Return array value: " + temperature,temperature > 500 && temperature < 1500);
        assertTrue("Return array value: " + cable_type,cable_type > 2000 && cable_type < 4000);
    }

    @Test
    public void getAllAnalogInput() {
        int[] returnArray = micronetHardware.getAllAnalogInput();

        // Check that values are in correct range
        // Correct range depends on input voltages to the device and the state of the device
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[0] > 11000 && returnArray[0] < 23000);
        assertTrue("Return array value: " + Arrays.toString(returnArray), returnArray[1] > 2700 && returnArray[1] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[2] > 2700 && returnArray[2] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[3] > 2700 && returnArray[3] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[4] > 2700 && returnArray[4] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[5] > 2700 && returnArray[5] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[6] > 2700 && returnArray[6] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[7] > 2700 && returnArray[7] < 3300);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[8] > 11000 && returnArray[8] < 23000);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[9] >= 0 && returnArray[9] < 6000);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[10] > 500 && returnArray[10] < 1500);
        assertTrue("Return array value: " + Arrays.toString(returnArray),returnArray[11] > 2000 && returnArray[11] < 4000);
    }

    @Test
    public void getInputState() {
        int analog_in1 = micronetHardware.getInputState(MicronetHardware.kADC_ANALOG_IN1);
        int gpio_in1 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN1);
        int gpio_in2 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN2);
        int gpio_in3 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN3);
        int gpio_in4 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN4);
        int gpio_in5 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN5);
        int gpio_in6 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN6);
        int gpio_in7 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN7);

        // Check that values are either 0 or 1
        assertTrue(analog_in1 == 1);
        assertTrue(gpio_in1 == 0);
        assertTrue(gpio_in2 == 0);
        assertTrue(gpio_in3 == 0);
        assertTrue(gpio_in4 == 0);
        assertTrue(gpio_in5 == 0);
        assertTrue(gpio_in6 == 0);
        assertTrue(gpio_in7 == 0);
    }

    @Test
    public void getAllPinInState() {
        int[] returnArray = micronetHardware.getAllPinInState();

        // Check that values are either 0 or 1
        assertTrue(returnArray[0] == 1);
        assertTrue(returnArray[1] == 0);
        assertTrue(returnArray[2] == 0);
        assertTrue(returnArray[3] == 0);
        assertTrue(returnArray[4] == 0);
        assertTrue(returnArray[5] == 0);
        assertTrue(returnArray[6] == 0);
        assertTrue(returnArray[7] == 0);
    }

    @Test
    public void getPowerUpIgnitionState(){
        int powerUpReason = micronetHardware.getPowerUpIgnitionState();

        assertTrue(powerUpReason == 1 || powerUpReason == 2 ||powerUpReason == 4 ||powerUpReason == 8);
    }

    @Test
    public void getSerialNumber(){
        Info info = new Info();
        String serial = info.GetSerialNumber();

        assertNotEquals("",serial);
    }

    @Test
    public void getMCUVersion(){
        try {
            String mcuVersion = micronetHardware.getMcuVersion();

            Log.d(TAG, "MCU Version: " + mcuVersion);

            // Check that the returned string is similar to "A.2.3.0"
            assertTrue(mcuVersion.matches("\\w\\.\\d+\\.\\d+\\.\\d+"));
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void getFPGAVersion(){
        try {
            String fpgaVersion = micronetHardware.getFpgaVersion();
            Log.d(TAG, "FPGA Version: " + fpgaVersion);

            // Check that the returned string is similar to "41000003"
            assertTrue(fpgaVersion.matches("\\d+"));
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void setLedStatus(){
        try {
            micronetHardware.setLedStatus(0, 255,0xFF0000);
            micronetHardware.setLedStatus(1, 255,0x00FF00);
            micronetHardware.setLedStatus(2, 255,0x0000FF);

            Thread.sleep(1000);

            LED right = micronetHardware.getLedStatus(0);
            LED center = micronetHardware.getLedStatus(1);
            LED left = micronetHardware.getLedStatus(2);

            assertEquals(right.RED, 255);
            assertEquals(right.GREEN, 0);
            assertEquals(right.BLUE, 0);
            assertEquals(right.BRIGHTNESS, 255);

            assertEquals(center.RED, 0);
            assertEquals(center.GREEN, 255);
            assertEquals(center.BLUE, 0);
            assertEquals(center.BRIGHTNESS, 255);

            assertEquals(left.RED, 0);
            assertEquals(left.GREEN, 0);
            assertEquals(left.BLUE, 255);
            assertEquals(left.BRIGHTNESS, 255);

            Log.d(TAG, "Right LED: RED " + right.RED + ", GREEN " + right.GREEN + ", BLUE " + right.BLUE + ", BRIGHTNESS " + right.BRIGHTNESS);
            Log.d(TAG, "Center LED: RED " + center.RED + ", GREEN " + center.GREEN + ", BLUE " + center.BLUE + ", BRIGHTNESS " + center.BRIGHTNESS);
            Log.d(TAG, "Left LED: RED " + left.RED + ", GREEN " + left.GREEN + ", BLUE " + left.BLUE + ", BRIGHTNESS " + left.BRIGHTNESS);

            // Set back to original colors
            micronetHardware.setLedStatus(0, 0,0x000000);
            micronetHardware.setLedStatus(1, 0,0x000000);
            micronetHardware.setLedStatus(2, 255,0x00FF00);

            Thread.sleep(1000);

            right = micronetHardware.getLedStatus(0);
            center = micronetHardware.getLedStatus(1);
            left = micronetHardware.getLedStatus(2);

            assertEquals(right.RED, 0);
            assertEquals(right.GREEN, 0);
            assertEquals(right.BLUE, 0);
            assertEquals(right.BRIGHTNESS, 0);

            assertEquals(center.RED, 0);
            assertEquals(center.GREEN, 0);
            assertEquals(center.BLUE, 0);
            assertEquals(center.BRIGHTNESS, 0);

            assertEquals(left.RED, 0);
            assertEquals(left.GREEN, 255);
            assertEquals(left.BLUE, 0);
            assertEquals(left.BRIGHTNESS, 255);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void getLedStatus(){
        try {
            LED right = micronetHardware.getLedStatus(0);
            LED center = micronetHardware.getLedStatus(1);
            LED left = micronetHardware.getLedStatus(2);

            Log.d(TAG, "Right LED: RED " + right.RED + ", GREEN " + right.GREEN + ", BLUE " + right.BLUE + ", BRIGHTNESS " + right.BRIGHTNESS);
            Log.d(TAG, "Center LED: RED " + center.RED + ", GREEN " + center.GREEN + ", BLUE " + center.BLUE + ", BRIGHTNESS " + center.BRIGHTNESS);
            Log.d(TAG, "Left LED: RED " + left.RED + ", GREEN " + left.GREEN + ", BLUE " + left.BLUE + ", BRIGHTNESS " + left.BRIGHTNESS);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void checkRTCBattery(){
        try {
            String batteryStatus = micronetHardware.checkRtcBattery();
            Log.d(TAG, "RTC Battery Status: " + batteryStatus);

            assertEquals("Good",batteryStatus);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

//    Smart cradle doesn't have this functionality
//    @Test
//    public void getRTCDateTime(){
//        try {
//            String rtcDateTime = micronetHardware.getRtcDateTime();
//            Log.d(TAG, "RTC DateTime: " + rtcDateTime);
//
//            // Make sure it matches the format of a rtc string
//            assertTrue(rtcDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{2}"));
//        } catch (MicronetHardwareException e) {
//            Log.e(TAG, e.toString());
//            fail();
//        }
//    }
//    @Test
//    public void setRTCDateTime(){
//        try {
//            // Will set datetime to "1111-11-11 11:11:11.00"
//            micronetHardware.setRtcDateTime("1111-11-11 11:11:11.11");
//
//            String rtcDateTime = micronetHardware.getRtcDateTime();
//            Log.d(TAG, "RTC DateTime: " + rtcDateTime);
//
//            // Make sure it matches the format of a rtc string
//            assertTrue(rtcDateTime.matches("1111-11-11 11:11:\\d{2}\\.\\d{2}"));
//
//            // Will set datetime to "2011-01-20 05:34:22.00"
//            micronetHardware.setRtcDateTime("2011-01-20 05:34:22.55");
//
//            rtcDateTime = micronetHardware.getRtcDateTime();
//            Log.d(TAG, "RTC DateTime: " + rtcDateTime);
//
//            // Make sure it matches the format of a rtc string
//            assertTrue(rtcDateTime.matches("2011-01-20 05:34:\\d{2}\\.\\d{2}"));
//        } catch (MicronetHardwareException e) {
//            Log.e(TAG, e.toString());
//            fail();
//        }
//    }

    @Test
    public void getRTCCalReg(){
        try {
            int[] rtcCalcReg = micronetHardware.getRtcCalReg();
            Log.d(TAG, "RTC CalcReg: " + Arrays.toString(rtcCalcReg));

            assertTrue(rtcCalcReg[0] >= 0);
            assertTrue(rtcCalcReg[1] > 0);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }
}