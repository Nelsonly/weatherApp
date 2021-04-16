package com.nelson.weather.utils;

import android.graphics.Bitmap;

public class TranslateBitmap {

    private  Bitmap bitmap;
    private static class TranslateBitmapHolder{
        private static final TranslateBitmap instance = new TranslateBitmap();
    }

    private TranslateBitmap(){}
    public static final TranslateBitmap getInstance(){
        return TranslateBitmapHolder.instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
