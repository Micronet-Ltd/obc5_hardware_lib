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
        micronetHardware.getAnalogInput(MicronetHardware.kADC_ANALOG_IN1);
    }

    @Test
    public void getAllAnalogInput() {
        micronetHardware.getAllAnalogInput();
    }

    @Test
    public void getInputState() {
        micronetHardware.getInputState(MicronetHardware.kADC_ANALOG_IN1);
    }

    @Test
    public void getAllPinInState() {
        micronetHardware.getAllPinInState();
    }

    @Test
    public void getPowerUpIgnitionState(){
        micronetHardware.getPowerUpIgnitionState();
    }

    @Test
    public void getSerialNumber(){
        Info info = new Info();
        info.GetSerialNumber();
    }
}