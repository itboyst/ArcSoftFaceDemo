package com.arcsoft.facedemo.base;


public class ImageInfo {
    public byte[] rgbData;
    public int width;
    public int height;

    public byte[] getRgbData() {
        return rgbData;
    }

    public void setRgbData(byte[] rgbData) {
        this.rgbData = rgbData;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
