/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package micronet.hardware;

import micronet.hardware.exception.MicronetHardwareException;

/**
 *  Micronet Hardware specific access class.
 *
 *  To get a MicronetHardware object use MicronetHardware.getInstance().
 */
public final class MicronetHardware {

    protected static final String TAG = "MicronetHardware";

    /**
     * Constant describing ignition.
     */
    public static final int kADC_ANALOG_IN1 = 0;

    /**
     * Constant describing gpio input 1.
     */
    public static final int kADC_GPIO_IN1 = 1;

    /**
     * Constant describing gpio input 2.
     */
    public static final int kADC_GPIO_IN2 = 2;

    /**
     * Constant describing gpio input 3.
     */
    public static final int kADC_GPIO_IN3 = 3;

    /**
     * Constant describing gpio input 4.
     */
    public static final int kADC_GPIO_IN4 = 4;

    /**
     * Constant describing gpio input 5.
     */
    public static final int kADC_GPIO_IN5 = 5;

    /**
     * Constant describing gpio input 6.
     */
    public static final int kADC_GPIO_IN6 = 6;

    /**
     * Constant describing gpio input 7.
     */
    public static final int kADC_GPIO_IN7 = 7;

    /**
     * Constant describing battery voltage.
     */
    public static final int kADC_POWER_IN = 8;

    /**
     * Constant describing the super cap.
     */
    public static final int kADC_POWER_VCAP = 9;

    /**
     * Constant describing the temperature sensor.
     */
    public static final int kADC_TEMPERATURE = 10;

    /**
     * Constant describing cable type.
     */
    public static final int kADC_CABLE_TYPE = 11;

    /**
     * A constant describing ignition signal id. Same integer value as {@link #kADC_ANALOG_IN1}.
     */
    public static final int TYPE_IGNITION = kADC_ANALOG_IN1;

    /**
     * Constant describing gpio output 0.
     */
    public static final int OUTPUT_0 = 0;

    /**
     * Constant describing gpio output 1.
     */
    public static final int OUTPUT_1 = 1;

    /**
     * Constant describing gpio output 2.
     */
    public static final int OUTPUT_2 = 2;

    /**
     * Constant describing gpio output 3.
     */
    public static final int OUTPUT_3 = 3;

    private static MicronetHardware instance = null;

    private static MControl mcontrol = null;

    /**
     * Lock to allow multithreaded use of MicronetHardware.
     */
    private static final Object lock = new Object();

    /**
     * Following singleton design, don't allow instantiation of Micronet Hardware.
     */
    private MicronetHardware(){}

    /**
     * @return The singleton instance of MicronetHardware
     */
    public static MicronetHardware getInstance() {
        synchronized(lock) {
            if (instance == null) {
                instance = new MicronetHardware();
            }

            if(mcontrol == null){
                mcontrol = new MControl();
            }

            return instance;
        }
    }

    /**
     * Gets analog input state of an A2D input signal.
     *
     * If the input is below 6000mv then it is considered low. If it is above 7000mv then it is
     * considered high. If it is between 6000-7000mv then it retains its old state.
     *
     * @return Input state voltage level in mV.
     * If there is an error getting the value, the value
     * of -1 is returned.
     *
     * @param inputType to be read. Can be one of the following:
     *  		{@link #kADC_ANALOG_IN1},
     *  		{@link #kADC_GPIO_IN1},
     *  		{@link #kADC_GPIO_IN2},
     *          {@link #kADC_GPIO_IN3},
     *  		{@link #kADC_GPIO_IN4},
     *          {@link #kADC_GPIO_IN5},
     *  		{@link #kADC_GPIO_IN6},
     *          {@link #kADC_GPIO_IN7},
     *          {@link #kADC_POWER_IN},
     *          {@link #kADC_POWER_VCAP},
     *  		{@link #kADC_TEMPERATURE},
     *          {@link #kADC_CABLE_TYPE}
     */
    public int getAnalogInput(int inputType) {
        int retval = -1;

        synchronized (lock){
            // Use MControl to get the adc voltage
            retval = mcontrol.get_adc_or_gpi_voltage(inputType);
        }

        return retval;
    }

