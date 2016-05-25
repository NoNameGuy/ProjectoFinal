package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoveOption extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_option);
    }

    public void createMove(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void savedMoves(View view) {
        Intent intent = new Intent(getApplicationContext(), MoveList.class);
        startActivity(intent);
    }
}
