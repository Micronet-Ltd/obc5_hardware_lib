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

import android.util.Log;

import java.util.Arrays;

/**
 *  Micronet hardware-specific access class for the OBC5.
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


    private static MicronetHardware instance = null;

    private static MControl mcontrol = null;

    /**
     * @return The singleton instance of MicronetHardware
     */
    public static MicronetHardware getInstance() {

        if (instance == null) {
            instance = new MicronetHardware();
        }

        if(mcontrol == null){
            mcontrol = new MControl();
        }

        return instance;
    }

    /**
     * Gets analog input state of an A2D input signal.
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

        // Use MControl to get the adc voltage
        retval = mcontrol.get_adc_or_gpi_voltage(inputType);

        Log.d(TAG, inputType + ": " + retval);

        return retval;
    }

    /**
     * Gets analog input state of all A2D input signals.
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

        retval[0] = mcontrol.get_adc_or_gpi_voltage(kADC_ANALOG_IN1);
        retval[1] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN1);
        retval[2] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN2);
        retval[3] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN3);
        retval[4] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN4);
        retval[5] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN5);
        retval[6] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN6);
        retval[7] = mcontrol.get_adc_or_gpi_voltage(kADC_GPIO_IN7);
        retval[8] = mcontrol.get_adc_or_gpi_voltage(kADC_POWER_IN);
        retval[9] = mcontrol.get_adc_or_gpi_voltage(kADC_POWER_VCAP);
        retval[10] = mcontrol.get_adc_or_gpi_voltage(kADC_TEMPERATURE);
        retval[11] = mcontrol.get_adc_or_gpi_voltage(kADC_CABLE_TYPE);

        Log.d(TAG, Arrays.toString(retval));

        return retval;
    }

    /**
     * Gets input state of an Automotive input signal.
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

        // GPIO Inputs match to 692 to 699.
        retval = mcontrol.get_gpio_value(inputType + 692);

        Log.d(TAG, inputType + ": " + retval);

        return retval;
    }

    /**
     * Gets all input pin state of an Automotive I/O signal.
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

        // GPIO Inputs match to 692 to 699.
        retval[0] = mcontrol.get_gpio_value(kADC_ANALOG_IN1 + 692);
        retval[1] = mcontrol.get_gpio_value(kADC_GPIO_IN1 + 692);
        retval[2] = mcontrol.get_gpio_value(kADC_GPIO_IN2 + 692);
        retval[3] = mcontrol.get_gpio_value(kADC_GPIO_IN3 + 692);
        retval[4] = mcontrol.get_gpio_value(kADC_GPIO_IN4 + 692);
        retval[5] = mcontrol.get_gpio_value(kADC_GPIO_IN5 + 692);
        retval[6] = mcontrol.get_gpio_value(kADC_GPIO_IN6 + 692);
        retval[7] = mcontrol.get_gpio_value(kADC_GPIO_IN7 + 692);

        Log.d(TAG, Arrays.toString(retval));

        return retval;
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
     * 0x0008: Watchdog Reset
     *
     * In case of error, -1 is returned.
     * </pre>
     */
    public int getPowerUpIgnitionState() {
        int retval = -1;

        retval = mcontrol.get_power_on_reason();

        Log.d(TAG, "Power on reason: " + retval);

        return retval;
    }

}