    /**
     * Gets analog input state of all A2D input signals.
     *
     * If the input is below 6000mv then it is considered low. If it is above 7000mv then it is
     * considered high. If it is between 6000-7000mv then it retains its old state.
     *
     * @return An int[12] of input state voltage level in mV.
     *       Order of voltages is this way:
     *          {@link #kADC_ANALOG_IN1},
     *  		{@link #kADC_GPIO_IN1},
     *  		{@link #kADC_GPIO_IN2},
     *          {@link #kADC_GPIO_IN3},
     *  		{@link #kADC_GPIO_IN4},
     *          {@link #kADC_GPIO_IN5},
     *  		{@link #kADC_GPIO_IN6},
     *          {@link #kADC_GPIO_IN7},
     *          {@link #kADC_POWER_IN},
     *          {@link #kADC_POWER_VCAP},
     *  		{@link #kADC_TEMPERATURE},
     *          {@link #kADC_CABLE_TYPE}
     */
    public int[] getAllAnalogInput() {
        int[] retval = new int[12];

        for (int i = 0; i < retval.length; i++){
            retval[i] = -1;
        }

        // Loop through all, if error then return early
        synchronized (lock){
            for(int i = 0; i < 12; i++){
                retval[i] = mcontrol.get_adc_or_gpi_voltage(i);

                // If -1 returned, then not able to communicate with mcu properly.
                if(retval[i] == -1){
                    return retval;
                }
            }
        }

        return retval;
    }

    /**
     * Gets input state of an Automotive input signal.
     *
     * NOTE: If this function is used with the smart cradle and the smart cradle is undocked, then
     * it will return the last known state before it was undocked, not the current state. Therefore,
     * it should not be used if the device is undocked.
     *
     * @return 1 when signal state is HIGH, 0 otherwise. In case of error, -1 is
     *         returned.
     *
     * @param inputType
     *          Input pin to be read. Can be one of the following:
     *          {@link #kADC_ANALOG_IN1},
     *  		{@link #kADC_GPIO_IN1},
     *  		{@link #kADC_GPIO_IN2},
     *          {@link #kADC_GPIO_IN3},
     *  		{@link #kADC_GPIO_IN4},
     *          {@link #kADC_GPIO_IN5},
     *  		{@link #kADC_GPIO_IN6},
     *          {@link #kADC_GPIO_IN7}
     */
    public int getInputState(int inputType) {
        int retval = -1;

        synchronized (lock){
            // GPIO Inputs match to 692 to 699.
            retval = mcontrol.get_gpio_value(inputType + 692);
        }

        return retval;
    }

    /**
     * Gets all input pin state of an Automotive I/O signal.
     *
     * NOTE: If this function is used with the smart cradle and the smart cradle is undocked, then
     * it will return the last known state before it was undocked, not the current state. Therefore,
     * it should not be used if the device is undocked.
     *
     *          {@link #kADC_ANALOG_IN1},
     *  		{@link #kADC_GPIO_IN1},
     *  		{@link #kADC_GPIO_IN2},
     *          {@link #kADC_GPIO_IN3},
     *  		{@link #kADC_GPIO_IN4},
     *          {@link #kADC_GPIO_IN5},
     *  		{@link #kADC_GPIO_IN6},
     *          {@link #kADC_GPIO_IN7}
     *
     * @return an int[8], if signal state is HIGH 1 for that value, 0 otherwise.
     * In case of error, -1 is returned in the array for that input.
     */
    public int[] getAllPinInState (){
        int[] retval = new int[8];

        for (int i = 0; i < retval.length; i++){
            retval[i] = -1;
        }

        // Loop through all, if error then return early
        synchronized (lock){
            for(int i = 0; i < 8; i++){
                retval[i] = mcontrol.get_gpio_value(i + 692);

                // If -1 returned, then not able to communicate with mcu properly.
                if(retval[i] == -1){
                    return retval;
                }
            }
        }

        return retval;
    }

    /**
     * Sets the output state of one of the gpio outputs. To use this function you need to have OS 0.1.17.0 or above.
     *
     * NOTE: If you run this method without validating and then another setOutputState() within about 500ms of a previous call to this method then the
     * behavior is undefined. Developers are encouraged to use the validate boolean.
     *
     * If used with the SmartTab, changing the outputs while the device is undocked will have no effect on the outputs. Only change the output
     * state while the device is plugged in. It is not possible to get the output state, only to set it. Also, A001 SmartHub devices do not have
     * outputs.
     *
     * @param output The output can be one of the four outputs:
     *          {@link #OUTPUT_0},
     *  		{@link #OUTPUT_1},
     *  		{@link #OUTPUT_2},
     *          {@link #OUTPUT_3}
     *
     * @param validateOutputStateAfterSet If true, then it will check if the output state is set properly. This takes usually at least 100 ms and
     * can take up to 500ms before it times out. If false, then it will not check if the output state is set properly and will return rather quickly.
     * Review the note above.
     *
     * @throws MicronetHardwareException If there was an error changing the output state.
     */
    public void setOutputState(int output, boolean state, boolean validateOutputStateAfterSet) throws MicronetHardwareException{
        synchronized (lock){
            mcontrol.set_gpio_value(output + 700, state, validateOutputStateAfterSet);
        }
    }

