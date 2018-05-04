package micronet.hardware;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MicronetHardwareTest {

    protected static final String TAG = "MicronetHardwareTest";

    private static MicronetHardware micronetHardware = null;

    @Before
    public void setUp() throws Exception {
        micronetHardware = MicronetHardware.getInstance();
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

    }

    @Test
    public void getAllAnalogInput() {
        int[] returnArray = micronetHardware.getAllAnalogInput();

        // Check that values are in correct range
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
        assertTrue(analog_in1 == 0 || analog_in1 == 1);
        assertTrue(gpio_in1 == 0 || gpio_in1 == 1);
        assertTrue(gpio_in2 == 0 || gpio_in2 == 1);
        assertTrue(gpio_in3 == 0 || gpio_in3 == 1);
        assertTrue(gpio_in4 == 0 || gpio_in4 == 1);
        assertTrue(gpio_in5 == 0 || gpio_in5 == 1);
        assertTrue(gpio_in6 == 0 || gpio_in6 == 1);
        assertTrue(gpio_in7 == 0 || gpio_in7 == 1);
    }

    @Test
    public void getAllPinInState() {
        int[] returnArray = micronetHardware.getAllPinInState();

        // Check that values are either 0 or 1
        assertTrue(returnArray[0] == 0 || returnArray[0] == 1);
        assertTrue(returnArray[1] == 0 || returnArray[1] == 1);
        assertTrue(returnArray[2] == 0 || returnArray[2] == 1);
        assertTrue(returnArray[3] == 0 || returnArray[3] == 1);
        assertTrue(returnArray[4] == 0 || returnArray[4] == 1);
        assertTrue(returnArray[5] == 0 || returnArray[5] == 1);
        assertTrue(returnArray[6] == 0 || returnArray[6] == 1);
        assertTrue(returnArray[7] == 0 || returnArray[7] == 1);
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

        assertTrue(!serial.equals(""));
    }
}