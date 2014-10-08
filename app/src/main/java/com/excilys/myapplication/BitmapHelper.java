package com.excilys.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHelper {

    ArrayList<Bitmap> createBitmaps(Bitmap bMap) {

        ArrayList<Bitmap> bitmapsArray = new ArrayList<Bitmap>();

        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 864, 705, true);

        bitmapsArray.add(Bitmap.createBitmap(bMapScaled, 0, 0, 16, 16));
        bitmapsArray.add(Bitmap.createBitmap(bMapScaled, 0, 16, 16, 16));

        return bitmapsArray;
    }

}
