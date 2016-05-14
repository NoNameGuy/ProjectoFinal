package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import database.DbHelper;

public class ViewData extends Activity {

    private ListView obj;
    DbHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        mydb = new DbHelper(this);
        obj = (ListView)findViewById(R.id.listView);

        showAccelerometerData(findViewById(R.id.btnAccelerometer));

    }

    public void showAccelerometerData(View view) {
        if(mydb.getAllAccelerometerRegists().size()!=0){
        ArrayList<String> array_list = new ArrayList<String>(mydb.getAllAccelerometerRegists());
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj.setAdapter(arrayAdapter);}


    }

    public void showGyroscopeData(View view) {

        obj.setAdapter(null);
        if(mydb.getAllGyroscopeRegists().size()!=0){
        ArrayList<String> array_list = new ArrayList<String>(mydb.getAllGyroscopeRegists());
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj.setAdapter(arrayAdapter);}
    }

    public void showOrientationData(View view) {

        if(mydb.getAllOrientationRegists().size()!=0) {
            ArrayList<String> array_list = new ArrayList<String>(mydb.getAllOrientationRegists());
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

            obj.setAdapter(arrayAdapter);
        }
    }

    public void clearAll(View view){
        Toast.makeText(getApplicationContext(),"clear all", Toast.LENGTH_SHORT).show();
        mydb.clearAllTables();
    }
}
