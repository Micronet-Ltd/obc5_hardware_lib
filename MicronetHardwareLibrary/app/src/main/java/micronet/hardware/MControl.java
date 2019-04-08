package micronet.hardware;

import micronet.hardware.exception.MicronetHardwareException;

/**
 * Class to communicate with the mctl library.
 */
final class MControl {

    // Load the JNI code
    static {
        System.loadLibrary("mctl");
    }

    // JNI function declarations. Renamed so that methods are obfuscated by proguard (https://stackoverflow.com/a/21243206)
    private native static String[] a(); // jniGetMCUVersion
    private native static int[] b(); // jniGetFPGAVersion
    private native static int[] c(int gpi_num); // jniGetADCorGPIVoltage
    private native static int[] d(int led_num); // jniGetLEDStatus
    private native static int e(int led, int brightness, int rgb); // jniSetLEDValue
    private native static int[] f(); // jniGetPowerOnThresholdCfg
    private native static int[] g(); // jniGetPowerOnReason
    private native static int h(int wait_time); // jniSetDevicePowerOff
    private native static String[] i(); // jniGetRTCDateTime
    private native static int j(String dateTime); // jniSetRTCDateTime
    private native static int[] k(); // jniGetRTCCalReg
    private native static int[] l(); // jniCheckRTCBattery
    private native static void m(); // jniSetSysPropPowerCtlShutdown

    // JNI functions that aren't used or aren't defined
//    private native static int jniSetPowerOnThresholdCfg(); // jniSetPowerOnThresholdCfg
//    private native static int jniSetRTCCalReg(); // jniSetRTCCalReg
//    private native static int jniGetRTCRegDBG(); // jniGetRTCRegDBG
//    private native static int jniSetRTCRegDBG(); // jniSetRTCRegDBG
//    private native static int jniSetGPIOStateDBG(int gpio_num, int gpio_value); // jniSetGPIOStateDBG
//    private native static int[] jniGetGPIOStateDBG(int gpio_num); // jniGetGPIOStateDBG

    /**
     * Gets the MCU version
     * @return MCU version Ex: "A.1.2.0"
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    String get_mcu_version() throws MicronetHardwareException{
        String[] resultArr = a();
        int resultCode = Integer.parseInt(resultArr[0]);

        if(resultCode >= 0){
            return resultArr[1];
        }else{
            throw new MicronetHardwareException("Error getting MCU Version", resultCode);
        }
    }

    /**
     * Gets the fpga version
     * @return fpga version Ex: "41000002"
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    String get_fpga_version() throws MicronetHardwareException{
        int[] resultArr = b();
        int resultCode = resultArr[0];

        if(resultCode >= 0){
            return Integer.toHexString(resultArr[1]);
        }else{
            throw new MicronetHardwareException("Error getting FPGA Version", resultCode);
        }
    }

    /**
     * To set the LED, the following command can be sent. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.html.
     *
     * @param led        right LED is 0, center LED is 1, and left LED is 2.
     * @param brightness brightness can be any int 0-255. Zero means the LED is off.
     * @param rgb        input a color as an int between 0x000000 and 0xffffff.
     *
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    void set_led_status(int led, int brightness, int rgb) throws MicronetHardwareException{
        // Check that params are valid.
        if(led < 0 || led > 2){
            throw new IllegalArgumentException("led must be between 0 and 2, not " + led);
        }else if(brightness < 0 || brightness > 255){
            throw new IllegalArgumentException("brightness must be between 0 and 255, not " + brightness);
        }else if(rgb < 0x000000 || rgb > 0xffffff){
            throw new IllegalArgumentException("rgb must be between 0 and 0xffffff, not " + rgb);
        }

        int resultCode = e(led, brightness, rgb);

        if(resultCode < 0){
            throw new MicronetHardwareException("Error setting LED state", resultCode);
        }
    }

    /**
     * Get GPI or ADC voltage. Response is in milliVolts.
     * @param gpi_num should be between 0 and 12.
     * @return milliVolts
     */
    int get_adc_or_gpi_voltage(int gpi_num){
        // Check that params are valid.
        if(gpi_num < 0 || gpi_num > 12){
            throw new IllegalArgumentException("gpi_num must be between 0 and 12, not " + gpi_num);
        }

        int[] resultArr = c(gpi_num);
        int resultCode = resultArr[0];

        if(resultCode >= 0){
            return resultArr[1];
        }else{
            return -1;
        }
    }


