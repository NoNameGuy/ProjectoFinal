package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserType extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
    }

    public void selectTeacher(View view) {
        Intent intent = new Intent(getApplicationContext(), MoveOption.class);
        startActivity(intent);
    }

    public void selectStudent(View view) {
        Intent intent = new Intent(getApplicationContext(), MoveList.class);
        startActivity(intent);
    }
}
