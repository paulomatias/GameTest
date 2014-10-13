package com.excilys.gametest.tile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

@EBean
public class Tile extends ImageView {
    private int collide = 0;
    private List<Drawable> charSet;

    public Tile(Context context) {
        super(context);
    }

//    public Tile(Context context, int collide) {
//        super(context);
//        this.collide = collide;
//    }
//
//    public Tile(Context context, int collide, List<Drawable> charSet) {
//        super(context);
//        this.collide = collide;
//        this.charSet = charSet;
//    }

    @UiThread
    public void setImage(int chiffre) {
        this.setImageDrawable(charSet.get(chiffre));
    }

    //TODO animate the tile
    public void animateTile(){
        Log.d(this.getClass().getSimpleName(), "In method animate tiles : "+charSet.size());
        if (charSet.size() > 0 ){
            CountDownTimer timer = new CountDownTimer(10000, 500) {
                int chiffre = 0;
                @Override
                public void onTick(long millisUntilFinished) {
                    chiffre++;
                    if(chiffre < charSet.size())
                        setImage(chiffre);
                    else{
                        chiffre = 0;
                        setImage(0);
                    }
                }

                @Override
                public void onFinish() {
                    animateTile();
                }
            };
            timer.start();
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
