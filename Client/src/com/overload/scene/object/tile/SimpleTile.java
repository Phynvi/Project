package com.overload.scene.object.tile;

public final class SimpleTile {

    public final int northEastColor;
    public final int northColor;
    public final int centerColor;
    public final int eastColor;
    public final int texture;
    public final boolean flat;
    public final int colorRGB;
    public final boolean textured;


    public SimpleTile(int northEastColor, int northColor, int centerColor, int eastColor, int texture, int colorRGB, boolean flat, boolean tex) {
        this.northEastColor = northEastColor;
        this.northColor = northColor;
        this.centerColor = centerColor;
        this.eastColor = eastColor;
        this.texture = texture;
        this.colorRGB = colorRGB;
        this.flat = flat;
        this.textured = tex;
    }

    public int getNorthEastColor() {
        return northEastColor;
    }

    public int getNorthColor() {
        return northColor;
    }

    public int getCenterColor() {
        return centerColor;
    }

    public int getEastColor() {
        return eastColor;
    }

    public int getTexture() {
        return texture;
    }

    public boolean isFlat() {
        return flat;
    }

    public int getColourRGB() {
        return colorRGB;
    }

}
