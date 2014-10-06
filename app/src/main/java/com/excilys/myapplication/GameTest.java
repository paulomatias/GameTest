package com.excilys.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);

        TableLayout tlGameBoard = (TableLayout) findViewById(R.id.tl_gameBoard);
        LinearLayout llGameBoard = (LinearLayout) findViewById(R.id.ll_gameBoard);
        Drawable drawable = getResources().getDrawable(R.drawable.textview_border);
        TableRow.LayoutParams params = new TableRow.LayoutParams(64, 64);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 10; j++) {
                final TextView textView = new TextView(this);
                textView.setLayoutParams(params);
                textView.setText("");
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.grass);
                textView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       textView.setBackgroundColor(Color.BLUE);
                    }
                });
                tableRow.addView(textView);
            }
            tlGameBoard.addView(tableRow);
            llGameBoard.setGravity(Gravity.CENTER);
        }
        tlGameBoard.setRotation(45);
        tlGameBoard.setRotationX(10);
    }
}
