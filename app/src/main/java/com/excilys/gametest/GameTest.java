package com.excilys.gametest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;


public class GameTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);
        TableLayout t = (TableLayout) findViewById(R.id.tableLayout);
        t.setRotationY(-10);
        t.setRotationX(45);
    }
}
