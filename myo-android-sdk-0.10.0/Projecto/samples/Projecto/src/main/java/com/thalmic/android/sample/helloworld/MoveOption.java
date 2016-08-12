package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import database.DbHelper;

public class MoveOption extends Activity {

    private DbHelper mydb;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_option);
        mydb = new DbHelper(this);

        listView = (ListView) findViewById(R.id.lstMoves);

        populateList(mydb.getAllRegists());
    }

    public void createMove(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void populateList(ArrayList arrayList){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        listView.setAdapter(adapter);
    }

}
