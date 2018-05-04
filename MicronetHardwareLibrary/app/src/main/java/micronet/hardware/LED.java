package micronet.hardware;

import android.graphics.Color;

/**
 * LED class that contains the information regarding one of the devices LEDs.
 */
public class LED {

    /**
     * Constant describing the right LED.
     */
    public final static int RIGHT = 0;

    /**
     * Constant describing the center LED.
     */
    public final static int CENTER = 1;

    /**
     * Constant describing the LEFT LED.
     */
    public final static int LEFT = 2;

    /**
     * LEDs RED value 0-255.
     */
    public int RED = 0;

    /**
     * LEDs GREEN value 0-255.
     */
    public int GREEN = 0;

    /**
     * LEDs BLUE value 0-255.
     */
    public int BLUE = 0;

    /**
     * LEDs BRIGHTNESS value 0-255.
     */
    public int BRIGHTNESS = 0;

    /**
     * The LED described by this LED object.
     */
    public final int led;

    /**
     * LED Constructor.
     * @param led The LED you want to get with this LED object. Can be 0,1, or 2.
     */
    public LED(int led) {
        this.led = led;
    }

    protected void setValue(int rgb, int brightness) {
        MControl mc = new MControl();
        mc.set_led_status(led, brightness, rgb);
    }

    /**
     * Get the color value of the LED.
     * @return Integer color value.
     */
    public int getColorValue() {
        return Color.argb(0xFF, RED, GREEN, BLUE);
    }
}
