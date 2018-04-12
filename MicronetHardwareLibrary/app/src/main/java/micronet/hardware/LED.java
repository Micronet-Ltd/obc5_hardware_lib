package micronet.hardware;

import android.graphics.Color;

/**
 * Created by brigham.diaz on 5/25/2016.
 */
public class LED {

    public final static int RIGHT = 0;
    public final static int CENTER = 1;
    public final static int LEFT = 2;

    public int RED = 0;
    public int GREEN = 0;
    public int BLUE = 0;
    public int BRIGHTNESS = 0;

    public final int led;

    public LED(int led) {
        this.led = led;
    }

    public void setValue(int rgb, int brightness) {
        MControl mc = new MControl();
        mc.set_led_status(led, brightness, rgb);
    }

    public int getColorValue() {
        return Color.argb(0xFF, RED, GREEN, BLUE);
    }
}
