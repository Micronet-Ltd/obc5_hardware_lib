package micronet.hardware;

import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import micronet.hardware.exception.MicronetHardwareException;

import static org.junit.Assert.*;

public class MControlTest {

    private static final String TAG = "MControlTest";
    private static MControl mControl;

    @BeforeClass
    public static void createMControl(){
        mControl = new MControl();
    }

    @Test
    public void get_mcu_version() {
        try {
            String mcuVersion = mControl.get_mcu_version();

            Log.d(TAG, "MCU Version: " + mcuVersion);

            // Check that the returned string is similar to "A.2.3.0"
            assertTrue(mcuVersion.matches("\\w\\.\\d+\\.\\d+\\.\\d+"));
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void get_fpga_version() {
        try {
            String fpgaVersion = mControl.get_fpga_version();

            Log.d(TAG, "FPGA Version: " + fpgaVersion);

            // Check that the returned string is similar to "41000003"
            assertTrue(fpgaVersion.matches("\\d+"));
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void set_led_status() {


    }

    @Test
    public void get_adc_or_gpi_voltage() {
        try {
            int ADC_ANALOG_IN1 = mControl.get_adc_or_gpi_voltage(0x00);
            int ADC_GPIO_IN1= mControl.get_adc_or_gpi_voltage(0x01);
            int ADC_GPIO_IN2= mControl.get_adc_or_gpi_voltage(0x02);
            int ADC_GPIO_IN3= mControl.get_adc_or_gpi_voltage(0x03);
            int ADC_GPIO_IN4= mControl.get_adc_or_gpi_voltage(0x04);
            int ADC_GPIO_IN5= mControl.get_adc_or_gpi_voltage(0x05);
            int ADC_GPIO_IN6= mControl.get_adc_or_gpi_voltage(0x06);
            int ADC_GPIO_IN7= mControl.get_adc_or_gpi_voltage(0x07);
            int ADC_POWER_IN = mControl.get_adc_or_gpi_voltage(0x08); // (Battery Voltage)
            int ADC_POWER_VCAP = mControl.get_adc_or_gpi_voltage(0x09); // (Super cap)
            int ADC_TEMPERATURE = mControl.get_adc_or_gpi_voltage(0x0a); // (Temp sensor)
            int ADC_CABLE_TYPE = mControl.get_adc_or_gpi_voltage(0x0b);

            // Output values
            Log.d(TAG, "ADC_ANALOG_IN1: " + ADC_ANALOG_IN1);
            Log.d(TAG, "ADC_GPIO_IN1: " + ADC_GPIO_IN1);
            Log.d(TAG, "ADC_GPIO_IN2: " + ADC_GPIO_IN2);
            Log.d(TAG, "ADC_GPIO_IN3: " + ADC_GPIO_IN3);
            Log.d(TAG, "ADC_GPIO_IN4: " + ADC_GPIO_IN4);
            Log.d(TAG, "ADC_GPIO_IN5: " + ADC_GPIO_IN5);
            Log.d(TAG, "ADC_GPIO_IN6: " + ADC_GPIO_IN6);
            Log.d(TAG, "ADC_GPIO_IN7: " + ADC_GPIO_IN7);
            Log.d(TAG, "ADC_POWER_IN: " + ADC_POWER_IN);
            Log.d(TAG, "ADC_POWER_VCAP: " + ADC_POWER_VCAP);
            Log.d(TAG, "ADC_TEMPERATURE: " + ADC_TEMPERATURE);
            Log.d(TAG, "ADC_CABLE_TYPE: " + ADC_CABLE_TYPE);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }

        // Make sure values are in correct range
    }

    @Test
    public void get_power_on_reason() {
        try {
            int powerOnReason = mControl.get_power_on_reason();
            Log.d(TAG, "Power On Reason: " + powerOnReason);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }


        // Check
    }

    @Test
    public void get_rtc_date_time() {
        try {
            String rtcDateTime = mControl.get_rtc_date_time();
            Log.d(TAG, "RTC DateTime: " + rtcDateTime);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }


        // Check
    }

    @Test
    public void set_rtc_date_time() {

    }

    @Test
    public void get_rtc_cal_reg() {
        try {
            int[] rtcCalcReg = mControl.get_rtc_cal_reg();
            Log.d(TAG, "RTC CalcReg: " + Arrays.toString(rtcCalcReg));
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }


        // Check
    }

    @Test
    public void get_led_status() {
        try {
            LED right = mControl.get_led_status(0);
            LED center = mControl.get_led_status(1);
            LED left = mControl.get_led_status(2);

            Log.d(TAG, "Right LED: RED " + right.RED + ", GREEN " + right.GREEN + ", BLUE " + right.BLUE + ", BRIGHTNESS " + right.BRIGHTNESS);
            Log.d(TAG, "Center LED: RED " + center.RED + ", GREEN " + center.GREEN + ", BLUE " + center.BLUE + ", BRIGHTNESS " + center.BRIGHTNESS);
            Log.d(TAG, "Left LED: RED " + left.RED + ", GREEN " + left.GREEN + ", BLUE " + left.BLUE + ", BRIGHTNESS " + left.BRIGHTNESS);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }



        // Check
    }

    @Test
    public void check_rtc_battery() {
        try {
            String batteryStatus = mControl.check_rtc_battery();
            Log.d(TAG, "RTC Battery Status: " + batteryStatus);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }


        // Check
    }

    @Test
    public void get_power_on() {
        try {
            int[] powerOn = mControl.get_power_on();
            Log.d(TAG, "Power On Threshold " + Arrays.toString(powerOn));
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }


        // Check
    }

    @Test
    public void get_gpio_value() {
        int value692 = mControl.get_gpio_value(692);
        int value693 = mControl.get_gpio_value(693);
        int value694 = mControl.get_gpio_value(694);
        int value695 = mControl.get_gpio_value(695);
        int value696 = mControl.get_gpio_value(696);
        int value697 = mControl.get_gpio_value(697);
        int value698 = mControl.get_gpio_value(698);
        int value699 = mControl.get_gpio_value(699);

        Log.d(TAG, "GPIO 692 Value: " + value692);
        Log.d(TAG, "GPIO 693 Value: " + value693);
        Log.d(TAG, "GPIO 694 Value: " + value694);
        Log.d(TAG, "GPIO 695 Value: " + value695);
        Log.d(TAG, "GPIO 696 Value: " + value696);
        Log.d(TAG, "GPIO 697 Value: " + value697);
        Log.d(TAG, "GPIO 698 Value: " + value698);
        Log.d(TAG, "GPIO 699 Value: " + value699);

        // Check that values are either 0 or 1
        assertTrue(value692 == 0 || value692 == 1);
        assertTrue(value693 == 0 || value693 == 1);
        assertTrue(value694 == 0 || value694 == 1);
        assertTrue(value695 == 0 || value695 == 1);
        assertTrue(value696 == 0 || value696 == 1);
        assertTrue(value697 == 0 || value697 == 1);
        assertTrue(value698 == 0 || value698 == 1);
        assertTrue(value699 == 0 || value699 == 1);
    }

    @Test
    public void set_gpio_value() {

        try{
            // GPIO numbers for the outputs
            final int GP_OUTPUT_0 = 267;
            final int GP_OUTPUT_1 = 272;
            final int GP_OUTPUT_2 = 273;
            final int GP_OUTPUT_3 = 261;

            // Set outputs to high
            int result0 = mControl.set_gpio_state_dbg(GP_OUTPUT_0,1);
            int result1 = mControl.set_gpio_state_dbg(GP_OUTPUT_1,1);
            int result2 = mControl.set_gpio_state_dbg(GP_OUTPUT_2,1);
            int result3 = mControl.set_gpio_state_dbg(GP_OUTPUT_3,1);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check outputs are high
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_0) == 1);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_1) == 1);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_2) == 1);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_3) == 1);

            // Set outputs to low
            result0 = mControl.set_gpio_state_dbg(GP_OUTPUT_0,0);
            result1 = mControl.set_gpio_state_dbg(GP_OUTPUT_1,0);
            result2 = mControl.set_gpio_state_dbg(GP_OUTPUT_2,0);
            result3 = mControl.set_gpio_state_dbg(GP_OUTPUT_3,0);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check outputs are low
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_0) == 0);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_1) == 0);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_2) == 0);
            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_3) == 0);
        }catch (MicronetHardwareException e){
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void get_gpoutput_value(){

        try {
            // GPIO numbers for the outputs
            final int GP_OUTPUT_0 = 267;
            final int GP_OUTPUT_1 = 272;
            final int GP_OUTPUT_2 = 273;
            final int GP_OUTPUT_3 = 261;

            // Set outputs to high

            int result0 = mControl.get_gpio_state_dbg(GP_OUTPUT_0);
            int result1 = mControl.get_gpio_state_dbg(GP_OUTPUT_1);
            int result2 = mControl.get_gpio_state_dbg(GP_OUTPUT_2);
            int result3 = mControl.get_gpio_state_dbg(GP_OUTPUT_3);

            // Check outputs are high
            assertTrue(result0 == 0 || result0 == 1);
            assertTrue(result1 == 0 || result1 == 1);
            assertTrue(result2 == 0 || result2 == 1);
            assertTrue(result3 == 0 || result3 == 1);
        } catch (MicronetHardwareException e) {
            e.printStackTrace();
            fail();
        }
    }
}