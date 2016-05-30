package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import database.DbHelper;

public class ViewData extends Activity {

    private final static String FILE_DIRECTORY = "/sdcard/SportsMove/";
    private final static String FILE_ORIENTATION = "_orientation.csv";
    private final static String FILE_ACCELEROMETER = "_accelerometer.csv";
    private final static String FILE_GYROSCOPE = "_gyroscope.csv";

    private ListView obj;
    private DbHelper mydb;

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
        mydb.clearAllTables();
    }

    public void exportCSV(View view) {
        //Accelerometer
        try {
            File directory = new File(FILE_DIRECTORY);
            directory.mkdirs();

            File myFile = new File(FILE_DIRECTORY+mydb.getAccelerometerMovename("1")+FILE_ACCELEROMETER);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; X; Y; Z; Current Arm; Username; Move Name; \n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);

            if(mydb.getAllAccelerometerRegists().size()!=0){
                ArrayList<String> array_list = new ArrayList<String>(mydb.getAllAccelerometerRegists());

                for (String temp : array_list) {
                    fw.append(temp + "\n");
                }

                Toast.makeText(getApplicationContext(), "Exported", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No data to export", Toast.LENGTH_SHORT).show();
            }

            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        //Gyroscope
        try {
            File directory = new File(FILE_DIRECTORY);
            directory.mkdirs();

            File myFile = new File(FILE_DIRECTORY+FILE_GYROSCOPE);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; X; Y; Z; Current Arm; Username; Move Name; \n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);

            if(mydb.getAllGyroscopeRegists().size()!=0){
                ArrayList<String> array_list = new ArrayList<String>(mydb.getAllGyroscopeRegists());

                for (String temp : array_list) {
                    fw.append(temp + "\n");
                }

                Toast.makeText(getApplicationContext(), "Exported", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No data to export", Toast.LENGTH_SHORT).show();
            }

            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        //Orientation

        try {
            File directory = new File(FILE_DIRECTORY);
            directory.mkdirs();

            File myFile = new File(FILE_DIRECTORY+FILE_ORIENTATION);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; W; X; Y; Z; Current Arm; Username; Move Name; \n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);

            if(mydb.getAllOrientationRegists().size()!=0){
                ArrayList<String> array_list = new ArrayList<String>(mydb.getAllOrientationRegists());

                for (String temp : array_list) {
                    fw.append(temp + "\n");
                }

                Toast.makeText(getApplicationContext(), "Exported", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No data to export", Toast.LENGTH_SHORT).show();
            }

            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
