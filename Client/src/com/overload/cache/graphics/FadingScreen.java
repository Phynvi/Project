package com.overload.cache.graphics;

import org.apache.commons.lang3.time.StopWatch;

import com.overload.Client;
import com.overload.draw.Rasterizer2D;

public class FadingScreen {
    /**
     * The state of the fade on the screen
     */
    private byte state;
    
    /**
     * How many seconds the fade exists for
     */
    private byte seconds;
    
    /**
     * The string of text that will be displayed on the screen
     */
    private String text;
    
    /**
     * Controls how long it's been since the screen started its last fade
     */
    private StopWatch watch;
    
    /**
     * Acts as a means to instance the local {@link FadingScreen} object.
     * Since the value of state by default is 0, nothing will be drawn on
     * the screen.
     */
    public FadingScreen() { this.state = 0; }
    
    /**
     * Creates a new fading screen
     * @param state        the state of the fade
     * @param seconds    the duration of the fade
     */
    public FadingScreen(String text, byte state, byte seconds) {
        this.text = text;
        this.state = state;
        this.seconds = seconds;
        this.watch = new StopWatch();
        this.watch.start();
    }
    
    /**
     * Draws the animation on the screen. If the state of the
     * screen is currenly 0 the animation will not be drawn.
     */
    public void draw() {
        if(state == 0) {
            return;
        }
        long end = watch.getStartTime() + (1000L * seconds);
        long increment = ((end - watch.getStartTime()) / 100);
        if (increment > 0) {
            long percentile = watch.getTime() / increment;
            int opacity = (int) ((percentile * (Byte.MAX_VALUE / 100)) * 2);
            if (state < 0) {
                opacity = 255 - opacity;
            }
            if (percentile > -1 && percentile <= 100) {
            	Rasterizer2D.setDrawingArea(334, 0, 512, 0);
                Rasterizer2D.drawTransparentBox(0, 0, 512, 334, 0x000000, opacity);
                if (percentile > 50 && state == 1 || percentile < 50 && state == -1) {
                    Client.instance.newRegularFont.drawCenteredString(text, 512 / 2, 334 / 2, 0xFFFFFF, 0);
                } else if (percentile == 100) {
                    watch.stop();
                    state = 0;
                }
            }
        }
    }
}