package com.ghosts.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Ahmed Allam on 2/23/2016.
 */
public class PictureUtils {
    public static Bitmap getScaledBitmap(String path , int destWidth , int destHieght){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path , options);

        float srcWidth = options.outWidth;
        float srcHieght = options.outHeight;

        int inSampleScale = 1;
        if(srcWidth > destWidth || srcHieght > destHieght){
            if(srcWidth > srcHieght){
                inSampleScale = Math.round(srcHieght / destHieght);
            }else
                inSampleScale = Math.round(srcWidth / destWidth);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleScale;

        return BitmapFactory.decodeFile(path , options);

    }

    public static Bitmap getScaledBitmap(String path , Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path , size.x , size.y);
    }
}
