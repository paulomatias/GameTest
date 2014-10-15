package com.excilys.gametest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class GameTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametest);
    }

    @Click(R.id.btn_town)
    void goTown(){
        Intent intent = new Intent(this, Town_.class);
        startActivity(intent);
    }

    @Click(R.id.btn_map)
    void goMap(){
        Intent intent = new Intent(this, Map_.class);
        startActivity(intent);
    }
}
