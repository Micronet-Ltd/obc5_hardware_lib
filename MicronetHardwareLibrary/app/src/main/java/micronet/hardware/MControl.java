package micronet.hardware;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import micronet.hardware.exception.MicronetHardwareException;

/**
 * Class to interface with the mctl library.
 */
public class MControl {

    private static final String TAG = "MControl";

    private static final Object lock = new Object();

    static int returnCode = 10;

    static {
        System.loadLibrary("mctl");
    }

    private native static String jniGetMCUVersion();
    private native static int jniGetFPGAVersion();
    private native static int jniGetADCorGPIVoltage(int gpi_num);
    private native static int[] jniGetLEDStatus(int led_num);
    private native static void jniSetLEDValue(int led, int brightness, int rgb);
    private native static int[] jniGetPowerOnThresholdCfg();
    private native static int jniSetPowerOnThresholdCfg();
    private native static int jniGetPowerOnReason();
    private native static void jniSetDevicePowerOff(int wait_time);
    private native static String jniGetRTCDateTime();
    private native static void jniSetRTCDateTime(String dateTime);
    private native static int[] jniGetRTCCalReg();
    private native static int jniSetRTCCalReg();
    private native static int jniGetRTCRegDBG();
    private native static int jniSetRTCRegDBG();
    private native static boolean jniCheckRTCBattery();
    private native static void jniSetSysPropPowerCtlShutdown();
    private native static void jniSetGPIOStateDBG(int gpio_num, int gpio_value);
    private native static int jniGetGPIOStateDBG(int gpio_num);

