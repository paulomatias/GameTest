package com.excilys.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import helper.StringHelper;


public class GameTest extends Activity {

    private ScaleGestureDetector scaleGestureDetector;
    private int boardLength = 10;
    private int tileSize = 64;
    private float mx, my;
    private boolean moved = false;
    private boolean isZoomed = false;
    private HScroll hScroll;
    private VScroll vScroll;
    private float scaleFactor = 1.0f;
    private TextView currentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);

        String mapString = StringHelper.convertStreamToString(getResources().openRawResource(R.raw.tilemap01));
        Log.i(this.getClass().toString(), "mapString : " + mapString);
        String[] map = mapString.split("\\+");

        TableLayout tlGameBoard = (TableLayout) findViewById(R.id.tl_gameBoard);
        LinearLayout llGameBoard = (LinearLayout) findViewById(R.id.ll_gameBoard);
        hScroll = (HScroll) findViewById(R.id.hs_gameBoard);
        vScroll = (VScroll) findViewById(R.id.vs_gameBoard);
        hScroll.setMinimumHeight((int) (boardLength * tileSize * Math.sqrt(2D)));
        hScroll.setMinimumWidth((int) (boardLength * tileSize * Math.sqrt(2D)));
        vScroll.setMinimumHeight((int) (boardLength * tileSize * Math.sqrt(2D)));
        vScroll.setMinimumWidth((int) (boardLength * tileSize * Math.sqrt(2D)));
        Drawable drawable = getResources().getDrawable(R.drawable.textview_border);


        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(),
                new ScaleListener());
        TableRow.LayoutParams params = new TableRow.LayoutParams(tileSize, tileSize);
        for (int i = 0; i < boardLength; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < boardLength; j++) {
                final TextView textView = new TextView(this);
                textView.setLayoutParams(params);
                textView.setText("");
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(drawable);
                textView.setBackground(switchTile(Integer.parseInt(map[i].substring(j, j + 1))));

                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            mx = event.getRawX();
                            my = event.getRawY();
                            currentTextView = textView;

                            return true;
                        }
                        return false;
                    }
                });
                tableRow.addView(textView);
            }
            tlGameBoard.addView(tableRow);
            llGameBoard.setGravity(Gravity.CENTER);
            llGameBoard.setBackground(getResources().getDrawable(R.drawable.grass_background));
        }
        tlGameBoard.setRotation(45);
//        tlGameBoard.setRotationX(10);
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


                        if (!moved && currentTextView != null) {
                          //Traitement lors du click sur une case
                            currentTextView.setBackgroundColor(Color.BLUE);


                        } else {
                            currentTextView = null;
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
                return getResources().getDrawable(R.drawable.grass);
            case 1:
                return getResources().getDrawable(R.drawable.rock);
            case 2:
                return getResources().getDrawable(R.drawable.stone);
            case 3:
                return getResources().getDrawable(R.drawable.water);
            default:
                break;
        }
        return null;
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

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            isZoomed = true;
            // don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 1.0f));
            if (scaleFactor > 0.5F && scaleFactor < 1.0F && detector.isInProgress()) {


                //Zoom

//                DisplayMetrics metrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int[] loc = new int[2];
//                Log.i("paulo", "ZOOM!!");
//                hexagonBoard.getLocationInWindow(loc);
//                hexagonBoard.setPivotX((x * (detector.getCurrentSpanX() / detector.getPreviousSpanX()) - loc[0]) / scaleFactor);
//                hexagonBoard.setPivotY((y * (detector.getCurrentSpanY() / detector.getPreviousSpanY()) - loc[1]) / scaleFactor);
//                hexagonBoard.setScaleX(scaleFactor);
//                hexagonBoard.setScaleY(scaleFactor);
            }
            return true;
        }
    }
}
