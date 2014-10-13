package com.excilys.gametest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.excilys.gametest.views.HScroll;
import com.excilys.gametest.views.VScroll;

import java.util.List;

import com.excilys.gametest.helper.BitmapHelper;
import com.excilys.gametest.helper.StringHelper;

public class GameTest extends Activity {

    private ScaleGestureDetector scaleGestureDetector;
    private int tileSize = 32;
    private float mx, my;
    private boolean moved = false;
    private boolean isZoomed = false;
    private HScroll hScroll;
    private VScroll vScroll;
    private TableLayout tlGameBoardElement;
    private TableLayout tlGameBoardGround;
    private RelativeLayout rlBoardContainer;
    private RelativeLayout rlGameBoard;
    private float scaleFactor = 1.0f;
    private List<Bitmap> bitmaps;
    private ImageView currentImageView;
    private int maxLengthX = 0;
    private int maxLengthY = 0;
    private Resources resources;

    // Drawable pour la map
    private Drawable dCaracter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);

        //découpage de la tile map
        cutBitmap();
        resources = getResources();

        //Découpage des Tilemap
        String mapBackground1 = StringHelper.convertStreamToString(getResources().openRawResource(R.raw.tilemap01));
        String mapImage1 = StringHelper.convertStreamToString(getResources().openRawResource(R.raw.tilemap02));
        String[] mapLine1 = mapBackground1.split("\\+");
        String[] mapLine2 = mapImage1.split("\\+");

        tlGameBoardGround = (TableLayout) findViewById(R.id.tl_gameBoardGround);
        tlGameBoardElement = (TableLayout) findViewById(R.id.tl_gameBoardElement);
        rlGameBoard = (RelativeLayout) findViewById(R.id.rl_gameBoard);
        hScroll = (HScroll) findViewById(R.id.hs_gameBoard);
        vScroll = (VScroll) findViewById(R.id.vs_gameBoard);
        rlBoardContainer = (RelativeLayout) findViewById(R.id.rl_boardContainer);
        dCaracter = resources.getDrawable(R.drawable.caracter);

        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        TableRow.LayoutParams params = new TableRow.LayoutParams(tileSize, tileSize);
        setMap(mapLine1, mapLine2, params, 1);
        rlGameBoard.setBackground(resources.getDrawable(R.drawable.grass_background));
    }

    //Création de la map
    public void setMap(String[] mapBackground, String[] mapImage, TableRow.LayoutParams params, int layer) {
        maxLengthY = mapBackground.length;

        // Population du tableau représentant la carte
        for (int i = 0; i < maxLengthY; i++) {
            //Ajout d'une row pour ground
            TableRow tableRowGround = new TableRow(this);
            //Ajout d'une row pour element
            TableRow tableRowElement = new TableRow(this);
            String[] mapColumn1 = mapBackground[i].split("x");
            String[] mapColumn2 = mapImage[i].split("x");

            for (int j = 0; j < mapColumn1.length; j++) {
                if (mapColumn1.length > maxLengthX) {
                    maxLengthX = mapColumn1.length;
                }
                //Creation image view pour ground
                final ImageView imageViewGround = new ImageView(this);
                imageViewGround.setLayoutParams(params);
                String[] tileRotation1 = mapColumn1[j].split(":");
                String[] tileRotation2 = mapColumn2[j].split(":");

                if (!mapColumn1[j].equals("") && !mapColumn1[j].equals("0") && tileRotation1.length > 1 && layer == 1) {
                    imageViewGround.setBackground(getDrawableTile(Integer.parseInt(tileRotation1[0]), Integer.parseInt(tileRotation1[1])));
                } else if (mapColumn1[j] != "" && !mapColumn1[j].equals("0") && layer == 1) {
                    imageViewGround.setBackground(getDrawableTile(Integer.parseInt(mapColumn1[j]), 0));
                }
                if (!mapColumn2[j].equals("") && !mapColumn2[j].equals("0") && tileRotation2.length > 1 && layer == 1) {
                    imageViewGround.setImageDrawable(getDrawableTile(Integer.parseInt(tileRotation2[0]), Integer.parseInt(tileRotation2[1])));
                } else if (mapColumn2[j] != "" && !mapColumn2[j].equals("0") && layer == 1) {
                    imageViewGround.setImageDrawable(getDrawableTile(Integer.parseInt(mapColumn2[j]), 0));
                }
                tableRowGround.addView(imageViewGround);

                //Creation image view pour Element
                final ImageView imageViewElement = new ImageView(this);
                imageViewElement.setLayoutParams(params);
                if (!mapColumn1[j].equals("") && !mapColumn1[j].equals("0") && tileRotation1.length > 1 && layer == 2) {
                    imageViewElement.setBackground(getDrawableTile(Integer.parseInt(tileRotation1[0]), Integer.parseInt(tileRotation1[1])));
                } else if (!mapColumn1[j].equals("") && !mapColumn1[j].equals("0") && layer == 2) {
                    imageViewElement.setBackground(getDrawableTile(Integer.parseInt(mapColumn1[j]), 0));
                }
                if (!mapColumn2[j].equals("") && !mapColumn2[j].equals("0") && tileRotation2.length > 1 && layer == 2) {
                    imageViewElement.setImageDrawable(getDrawableTile(Integer.parseInt(tileRotation2[0]), Integer.parseInt(tileRotation2[1])));
                } else if (!mapColumn2[j].equals("") && !mapColumn2[j].equals("0") && layer == 2) {
                    imageViewElement.setImageDrawable(getDrawableTile(Integer.parseInt(mapColumn2[j]), 0));
                }
                tableRowElement.addView(imageViewElement);

                //click on element tile
                imageViewElement.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (imageViewElement.getDrawable() == null) {
                            imageViewElement.setImageDrawable(dCaracter);
                        }
                        return false;
                    }
                });
            }
            tlGameBoardGround.addView(tableRowGround);
            tlGameBoardElement.addView(tableRowElement);

        }
    }

    /**
     * Découpe le tileset et stock les images dans bitmaps
     */
    private void cutBitmap() {
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.tilesetdemo);
        BitmapHelper bitmapHelper = new BitmapHelper();
        bitmaps = BitmapHelper.createBitmaps(bMap, 16, 1);
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
                            currentImageView.setBackground(dCaracter);
                            currentImageView.setRotation(-45);
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

    private Drawable getDrawableTile(int tileNumber, int tileRotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(tileRotation);

        return new BitmapDrawable(resources, Bitmap.createBitmap(bitmaps.get(tileNumber), 0, 0, bitmaps.get(tileNumber).getWidth(), bitmaps.get(tileNumber).getHeight(), matrix, true));
    }


    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            isZoomed = true;
            // don't let the object get too small or too large.
            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 4.0f));
            rlGameBoard.setScaleX(scaleFactor);
            rlGameBoard.setScaleY(scaleFactor);
            return true;
        }
    }
}
