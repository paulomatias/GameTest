package com.excilys.gametest.tile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

public class Tile extends ImageView {
    private int collide = 0;
    private List<Drawable> charSet;

    public Tile(Context context) {
        super(context);
    }

    public Tile(Context context, int collide) {
        super(context);
        this.collide = collide;
    }

    public Tile(Context context, int collide, List<Drawable> charSet) {
        super(context);
        this.collide = collide;
        this.charSet = charSet;
    }

    //TODO animate the tile
    public void animateTile(){
        if (charSet.size() > 0 ){

        }
    }

    public int getCollide() {
        return collide;
    }

    public List<Drawable> getCharSet() {
        return charSet;
    }

    public void setCollide(int collide) {
        this.collide = collide;
    }

    public void setCharSet(List<Drawable> charSet) {
        this.charSet = charSet;
    }
}
