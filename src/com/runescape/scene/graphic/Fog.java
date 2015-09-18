package com.runescape.scene.graphic;
import com.runescape.media.Raster;

public class Fog {    
    /**
     * Grabs distance from player.
     */
    private float fogDistance;
    
    /**
     * Sets the fog color.
     */
    public static int setColor = 0xCBCAB6;
    
    /**
     * 
     * @param fogStartDistance
     * @param fogEndDistance
     * @param fogIntensity
     */
    public void renderFog(boolean belowGround, int fogStartDistance, int fogEndDistance, int fogIntensity) {
        getColor(setColor);
        int pos = Rasterizer.anIntArray1472[0];
        int src, dst, alpha;
        int fogBegin = (int) (fogStartDistance + fogDistance);
        int fogEnd = (int) (fogEndDistance + fogDistance);
        for (int y = 0; y < Raster.bottomY; y++) {
            for (int x = 0; x < Raster.centerX; x++) {
                if (Raster.depthBuffer[pos] >= fogEnd) {
                    Raster.pixels[pos] = setColor;
                } else if (Raster.depthBuffer[pos] >= fogBegin) {
                    alpha = (int)(Raster.depthBuffer[pos] - fogBegin) / fogIntensity;
                    src = ((setColor & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((setColor & 0xff00) * alpha >> 8 & 0xff00);
                    alpha = 256 - alpha;
                    dst = Raster.pixels[pos];
                    dst = ((dst & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((dst & 0xff00) * alpha >> 8 & 0xff00);
                    Raster.pixels[pos] = src + dst;
                }
                pos++;
            }
            pos += Raster.width - Raster.centerX;
        }
    }
    
    /**
     * 
     * @param fogDistance
     */
    public void setFogDistance(float fogDistance) {
        this.fogDistance = fogDistance;
    }
    
    /**
     * 
     * @param fogColor
     */
    public void getColor(int fogColor) {
        setColor = fogColor;
    }
}