    /**
     * Get Power up ignition connected I/O state.
     *
     * <pre>
     * @return  Integer whose value depends on what I/O connection exist on power up. To retrieve
     * specific I/O connections, use the bitwise mapping shown below.
     *
     * Bit Mask: I/O NAME,
     * 0x0001: Ignition Trigger,
     * 0x0002: Wiggle Trigger,
     * 0x0004: Arm Lockup,
     * 0x0008: Watchdog Reset.
     *
     * In case of error, -1 is returned.
     * </pre>
     */
    public int getPowerUpIgnitionState() {
        synchronized (lock){
            return mcontrol.get_power_on_reason();
        }
    }

    /**
     * Sets Delayed Power down Time in seconds.
     *
     * @param timeInSeconds Delayed power down in seconds
     *
     * @throws MicronetHardwareException if there is an error setting the value.
     */
    public void SetDelayedPowerDownTime(int timeInSeconds) throws MicronetHardwareException{
        synchronized (lock){
            mcontrol.set_device_power_off(timeInSeconds);
        }
    }

    /**
     * Gets the MCU version.
     * @return MCU version Ex: "A.1.2.0"
     *
     * @throws MicronetHardwareException if there is an error getting the value.
     */
    public String getMcuVersion() throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.get_mcu_version();
        }
    }

    /**
     * Gets the fpga version.
     * @return fpga version Ex: "41000002"
     *
     * @throws MicronetHardwareException if there is an error getting the value.
     */
    public String getFpgaVersion() throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.get_fpga_version();
        }
    }

    /**
     * To set the LED, the following command can be sent. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.html.
     *
     * @param led        right LED is 0, center LED is 1, and left LED is 2.
     * @param brightness brightness can be any int 0-255. Zero means the LED is off.
     * @param rgb        input a color as an int.
     *
     * @throws MicronetHardwareException if there is an error setting the value.
     */
    public void setLedStatus(int led, int brightness, int rgb) throws MicronetHardwareException{
        synchronized (lock){
            mcontrol.set_led_status(led, brightness, rgb);
        }
    }

    /**
     * To get the LED status, the following command can be sent.
     * Right LED is 0, center LED is 1, and left LED is 2. Brightness ranges from 0-255.
     * Zero means the led is off. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.html
     *
     * @return LED object that contains the current state of the desired LED
     *
     * @throws MicronetHardwareException if there is an error getting the value.
     */
    public LED getLedStatus(int led_num) throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.get_led_status(led_num);
        }
    }

    /**
     * Checks if the RTC battery is good, bad or not present. This function reads the register bit on the RTC to determine whether the RTC is good or bad.
     *
     * @return "Good" or "Bad" depending on the battery state.
     *
     * @throws MicronetHardwareException if there is an error checking the value.
     */
    public String checkRtcBattery() throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.check_rtc_battery();
        }
    }

    /**
     * Gets the MCU rtc date and time.
     * @return a string with the date and time. Ex: "2016-08-25 16:00:55.11"
     *
     * @throws MicronetHardwareException if there is an error getting the value.
     */
    public String getRtcDateTime() throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.get_rtc_date_time();
        }
    }

    /**
     * To set the MCU rtc date and time, send the following command. The command sets the time by using passed in dateTime string.
     * Datetime String must be of the form: "2000-00-00 00:00:00.00".
     *
     * Note: milliseconds are not set using the set command.
     *
     * @param dateTime A string datetime that datetime will be set to.
     *
     * @throws MicronetHardwareException if there is an error setting the value.
     */
    public void setRtcDateTime(String dateTime) throws MicronetHardwareException{
        synchronized (lock){
            mcontrol.set_rtc_date_time(dateTime);
        }
    }

    /**
     * Get the digital and analog rtc calibration registers.
     *
     * @return An int array of length two containing digital and analog rtc cal, respectively.
     *
     * @throws MicronetHardwareException if there is an error getting the value.
     */
    public int[] getRtcCalReg() throws MicronetHardwareException{
        synchronized (lock){
            return mcontrol.get_rtc_cal_reg();
        }
    }

}

