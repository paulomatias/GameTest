package com.excilys.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHelper {

    ArrayList<Bitmap> createBitmaps(Bitmap bMap, int tileSize, int tileSpace) {
        ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
        int bMapWidth = bMap.getWidth();
        int bMapHeight = bMap.getHeight();
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, bMapWidth, bMapHeight, true);
        for (int y = 0; y < bMapHeight; y += tileSize + tileSpace) {
            for (int x = 0; x < bMapWidth; x += tileSize + tileSpace) {
                bitmapList.add(Bitmap.createBitmap(bMapScaled, x, y, tileSize, tileSize));
            }
        }
        return bitmapList;
    }

    // Avec x et y nécessairement supérieur à 1
    public static int superCalcul(int x, int y) {
        if (x < 1 || y < 1) {
            return 0;
        }
        int a;
        if (y > 1) {
            a = (y - 1) * 61 + x - 1;
        } else {
            a = x - 1;
        }
        return a;
    }
}
