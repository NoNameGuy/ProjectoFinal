package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PlaybackMove extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_move);
    }

    public void play(View view){
        Toast.makeText(getApplicationContext(), "Play", Toast.LENGTH_SHORT).show();
    }

    public void stop(View view){
        Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
    }

}
