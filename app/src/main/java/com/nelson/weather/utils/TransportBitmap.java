package com.nelson.weather.utils;

import android.graphics.Bitmap;

/**
 * @author nelson
 */
public class TransportBitmap {

    private  Bitmap bitmap;
    private static class TransportBitmapHolder {
        private static final TransportBitmap instance = new TransportBitmap();
    }

    private TransportBitmap(){}
    public static final TransportBitmap getInstance(){
        return TransportBitmapHolder.instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
