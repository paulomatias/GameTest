package com.excilys.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import helper.StringHelper;


public class GameTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);

        String mapString = StringHelper.convertStreamToString(getResources().openRawResource(R.raw.tilemap01));
        Log.i(this.getClass().toString(), "mapString : " + mapString);
        String[] map = mapString.split("\\+");

        TableLayout tlGameBoard = (TableLayout) findViewById(R.id.tl_gameBoard);
        LinearLayout llGameBoard = (LinearLayout) findViewById(R.id.ll_gameBoard);
        TableRow.LayoutParams params = new TableRow.LayoutParams(64, 64);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 10; j++) {
                final TextView textView = new TextView(this);
                textView.setLayoutParams(params);
                textView.setText("");
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(switchTile(Integer.parseInt(map[i].substring(j, j + 1))));

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setBackgroundColor(Color.BLUE);
                    }
                });
                tableRow.addView(textView);
            }
            tlGameBoard.addView(tableRow);
            llGameBoard.setGravity(Gravity.CENTER);
            llGameBoard.setBackground(getResources().getDrawable(R.drawable.grass_background));
        }
        tlGameBoard.setRotation(45);
        tlGameBoard.setRotationX(10);
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
}