    /**
     * Gets the MCU version
     * @return MCU version Ex: "A.1.2.0"
     * @throws MicronetHardwareException if mcu cannot be reached
     *
     */
    protected String get_mcu_version() throws MicronetHardwareException{
        String mcuString;
        int resultCode;

        synchronized (lock){
            mcuString = jniGetMCUVersion();
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get MCU Version successful");
            return mcuString;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting MCU version.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * Gets the fpga version
     * @return fpga version Ex: "41000002"
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected String get_fpga_version() throws MicronetHardwareException {
        String fpgaVersion;
        int resultCode;

        synchronized (lock){
            fpgaVersion = Integer.toHexString(jniGetFPGAVersion());
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get FPGA Version successful");
            return fpgaVersion;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting FPGA version.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * To set the LED, the following command can be sent. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.htm.
     *
     * @param led        right LED is 0, center LED is 1, and left LED is 2.
     * @param brightness brightness can be any int 0-255. Zero means the LED is off.
     * @param rgb        input a color as an int.
     *
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected void set_led_status(int led, int brightness, int rgb) throws MicronetHardwareException {

        // Check the parameters
        if(led < 0 || led > 2){
            throw new IllegalArgumentException("led must be between 0 and 2, not " + led);
        }

        if(brightness < 0 || brightness > 255){
            throw new IllegalArgumentException("brightness must be between 0 and 255, not " + brightness);
        }

        int resultCode;

        synchronized (lock){
            jniSetLEDValue(led, brightness, rgb);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Set LED successful");
        }else{
            Log.e(TAG, "Error " + resultCode + ": error setting LED.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * Get GPI or ADC voltage. Response is in milliVolts.
     * @param gpi_num
     * @return milliVolts
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int get_adc_or_gpi_voltage(int gpi_num) throws MicronetHardwareException {
        int voltage;
        int resultCode;

        synchronized (lock){
            voltage = jniGetADCorGPIVoltage(gpi_num);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get Voltage successful");
            return voltage;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting voltage.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }


    /**
     * To get the reason for the A8/CPU power up, the following command can be sent.
     * @return the Power On Reason
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int get_power_on_reason() throws MicronetHardwareException {
        int power_on_reason;
        int resultCode;

        synchronized (lock){
            power_on_reason = jniGetPowerOnReason();
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get power up reason successful");
            return power_on_reason;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting power up reason.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * To shutdown the A8/CPU and go into low power mode(pulling less than 5mA at 12V), the following command can be sent.
     * A wait time in seconds can be provided to inform the MCU to wait that period before shutting down and going into a
     * mode where it monitors the wiggle sense and ignition line.
     *
     * Note: If the ignition is ON the unit will shutdown and wake back up.
     *
     * @param wait_time a wait time given in seconds
     * @return result code
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int set_device_power_off(int wait_time) throws MicronetHardwareException {

        if(wait_time < 0){
            throw new IllegalArgumentException("wait_time must not be negative");
        }

        int resultCode;

        synchronized (lock){
            jniSetDevicePowerOff(wait_time);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Set Device Power Off successful");
            return resultCode;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error setting device power off.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }


    /**
     * Gets the MCU rtc date and time.
     * @return a string with the date and time. Ex: "2016-08-25 16:00:55.11"
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected String get_rtc_date_time() throws MicronetHardwareException {
        String rtcDateTime;
        int resultCode;

        synchronized (lock){
            rtcDateTime = jniGetRTCDateTime();
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get RTC DateTime successful");
            return rtcDateTime;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting RTC DateTime.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * To set the MCU rtc date and time, send the following command. The command sets the time by using the android time.
     * So if the android time is incorrect, it will set the wrong time. To verify the date and time set on android the ‘date’
     * command can be run in a shell.
     *
     *Note: milliseconds are not set using the set command.
     *
     * @param dateTime
     * @return result code
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int set_rtc_date_time(String dateTime) throws MicronetHardwareException {
        int resultCode;

        synchronized (lock){
            jniSetRTCDateTime(dateTime);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Set RTC DateTime successful");
            return resultCode;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error setting RTC DateTime.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * get the digital and analog rtc cal registers
     *
     * @return an int array of length two containing digital and analog rtc cal, respectively.
     * A value of -1 indicates that value is invalid.
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int[] get_rtc_cal_reg() throws MicronetHardwareException {
        int resultCode;
        int[] arr;

        synchronized (lock){
            arr = jniGetRTCCalReg();
            resultCode = returnCode;
        }

        if (arr.length == 1) {
            arr = new int[]{arr[0], -1};
        } else if (arr.length != 2) {
            arr = new int[]{-1, -1};
        }

        if(resultCode == 0){
            Log.d(TAG, "Get RTC Cal Reg successful");
            return arr;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting RTC Cal Reg.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * To get the LED status, the following command can be sent. Right LED is 0 and Center LED is 1. Brightness ranges from 0-255. Zero means the led is off. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.html
     *
     * @return LED object that contains the current state of the desired LED
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected LED get_led_status(int led_num) throws MicronetHardwareException {

        if(led_num < 0 || led_num > 2){
            throw new IllegalArgumentException("led_num should be between 0 and 2, not " + led_num);
        }

        LED led = new LED(led_num);

        String command = "mctl api 02050" + led_num;
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            String[] tokens = line.split(",");
            led.BRIGHTNESS = Integer.parseInt(tokens[1].split("=")[1].trim());
            led.RED = Integer.parseInt(tokens[2].split("=")[1].trim());
            led.GREEN = Integer.parseInt(tokens[3].split("=")[1].trim());
            led.BLUE = Integer.parseInt(tokens[4].split("=")[1].trim());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return led;

//        int resultCode;
//        int[] ledValues;
//
//        synchronized (lock){
//            ledValues = jniGetLEDStatus(led_num);
//            resultCode = returnCode;
//        }
//
//        if(resultCode == 0){
//            Log.d(TAG, "Get LED successful");
//
//            LED led = new LED(led_num);
//            led.BRIGHTNESS = ledValues[0];
//            led.RED = ledValues[1];
//            led.GREEN = ledValues[2];
//            led.BLUE = ledValues[3];
//
//            return led;
//        }else{
//            Log.e(TAG, "Error " + resultCode + ": error getting LED.");
//            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
//        }
    }

    /**
     * Checks if the RTC battery is good, bad or not present. This function reads the register bit on the RTC to determine whether the RTC is good or bad.
     *
     * @return "Good" or "Low or not present" depending on the battery state.
     * @throws MicronetHardwareException if mcu cannot be reached
     */

    protected String check_rtc_battery() throws MicronetHardwareException {
        int resultCode;
        boolean batteryStatus;

        synchronized (lock){
            batteryStatus = jniCheckRTCBattery();
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Check RTC Battery successful");

            if(batteryStatus){
                return "Good";
            }else{
                return "Low or not present";
            }
        }else{
            Log.e(TAG, "Error " + resultCode + ": error checking RTC battery.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * To get the power on threshold, use the following command.
     * The response returned gives the wiggle count, wiggle count sample period and the ignition threshold.
     * The wiggle count sample period (in mS) refers to how long to collect samples for before making a decision on whether a wiggle event happened.
     * The ignition threshold is in milliVolts. Values are big Endian format.
     * @return the power on threshold
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int[] get_power_on() throws MicronetHardwareException {
        int resultCode;
        int[] arr;

        synchronized (lock){
            arr = jniGetPowerOnThresholdCfg();
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get Power On Threshold successful");
            return arr;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting power on threshold.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /** shutdown the device via OS command */
    protected static void setSysPropPowerCtlShutdown() {
        jniSetSysPropPowerCtlShutdown();
    }

    /**
     * Get the GPIO value for a certain GPInput
     * @param gpioNumber the gpio number you want to get the value of.
     * @return the GPIO value
     */
    protected int get_gpio_value(int gpioNumber){
        GPIO gpio = new GPIO(gpioNumber);
        return gpio.getValue();
    }

    /**
     * Sets the gpio state of the desired gpio_num, uses the mcu mapping of gpios.
     * @param gpio_num the gpio number you want to get the value of.
     * @param gpio_value the value you want to set the gpio to, either 1 or 0.
     * @return result code
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int set_gpio_state_dbg(int gpio_num, int gpio_value) throws MicronetHardwareException {
        int resultCode;

        synchronized (lock){
            jniSetGPIOStateDBG(gpio_num, gpio_value);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Set gpio state successful");
            return resultCode;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error setting gpio state.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * Gets the gpio state of the desired gpio_num, uses the mcu mapping of gpios.
     * @param gpio_num the gpio number you want to get the value of.
     * @return gpio state
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int get_gpio_state_dbg(int gpio_num) throws MicronetHardwareException {
        int resultCode;
        int gpioState;

        synchronized (lock){
            gpioState = jniGetGPIOStateDBG(gpio_num);
            resultCode = returnCode;
        }

        if(resultCode == 0){
            Log.d(TAG, "Get GPIO state successful");
            return gpioState;
        }else{
            Log.e(TAG, "Error " + resultCode + ": error getting gpio state.");
            throw new MicronetHardwareException("Micronet Hardware Error : " + resultCode, resultCode);
        }
    }

    /**
     * Get the current state of the can1/j1708 power enable gpio.
     * @return current state of the gpio, either 1 or 0.
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected int get_can1_j1708_pwr_enable_gpio() throws MicronetHardwareException {
        return get_gpio_state_dbg(512);
    }

    /**
     * Set the current state of the can1/j1708 power enable gpio.
     * @param value what you want to set the gpio value to, either 1 or 0.
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    protected void set_can1_j1708_pwr_enable_gpio(int value) throws MicronetHardwareException {
        // Check input data is valid
        if(value < 0 || value > 1){
            throw new IllegalArgumentException("Value must be 1 or 0, not " + value);
        }

        set_gpio_state_dbg(512,value);
    }
}
