package micronet.hardware;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import micronet.hardware.exception.MicronetHardwareException;

import static org.junit.Assert.*;

public class MControlTest {

    private static final String TAG = "MControlTest";
    private MControl mControl;
    private int deviceType;

    @Before
    public void setUp(){
        mControl = new MControl();
        deviceType = new Info().getDeviceType();
    }

    @Test
    public void getMcuVersion() {
        try {
            String mcuVersion = mControl.get_mcu_version();

            Log.d(TAG, "MCU Version: " + mcuVersion);

            // Check that the returned string is similar to "A.2.3.0" or "A.2.C.0"
            assertTrue(mcuVersion.matches("\\w\\.\\d+\\.\\w+\\.\\d+") || mcuVersion.matches("\\w\\.\\d+\\.\\d+\\.\\d+"));
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void getFpgaVersion() {
        try {
            String fpgaVersion = mControl.get_fpga_version();
            Log.d(TAG, "FPGA Version: " + fpgaVersion);

            // Check that the returned string is similar to "41000003"
            assertTrue(fpgaVersion.matches("\\d+"));
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void setLedStatus() {
        try {
            mControl.set_led_status(0, 255,0xFF0000);
            mControl.set_led_status(1, 255,0x00FF00);
            mControl.set_led_status(2, 255,0x0000FF);

            LED right = mControl.get_led_status(0);
            LED center = mControl.get_led_status(1);
            LED left = mControl.get_led_status(2);

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
            mControl.set_led_status(0, 0,0x000000);
            mControl.set_led_status(1, 0,0x000000);
            mControl.set_led_status(2, 255,0x00FF00);

            right = mControl.get_led_status(0);
            center = mControl.get_led_status(1);
            left = mControl.get_led_status(2);

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
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void setLedStatusBadParams() {
        // Led should be in range 0-3
        try {
            mControl.set_led_status(4, 255,0xFF0000);
            fail("Didn't throw IllegalArgumentException on bad led value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            mControl.set_led_status(-1, 255,0xFF0000);
            fail("Didn't throw IllegalArgumentException on bad led value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        // Brightness should be in range 0-255
        try {
            mControl.set_led_status(0, -1,0xFF0000);
            fail("Didn't throw IllegalArgumentException on bad brightness value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            mControl.set_led_status(0, 256,0xFF0000);
            fail("Didn't throw IllegalArgumentException on bad brightness value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        // Rgb should be between 0 and 0xffffff
        try {
            mControl.set_led_status(0, 255,-1);
            fail("Didn't throw IllegalArgumentException on bad rgb value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            mControl.set_led_status(0, 255,0x1000000);
            fail("Didn't throw IllegalArgumentException on bad rgb value.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void getAdcOrGpiVoltage() {
        int ADC_ANALOG_IN1 = mControl.get_adc_or_gpi_voltage(0x00); // (Ignition)
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

        // Check that values are in correct range
        // Correct range depends on input voltages to the device and the state of the device
        assertTrue(ADC_ANALOG_IN1 > 11000 && ADC_ANALOG_IN1 < 23000);
        assertTrue(ADC_GPIO_IN1 > 2700 && ADC_GPIO_IN1 < 3300);
        assertTrue(ADC_GPIO_IN2 > 2700 && ADC_GPIO_IN2 < 3300);
        assertTrue(ADC_GPIO_IN3 > 2700 && ADC_GPIO_IN3 < 3300);
        assertTrue(ADC_GPIO_IN4 > 2700 && ADC_GPIO_IN4 < 3300);
        assertTrue(ADC_GPIO_IN5 > 2700 && ADC_GPIO_IN5 < 3300);
        assertTrue(ADC_GPIO_IN6 > 2700 && ADC_GPIO_IN6 < 3300);
        assertTrue(ADC_GPIO_IN7 > 2700 && ADC_GPIO_IN7 < 3300);
        assertTrue(ADC_POWER_IN > 11000 && ADC_POWER_IN < 23000);
        assertTrue(ADC_POWER_VCAP > 0 && ADC_POWER_VCAP < 6000);
        assertTrue(ADC_TEMPERATURE > 500 && ADC_TEMPERATURE < 1500);
        assertTrue(ADC_CABLE_TYPE > 2000 && ADC_CABLE_TYPE < 4000);
    }

    @Test
    public void getAdcOrGpiVoltageBadParams() {
        // Gpi num should be in range 0-12
        try{
            mControl.get_adc_or_gpi_voltage(-1);
            fail();
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
        try{
            mControl.get_adc_or_gpi_voltage(13);
            fail();
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void getPowerOnReason() {
        int powerOnReason = mControl.get_power_on_reason();
        Log.d(TAG, "Power On Reason: " + powerOnReason);
        assertTrue(powerOnReason == 1 || powerOnReason == 2 || powerOnReason == 4 || powerOnReason == 8);
    }

    @Test
    public void getGpioValue() {
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

    // Not testing these functions anymore unless they are added back in.s
//    @Test
//    public void set_gpio_state_dbg() {
//        // GPIO numbers for the outputs
//        final int GP_OUTPUT_0 = 267;
//        final int GP_OUTPUT_1 = 272;
//        final int GP_OUTPUT_2 = 273;
//        final int GP_OUTPUT_3 = 261;
//
//        try {
//            // Set outputs to high
//            mControl.set_gpio_state_dbg(GP_OUTPUT_0,1);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_1,1);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_2,1);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_3,1);
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // Check outputs are high
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_0) == 1);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_1) == 1);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_2) == 1);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_3) == 1);
//
//            // Set outputs to low
//            mControl.set_gpio_state_dbg(GP_OUTPUT_0,0);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_1,0);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_2,0);
//            mControl.set_gpio_state_dbg(GP_OUTPUT_3,0);
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // Check outputs are low
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_0) == 0);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_1) == 0);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_2) == 0);
//            assertTrue(mControl.get_gpio_state_dbg(GP_OUTPUT_3) == 0);
//        } catch (MicronetHardwareException e) {
//            Log.e(TAG, e.toString());
//            fail();
//        }
//    }
//
//    @Test
//    public void get_gpio_state_dbg(){
//        // GPIO numbers for the outputs
//        final int GP_OUTPUT_0 = 267;
//        final int GP_OUTPUT_1 = 272;
//        final int GP_OUTPUT_2 = 273;
//        final int GP_OUTPUT_3 = 261;
//
//        try {
//            // Get outputs
//            int result0 = mControl.get_gpio_state_dbg(GP_OUTPUT_0);
//            int result1 = mControl.get_gpio_state_dbg(GP_OUTPUT_1);
//            int result2 = mControl.get_gpio_state_dbg(GP_OUTPUT_2);
//            int result3 = mControl.get_gpio_state_dbg(GP_OUTPUT_3);
//
//            // Check outputs
//            assertTrue(result0 == 0 || result0 == 1);
//            assertTrue(result1 == 0 || result1 == 1);
//            assertTrue(result2 == 0 || result2 == 1);
//            assertTrue(result3 == 0 || result3 == 1);
//        } catch (MicronetHardwareException e) {
//            Log.e(TAG, e.toString());
//            fail();
//        }
//    }
}