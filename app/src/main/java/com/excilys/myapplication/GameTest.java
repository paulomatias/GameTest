package com.excilys.myapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import helper.StringHelper;

public class GameTest extends Activity {

    private ScaleGestureDetector scaleGestureDetector;
    private int tileSize = 64;
    private float mx, my;
    private boolean moved = false;
    private boolean isZoomed = false;
    private HScroll hScroll;
    private VScroll vScroll;
    private float scaleFactor = 1.0f;
    private ImageView currentImageView;
    private int maxLengthX = 0;
    private int maxLengthY = 0;
    private Resources resources;

    // Drawable pour la map
    private Drawable dTransparent;
    private Drawable dGrass;
    private Drawable dStone;
    private Drawable dWater;
    private Drawable dRock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);

        resources = getResources();
        dTransparent = resources.getDrawable(R.drawable.transparent);
        dGrass = resources.getDrawable(R.drawable.grass);
        dStone = resources.getDrawable(R.drawable.stone);
        dWater = resources.getDrawable(R.drawable.water);
        dRock = resources.getDrawable(R.drawable.rock);

        //Découpage des Tilemap
        String mapString = StringHelper.convertStreamToString(getResources().openRawResource(R.raw.tilemap01));
        String[] map = mapString.split("\\+");

        TableLayout tlGameBoard = (TableLayout) findViewById(R.id.tl_gameBoard);
        RelativeLayout rlGameBoard = (RelativeLayout) findViewById(R.id.rl_gameBoard);
        hScroll = (HScroll) findViewById(R.id.hs_gameBoard);
        vScroll = (VScroll) findViewById(R.id.vs_gameBoard);
        Drawable drawable = getResources().getDrawable(R.drawable.textview_border);

        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        TableRow.LayoutParams params = new TableRow.LayoutParams(tileSize, tileSize);
        maxLengthY = map.length;

        // Population du tableau représentant la carte
        for (int i = 0; i < map.length; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < map[i].length(); j++) {
                if (map[i].length() > maxLengthX) {
                    maxLengthX = map[i].length();
                }
                final ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                imageView.setBackground(drawable);
                imageView.setBackground(switchTile(Integer.parseInt(map[i].substring(j, j + 1))));

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            mx = event.getRawX();
                            my = event.getRawY();
                            currentImageView = imageView;
                            return true;
                        }
                        return false;
                    }
                });
                tableRow.addView(imageView);
            }
            tlGameBoard.addView(tableRow);
            rlGameBoard.setBackground(resources.getDrawable(R.drawable.grass_background));
        }

        // Initialisation et positionnement du GameBoard
        initGameBoardPosition(tlGameBoard);
    }

    private void initGameBoardPosition(TableLayout tlGameBoard) {
        float offsetTop;
        float offsetLeft;
        if (maxLengthX >= maxLengthY) {
            offsetTop = (float) -(Math.sqrt(2) / 4 * tileSize * (-maxLengthX + maxLengthY) - maxLengthY * tileSize / 4);
            offsetLeft = (float) -(Math.sqrt(2) / 4 * tileSize * (-maxLengthX - maxLengthY) + maxLengthX * tileSize / 4);
        } else {
            offsetTop = (float) -(Math.sqrt(2) / 4 * tileSize * (-maxLengthY + maxLengthX) - maxLengthX * tileSize / 4);
            offsetLeft = (float) -(Math.sqrt(2) / 4 * tileSize * (-maxLengthY - maxLengthX) + maxLengthY * tileSize / 4);
        }

        int heightScroll = (int) (maxLengthY * tileSize + offsetTop * 2);
        int widthScroll = (int) (maxLengthX * tileSize + offsetLeft * 2);

        hScroll.setMinimumHeight(heightScroll);
        vScroll.setMinimumHeight(heightScroll);

        hScroll.setMinimumWidth(widthScroll);
        vScroll.setMinimumWidth(widthScroll);

        FrameLayout.LayoutParams vParams = new VScroll.LayoutParams(widthScroll, heightScroll);
        hScroll.setLayoutParams(vParams);

        tlGameBoard.setTranslationX(offsetLeft);
        tlGameBoard.setTranslationY(offsetTop);
        tlGameBoard.setRotation(45);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curX, curY;
        scaleGestureDetector.onTouchEvent(event);
        // get masked (not specific to a pointer) action
        int maskedAction = event.getAction();

        if (event.getPointerCount() == 1) {
            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                    mx = event.getX();
                    my = event.getY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    curX = event.getX();
                    curY = event.getY();
                    if (!isZoomed && (Math.abs(my - curY) > 10 || Math.abs(mx - curX) > 10)) {

                        vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                        hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                        mx = curX;
                        my = curY;
                        moved = true;
                    }
                    return true;
                case MotionEvent.ACTION_UP:

                    if (!isZoomed) {
                        curX = event.getX();
                        curY = event.getY();
                        if (!moved && currentImageView != null) {
                            //Traitement lors du click sur une case
                            currentImageView.setBackgroundColor(Color.BLUE);
                        } else {
                            currentImageView = null;
                            vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                            hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                        }
                        mx = event.getX();
                        my = event.getY();
                        moved = false;
                    }
                    isZoomed = false;
                    return true;
            }
        }
        return false;
    }

    private Drawable switchTile(int tileNumber) {

        switch (tileNumber) {
            case 0:
                return dTransparent;
            case 1:
                return dGrass;
            case 2:
                return dRock;
            case 3:
                return dStone;
            case 4:
                return dWater;
            default:
                break;
        }
        return null;
    }
            if (scaleFactor > 0.5F && scaleFactor < 1.0F && detector.isInProgress()) {
            }
    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        float x;
        float y;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            x = detector.getFocusX();
            y = detector.getFocusY();
            return true;
        }

        //TODO ZOOM
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            isZoomed = true;
            // don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 1.0f));
            return true;
        }
    }
}
