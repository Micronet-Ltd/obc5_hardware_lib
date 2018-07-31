package micronet.hardware;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import micronet.hardware.exception.MicronetHardwareException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MicronetHardwareOutputsTesting {

    private static final String TAG = "MicronetHardwareTest";

    private static MicronetHardware micronetHardware = null;

    @Before
    public void setUp() throws Exception {
        micronetHardware = MicronetHardware.getInstance();
    }

    @Test
    public void stressTest() {
        for(int i = 0; i < 100; i++){
            output0On();
            output1On();
            output2On();
            output3On();

            output0Off();
            output1Off();
            output2Off();
            output3Off();
        }
    }

    @Test
    public void allOutputsOn() {
        output0On();
        output1On();
        output2On();
        output3On();
    }

    @Test
    public void allOutputsOff() {
        output0Off();
        output1Off();
        output2Off();
        output3Off();
    }

    @Test
    public void output0On() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_0, true, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output0Off() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_0, false, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output1On() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_1, true, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output1Off() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_1, false, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output2On() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_2, true, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output2Off() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_2, false, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output3On() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_3, true, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }

    @Test
    public void output3Off() {
        try {
            micronetHardware.setOutputState(MicronetHardware.OUTPUT_3, false, false);
        } catch (MicronetHardwareException e) {
            Log.e(TAG, e.toString());
            fail();
        }
    }
}