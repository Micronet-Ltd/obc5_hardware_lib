package micronet.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to interface with the mctl library.
 */
public class MControl {

    protected static boolean DBG = false;

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
    private native static int jniSetDevicePowerOff(int wait_time);
    private native static String jniGetRTCDateTime();
    private native static int jniSetRTCDateTime(String dateTime);
    private native static int[] jniGetRTCCalReg();
    private native static int jniSetRTCCalReg();
    private native static int jniGetRTCRegDBG();
    private native static int jniSetRTCRegDBG();
    private native static boolean jniCheckRTCBattery();
    private native static void jniSetSysPropPowerCtlShutdown();
    private native static int jniSetGPIOStateDBG(int gpio_num, int gpio_value);
    private native static int jniGetGPIOStateDBG(int gpio_num);

    /**
     * Gets the MCU version
     * @return MCU version Ex: "A.1.2.0"
     */
    protected String get_mcu_version() {
        if (DBG) return "1234DBG";
        return jniGetMCUVersion();
    }

    /**
     * Gets the fpga version
     * @return fpga version Ex: "41000002"
     */
    protected String get_fpga_version() {
        if (DBG) return "1234DBG";
        return Integer.toHexString(jniGetFPGAVersion());
    }

    /**
     * To set the LED, the following command can be sent. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.htm.
     *
     * @param led        right LED is 0, center LED is 1, and left LED is 2.
     * @param brightness brightness can be any int 0-255. Zero means the LED is off.
     * @param rgb        input a color as an int.
     */
    protected void set_led_status(int led, int brightness, int rgb) {
        if (DBG) return;
        jniSetLEDValue(led, brightness, rgb);
    }

    /**
     * Get GPI or ADC voltage. Response is in milliVolts.
     * @param gpi_num
     * @return milliVolts
     */
    protected int get_adc_or_gpi_voltage(int gpi_num) {
        if (DBG) return 1234;
        return jniGetADCorGPIVoltage(gpi_num);
    }


    /**
     * To get the reason for the A8/CPU power up, the following command can be sent.
     * @return the Power On Reason
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
    protected int get_power_on_reason(){
        if (DBG) return 5;
        return jniGetPowerOnReason();
    }

    /**
     * To shutdown the A8/CPU and go into low power mode(pulling less than 5mA at 12V), the following command can be sent.
     * A wait time in seconds can be provided to inform the MCU to wait that period before shutting down and going into a
     * mode where it monitors the wiggle sense and ignition line.
     *
     * Note: If the ignition is ON the unit will shutdown and wake back up.
     *
     * @param wait_time a wait time given in seconds
     * @return
     */
    protected int set_device_power_off(int wait_time) {
        if (DBG) return 1234;
        return jniSetDevicePowerOff(wait_time);
    }


    /**
     * Gets the MCU rtc date and time.
     * @return a string with the date and time. Ex: "2016-08-25 16:00:55.11"
     */
    protected String get_rtc_date_time() {
        if (DBG) {
            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
            Date today = Calendar.getInstance().getTime();
            return formatter.format(today);
        }
        return jniGetRTCDateTime();
    }

    /**
     * To set the MCU rtc date and time, send the following command. The command sets the time by using the android time.
     * So if the android time is incorrect, it will set the wrong time. To verify the date and time set on android the ‘date’
     * command can be run in a shell.
     *
     *Note: milliseconds are not set using the set command.
     *
     * @param dateTime
     * @return
     */
    protected int set_rtc_date_time(String dateTime) {
        if (DBG) return 1234;
        return jniSetRTCDateTime(dateTime);
    }

    /**
     * get the digital and analog rtc cal registers
     *
     * @return an int array of length two containing digital and analog rtc cal, respectively.
     * A value of -1 indicates that value is invalid.
     */
    protected int[] get_rtc_cal_reg() {
        if (DBG) {
            return new int[]{-1, -1};
        }

        int[] arr = jniGetRTCCalReg();
        if (arr.length == 1) {
            arr = new int[]{arr[0], -1};
        } else if (arr.length != 2) {
            arr = new int[]{-1, -1};
        }

        return arr;
    }

    /**
     * To get the LED status, the following command can be sent. Right LED is 0 and Center LED is 1. Brightness ranges from 0-255. Zero means the led is off. The RGB color code used is are standard RGB color codes defined at:
     * http://www.rapidtables.com/web/color/RGB_Color.htm
     */
    protected LED get_led_status(int led_num) {
        LED led = new LED(led_num);

        if (DBG) {
            return led;
        }

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
    }

    /**
     * Checks if the RTC battery is good, bad or not present. This function reads the register bit on the RTC to determine whether the RTC is good or bad.
     */

    protected String check_rtc_battery() {
        if (DBG) return "1234DBG";
        if (jniCheckRTCBattery()) {
            return "Good";
        } else {
            return "Bad";
        }
    }

    /**
     * To get the power on threshold, use the following command.
     * The response returned gives the wiggle count, wiggle count sample period and the ignition threshold.
     * The wiggle count sample period (in mS) refers to how long to collect samples for before making a decision on whether a wiggle event happened.
     * The ignition threshold is in milliVolts. Values are big Endian format.
     * @return
     */
    protected int[] get_power_on(){
        if(DBG) return new int[]{1,2,3};
        int[] arr=jniGetPowerOnThresholdCfg();
        return arr;
    }

    /** shutdown the device via OS command */
    protected static void setSysPropPowerCtlShutdown() {
        jniSetSysPropPowerCtlShutdown();
    }

    /**
     * Get the GPIO value for a certain GPInput
     * @param gpioNumber
     * @return
     */
    protected int get_gpio_value(int gpioNumber){
        if(DBG) return 2;

        GPIO gpio = new GPIO(gpioNumber);
        return gpio.getValue();
    }

    /**
     * Set GPIO state for GPOutput
     * @param gpio_num
     * @param gpio_value
     * @return
     */
    protected int set_gpio_state(int gpio_num, int gpio_value){
        if(DBG) return 8;
        return jniSetGPIOStateDBG(gpio_num, gpio_value);
    }

    protected int get_gpoutput_state(int gpio_num){
        if(DBG) return 8;
        return jniGetGPIOStateDBG(gpio_num);
    }
}