    /**
     * To get the reason for the A8/CPU power up, the following command can be sent.
     * @return the Power On Reason, -1 is returned if there is an error getting the power on reason.
     *      *  <b>Bit Mask values versus I/Os Names</b>
     *----------------------------------------------------
     *    Bit Mask   |       I/O NAME
     *----------------------------------------------------
     *   0x0001      |       Ignition Trigger
     *   0x0002      |       Wiggle Trigger
     *   0x0004      |       Arm Lockup
     *   0x0008      |       Watchdog Reset
     *----------------------------------------------------
     */
    int get_power_on_reason(){
        int[] resultArr = g();
        int resultCode = resultArr[0];

        if(resultCode >= 0){
            return resultArr[1];
        }else{
            return -1;
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
     * @throws MicronetHardwareException if mcu cannot be reached
     */
    void set_device_power_off(int wait_time) throws MicronetHardwareException{
        if(wait_time < 0){
            throw new IllegalArgumentException("Parameter wait_time must not be negative");
        }

        int resultCode = h(wait_time);

        if(resultCode < 0){
            throw new MicronetHardwareException("Error setting device power off time", resultCode);
        }
    }


    /**
     * Gets the MCU rtc date and time.
     * @return A string with the date and time. Ex: "2016-08-25 16:00:55.11"
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    String get_rtc_date_time() throws MicronetHardwareException{
        String[] resultArr = i();
        int resultCode = Integer.parseInt(resultArr[0]);

        if(resultCode >= 0){
            return resultArr[1];
        }else{
            throw new MicronetHardwareException("Error getting RTC Date Time", resultCode);
        }
    }

    /**
     * To set the MCU rtc date and time, send the following command. The command sets the time by using passed in dateTime string.
     * Datetime String must be of the form: "2000-00-00 00:00:00.00".
     *
     *Note: milliseconds are not set using the set command.
     *
     * @param dateTime The date time string.
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    void set_rtc_date_time(String dateTime) throws MicronetHardwareException{
        if(!dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{2}")){
            throw new IllegalArgumentException("Date time must be of the form: 2000-00-00 00:00:00.00");
        }

        int resultCode = j(dateTime);

        if(resultCode < 0){
            throw new MicronetHardwareException("Error setting RTC Date Time", resultCode);
        }
    }

    /**
     * Get the digital and analog rtc cal registers.
     *
     * @return An int array of length two containing digital and analog rtc cal.
     * A value of -1 indicates that value is invalid.
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    int[] get_rtc_cal_reg() throws MicronetHardwareException{
        int[] resultArr = k();
        int resultCode = resultArr[0];

        if(resultCode >= 0){
            return new int[]{resultArr[1], resultArr[2]};
        }else{
            throw new MicronetHardwareException("Error getting RTC Cal Reg", resultCode);
        }
    }

    /**
     * To get the LED status, the following command can be sent. Right LED is 0, Center LED is 1, and Left LED is 2.
     * Brightness ranges from 0-255. Zero means the led is off. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.html .
     *
     * @param led_num The led number 0,1, or 2 that you want to get.
     * @return LED object that contains the current state of the desired LED.
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    LED get_led_status(int led_num) throws MicronetHardwareException{
        // Check that params are valid.
        if(led_num < 0 || led_num > 2){
            throw new IllegalArgumentException("led_num should be between 0 and 2, not " + led_num);
        }

        // Get led state and status code
        int[] ledState = d(led_num);
        int resultCode = ledState[0];

        if(resultCode >= 0){
            LED led = new LED(led_num);

            led.RED = ledState[1];
            led.GREEN = ledState[2];
            led.BLUE = ledState[3];
            led.BRIGHTNESS = ledState[4];

            return led;
        }else{
            throw new MicronetHardwareException("Error getting LED status", resultCode);
        }
    }

    /**
     * Checks if the RTC battery is good, bad, or not present. This function reads the register bit on the RTC to determine whether the RTC is good or bad.
     *
     * @return "Good" or "Bad" depending on the battery state.
     */

    String check_rtc_battery() throws MicronetHardwareException{
        int[] resultArr = l();

        int resultCode = resultArr[0];

        if(resultCode >= 0){
            int batteryState = resultArr[1];
            if(batteryState != 0){
                return "Good";
            }else{
                return "Bad";
            }
        }else{
            throw new MicronetHardwareException("Error checking the rtc battery", resultCode);
        }
    }

    /**
     * To get the power on threshold, use the following command.
     * The response returned gives the wiggle count, wiggle count sample period and the ignition threshold.
     * The wiggle count sample period (in mS) refers to how long to collect samples for before making a decision on whether a wiggle event happened.
     * The ignition threshold is in milliVolts. Values are big Endian format.
     * @return An array containing the power on threshold.
     * @throws MicronetHardwareException if mcu cannot be reached.
     */
    int[] get_power_on_threshold() throws MicronetHardwareException{
        int[] resultArr = f();
        int resultCode = resultArr[0];

        if(resultCode >= 0){
            int[] powerOnThresholdCfg = new int[3];
            powerOnThresholdCfg[0] = resultArr[1];
            powerOnThresholdCfg[1] = resultArr[2];
            powerOnThresholdCfg[2] = resultArr[3];
            return powerOnThresholdCfg;
        }else{
            throw new MicronetHardwareException("Error getting power on threshold", resultCode);
        }
    }

    /** Shutdown the device via OS command. */
    static void setSysPropPowerCtlShutdown() {
        m();
    }

    /**
     * Get the GPIO value for a certain GPIO.
     * @param gpioNumber should be between 692 and 699.
     * @return The value of that GPIO.
     */
    int get_gpio_value(int gpioNumber){
        if(gpioNumber < 692 || gpioNumber > 699){
            throw new IllegalArgumentException("gpioNumber should be between 692 and 699, not " + gpioNumber);
        }

        GPIO gpio = new GPIO(gpioNumber);
        return gpio.getValue();
    }

    /**
     * Set the GPIO state for an output GPIO.
     * @param gpioNumber should be between 700 and 703.
     * @param state set output either high or low.
     * @param validateOutputStateAfterSet whether or not the function should validate result or not. Validating takes longer.
     */
    void set_gpio_value(int gpioNumber, boolean state, boolean validateOutputStateAfterSet) throws MicronetHardwareException{

        if(gpioNumber < 700 || gpioNumber > 703){
            throw new IllegalArgumentException("gpioNumber should be between 700 and 703, not " + gpioNumber);
        }

        GPIO gpio = new GPIO(gpioNumber);

        // Set gpio output state
        boolean result = gpio.setValue(gpioNumber, state, validateOutputStateAfterSet);

        if(!result){
            throw new MicronetHardwareException("Error setting gpio state", -5);
        }
    }

//  Right now this function isn't used and has too much ability. Removing for now.
//    /**
//     * Sets the gpio state of the desired gpio_num, uses the mcu mapping of gpios.
//     * @param gpio_num The gpio number you want to get the value of.
//     * @param gpio_value The value you want to set the gpio to, either 0 or 1.
//     * @throws MicronetHardwareException if mcu cannot be reached.
//     */
//    void set_gpio_state_dbg(int gpio_num, int gpio_value) throws MicronetHardwareException{
//        int resultCode = jniSetGPIOStateDBG(gpio_num, gpio_value);
//
//        if(resultCode < 0){
//            throw new MicronetHardwareException("Error setting GPIO state", resultCode);
//        }
//    }

//  Right now this function isn't used. Keeping code if needed in the future.
//    /**
//     * Gets the gpio state of the desired gpio_num, uses the mcu mapping of gpios.
//     * @param gpio_num The gpio number you want to get the value of.
//     * @return The gpio state.
//     * @throws MicronetHardwareException if mcu cannot be reached.
//     */
//    int get_gpio_state_dbg(int gpio_num) throws MicronetHardwareException{
//        int[] resultArr = jniGetGPIOStateDBG(gpio_num);
//        int resultCode = resultArr[0];
//
//        if(resultCode >= 0){
//            return resultArr[1];
//        }else{
//            throw new MicronetHardwareException("Error getting GPOutput state", resultCode);
//        }
//    }

//   Right now this function isn't used. Keeping code in case it is needed in the future.
//    /**
//     * Get the current state of the can1/j1708 power enable gpio.
//     * @return Current state of the gpio, either 1 or 0.
//     * @throws MicronetHardwareException if mcu cannot be reached.
//     */
//    int get_can1_j1708_pwr_enable_gpio() throws MicronetHardwareException {
//        return get_gpio_state_dbg(512);
//    }

//   Right now this function isn't used. Keeping code in case it is needed in the future.
//    /**
//     * Set the current state of the can1/j1708 power enable gpio.
//     * @param value What you want to set the gpio value to, either 0 or 1.
//     * @throws MicronetHardwareException if mcu cannot be reached.
//     */
//    void set_can1_j1708_pwr_enable_gpio(int value) throws MicronetHardwareException {
//        // Check input data is valid
//        if(value < 0 || value > 1){
//            throw new IllegalArgumentException("Value must be 1 or 0, not " + value);
//        }
//
//        set_gpio_state_dbg(512,value);
//    }
}
