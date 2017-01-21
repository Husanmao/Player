package com.huawei.colin.mediaplayer.util.component;

import android.graphics.Bitmap;

/**
 * Created by Colin on 2017/1/22.
 * Description:NA
 */
public class LoadedImage {

    private Bitmap mBitmap;

    public LoadedImage(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }
}
