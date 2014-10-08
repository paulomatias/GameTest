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
    private RelativeLayout rlBoardContainer;
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

        TableLayout tlGameBoardGround = (TableLayout) findViewById(R.id.tl_gameBoardGround);
        TableLayout tlGameBoardElement = (TableLayout) findViewById(R.id.tl_gameBoardElement);
        RelativeLayout rlGameBoard = (RelativeLayout) findViewById(R.id.rl_gameBoard);
        hScroll = (HScroll) findViewById(R.id.hs_gameBoard);
        vScroll = (VScroll) findViewById(R.id.vs_gameBoard);
        rlBoardContainer = (RelativeLayout) findViewById(R.id.rl_boardContainer);
        Drawable drawable = getResources().getDrawable(R.drawable.textview_border);

        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        TableRow.LayoutParams params = new TableRow.LayoutParams(tileSize, tileSize);
        maxLengthY = map.length;

        // Population du tableau représentant la carte
        for (int i = 0; i < map.length; i++) {
            //Ajout d'une row pour ground
            TableRow tableRowGround = new TableRow(this);
            //Ajout d'une row pour element
            TableRow tableRowElement = new TableRow(this);
            for (int j = 0; j < map[i].length(); j++) {
                if (map[i].length() > maxLengthX) {
                    maxLengthX = map[i].length();
                }
                //Creation image view pour ground
                final ImageView imageViewGround = new ImageView(this);
                imageViewGround.setLayoutParams(params);
                imageViewGround.setBackground(switchTile(Integer.parseInt(map[i].substring(j, j + 1))));
                tableRowGround.addView(imageViewGround);
                //Creation image view pour ground
                final ImageView imageViewElement = new ImageView(this);
                imageViewElement.setLayoutParams(params);
//                imageViewElement.setBackground(dTransparent);
                tableRowElement.addView(imageViewElement);
                //click on element tile
                imageViewElement.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentImageView = imageViewElement;
                        return false;
                    }
                });
            }
            tlGameBoardGround.addView(tableRowGround);
            tlGameBoardElement.addView(tableRowElement);
            rlGameBoard.setBackground(resources.getDrawable(R.drawable.grass_background));
        }

        // Initialisation et positionnement du GameBoard
        initGameBoardPosition(tlGameBoardGround, tlGameBoardElement);
    }

    private void initGameBoardPosition(TableLayout tlGameBoardGround, TableLayout tlGameBoardElement) {
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

        rlBoardContainer.setMinimumHeight(heightScroll);
        hScroll.setMinimumHeight(heightScroll);
        vScroll.setMinimumHeight(heightScroll);

        rlBoardContainer.setMinimumWidth(widthScroll);
        hScroll.setMinimumWidth(widthScroll);
        vScroll.setMinimumWidth(widthScroll);

        FrameLayout.LayoutParams vParams = new VScroll.LayoutParams(widthScroll, heightScroll);
        hScroll.setLayoutParams(vParams);

        tlGameBoardGround.setTranslationX(offsetLeft);
        tlGameBoardGround.setTranslationY(offsetTop);
        tlGameBoardGround.setRotation(45);
        tlGameBoardElement.setTranslationX(offsetLeft);
        tlGameBoardElement.setTranslationY(offsetTop);
        tlGameBoardElement.setRotation(45);
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
