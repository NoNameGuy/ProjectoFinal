package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import database.DbHelper;

public class ViewData extends Activity {

    private final static String FILE_DIRECTORY = "/sdcard/SportsMove/";
    private final static String FILE_ORIENTATION = "_orientation.csv";
    private final static String FILE_ACCELEROMETER = "_accelerometer.csv";
    private final static String FILE_GYROSCOPE = "_gyroscope.csv";
    private static String FILE_MOVE =null;
    public String MOVENAME="MOVENAME";
    public String USERNAME="USERNAME";
    public String DATE="DATE";
    private String moveName;
    private String userName;
    private String date;

    private ListView obj;
    private DbHelper mydb;

    //private Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
    //private Spinner usernameSpinner = (Spinner) findViewById(R.id.usernameSpinner);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        mydb = new DbHelper(this);
        obj = (ListView)findViewById(R.id.listView);

        showAccelerometerData(findViewById(R.id.btnAccelerometer));
        moveName=(String) getIntent().getExtras().get(MOVENAME);
        userName=(String) getIntent().getExtras().get(USERNAME);
        date=(String) getIntent().getExtras().get(DATE);

        populateNameSpinner();
        populateDateSpinner();
        filterData();
    }

    private void populateNameSpinner()
    {
        Spinner usernameSpinner = (Spinner) findViewById(R.id.usernameSpinner);
        ArrayList<String> usernames = new ArrayList<String>();
        usernames.add("Username");
        for(String username:mydb.getAllUsernames()){

            if(!usernames.contains(username))
                usernames.add(username);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usernameSpinner.setAdapter(adapter);
    }

    private void populateDateSpinner()
    {
        Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        ArrayList<String> dates = new ArrayList<String>();
        dates.add("Date");
        for(String date:mydb.getAllDates())
        {
            if(!dates.contains(date))
                dates.add(date);
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);
    }

    public void filterData(){
        Toast.makeText(this, "Filter data", Toast.LENGTH_SHORT).show();
        Spinner usernameSpinner = (Spinner) findViewById(R.id.usernameSpinner);
        if(usernameSpinner.getAdapter().getItem(0) != usernameSpinner.getSelectedItem()){
            //filter username results

        }
/*
        if(dateSpinner.getAdapter().getItem(0) != dateSpinner.getSelectedItem()){
            //filter date results
        }*/

    }

    public void showAccelerometerData(View view) {
        if(mydb.getAllAccelerometerRegists().size()!=0){
        ArrayList<String> array_list = new ArrayList<String>(mydb.getAllAccelerometerRegists());
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj.setAdapter(arrayAdapter);
        } else {
            obj.setAdapter(null);
        }


    }

    public void showGyroscopeData(View view) {

        obj.setAdapter(null);
        if(mydb.getAllGyroscopeRegists().size()!=0){
        ArrayList<String> array_list = new ArrayList<String>(mydb.getAllGyroscopeRegists());
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj.setAdapter(arrayAdapter);
        } else {
            obj.setAdapter(null);
        }
    }

    public void showOrientationData(View view) {

        if(mydb.getAllOrientationRegists().size()!=0) {
            ArrayList<String> array_list = new ArrayList<String>(mydb.getAllOrientationRegists());
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

            obj.setAdapter(arrayAdapter);
        } else {
            obj.setAdapter(null);
        }
    }

    public void showAllData(View view) {

        if(mydb.getAllRegists().size()!=0) {
            ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegists());
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

            obj.setAdapter(arrayAdapter);
        }
    }

    public void clearAll(View view){
        mydb.clearAllTables();
        showAccelerometerData(findViewById(R.id.btnAccelerometer));
    }

    public void exportCSV(View view) {
        //Accelerometer
        try {
            File directory = new File(FILE_DIRECTORY);
            directory.mkdirs();

            File myFile = new File(FILE_ACCELEROMETER);
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

            File myFile = new File(FILE_GYROSCOPE);
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

            File myFile = new File(FILE_ORIENTATION);
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
        //MOVE

        try {
            File directory = new File(FILE_DIRECTORY);
            directory.mkdirs();

            FILE_MOVE = "/sdcard/SportsMove/"+userName+"_"+moveName+"_"+getDate()+".csv";

            File myFile = new File(FILE_MOVE);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("MoveId; Timestamp; Accel_x; Accel_y; Accel_z; Gyro_x; Gyro_y; Gyro_z; Orient_x; Orient_y; Orient_z; Orient_w; CurrentArm; Reference\n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);

            if(mydb.getAllRegists().size()!=0){
                ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegists());

                for (String temp : array_list) {
                    temp = temp.replaceAll(" ", "; ");
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

    private String getDate() {
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy, HH:mm");
        return date = df.format(Calendar.getInstance().getTime());
    }
}
