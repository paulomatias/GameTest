package com.excilys.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHelper {

    ArrayList<Bitmap> createBitmaps(Bitmap bMap) {
        ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
        int bMapWidth = bMap.getWidth();
        int bMapHeight = bMap.getHeight();
        int tileSize = 16;
        int tileSpace = 1;
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, bMapWidth, bMapHeight, true);
        for (int y = 0; y < bMapHeight; y += tileSize + tileSpace) {
            for (int x = 0; x < bMapWidth; x += tileSize + tileSpace) {
                bitmapList.add(Bitmap.createBitmap(bMapScaled, x, y, tileSize, tileSize));
            }
        }
        return bitmapList;
    }

}
