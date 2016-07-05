package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private String moveName;
    private String userName;
    private String date;

    private ListView obj;
    private DbHelper mydb;

    private Spinner dateSpinner;
    protected Spinner usernameSpinner;
    protected Spinner movenameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        mydb = new DbHelper(this);
        obj = (ListView)findViewById(R.id.listView);

        showAccelerometerData(findViewById(R.id.btnAccelerometer));
        moveName=(String) getIntent().getExtras().get(Constants.MOVENAME);
        userName=(String) getIntent().getExtras().get(Constants.USERNAME);

        usernameSpinner = (Spinner) findViewById(R.id.usernameSpinner);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        movenameSpinner = (Spinner) findViewById(R.id.movenameSpinner);

        populateNameSpinner();
        populateDateSpinner();
        populateMovenameSpinner();
        filterData();
    }

    private void populateNameSpinner()
    {
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

    private void populateMovenameSpinner()
    {
        ArrayList<String> moves = new ArrayList<String>();
        moves.add("Movename");
        for(String move:mydb.getAllMovenames())
        {
            if(!moves.contains(move))
                moves.add(move);
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, moves);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movenameSpinner.setAdapter(adapter);
    }

    public void filterData(){

        usernameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Filter data", Toast.LENGTH_SHORT).show();

                String username = usernameSpinner.getItemAtPosition(position).toString().trim();

                if(mydb.getAllRegistsByUsername(username).size()==0 || usernameSpinner.getAdapter().getItem(0).toString().equals(usernameSpinner.getSelectedItem().toString())){
                    //preenche os dados todos
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegists());
                    fillList(array_list);
                } else {
                    //preenche os dados selecionados
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegistsByUsername(username));
                    fillList(array_list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String date = dateSpinner.getItemAtPosition(position).toString().trim();

                if(mydb.getAllRegistsByDate(date).size()==0 || dateSpinner.getAdapter().getItem(0).toString().equals(dateSpinner.getSelectedItem().toString())){
                    //preenche os dados todos
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegists());
                    fillList(array_list);
                } else {
                    //preenche os dados selecionados
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegistsByDate(date));
                    fillList(array_list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        movenameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String move = movenameSpinner.getItemAtPosition(position).toString().trim();
                if (mydb.getAllRegistsByMovename(move).size() == 0 || movenameSpinner.getAdapter().getItem(0).toString().equals(movenameSpinner.getSelectedItem().toString())) {
                    //preenche os dados todos
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegists());
                    fillList(array_list);
                } else {
                    //preenche os dados selecionados
                    ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegistsByMovename(move));
                    fillList(array_list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillList(ArrayList<String> array_list) {
        //ArrayList<String> array_list = new ArrayList<String>(mydb.getAllRegistsByMovename(move));
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, array_list);

        obj.setAdapter(arrayAdapter);
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

    public void promptExportCSV(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        input.setText(userName+"_"+moveName+"_"+getDate());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportCSV(input.getText().toString().trim());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void exportCSV(String filename) {
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

            FILE_MOVE = "/sdcard/SportsMove/"+filename+".csv";

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
