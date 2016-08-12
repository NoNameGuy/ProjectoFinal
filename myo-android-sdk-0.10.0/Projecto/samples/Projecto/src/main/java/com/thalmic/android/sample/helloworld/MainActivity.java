package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.android.sample.helloworld.auxiliary.AccelerometerRecord;
import com.thalmic.android.sample.helloworld.auxiliary.Constants;
import com.thalmic.android.sample.helloworld.auxiliary.GyroscopeRecord;
import com.thalmic.android.sample.helloworld.auxiliary.OpenGLRenderer;
import com.thalmic.android.sample.helloworld.auxiliary.OrientationRecord;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import database.DbHelper;

public class MainActivity extends Activity {

    private TextView mLockStateView;
    private TextView posX;
    private TextView posY;
    private TextView posZ;
    private EditText txtUsername;
    private EditText txtMovename;


    private float accelX;
    private float accelY;
    private float accelZ;
    private float gyroX;
    private float gyroY;
    private float gyroZ;
    private float orientationW;
    private float orientationX;
    private float orientationY;
    private float orientationZ;

    private final Handler memoryHandler = new Handler();
    private final Handler fileHandler = new Handler();
    private final int dataCollectionDelay = 1000; //milliseconds
    private final int dataWriteDelay = 3000; //milliseconds
    private long accelerometerTimestamp;
    private long gyroscopeTimestamp;
    private long orientationTimestamp;
    private long startRecordingTimestamp;
    
    private Arm currentArm;
    private String username;
    private String moveName;

    private int reference;
    private int moveId;


    private OpenGLRenderer renderer;
    private GLSurfaceView glView;

    private boolean recording;
    private StringBuilder accelerometerTempData = new StringBuilder();
    private StringBuilder gyroscopeTempData = new StringBuilder();
    private StringBuilder orientationTempData = new StringBuilder();
    private ArrayList<AccelerometerRecord> accelerometerArrayList = new ArrayList<AccelerometerRecord>();
    private ArrayList<GyroscopeRecord> gyroscopeArrayList = new ArrayList<GyroscopeRecord>();
    private ArrayList<OrientationRecord> orientationArrayList = new ArrayList<OrientationRecord>();


    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {

        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            if(!myo.isUnlocked()) {
                myo.unlock(Myo.UnlockType.HOLD);
                Toast.makeText(getApplicationContext(), "unlocked", Toast.LENGTH_SHORT).show();
            }
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            mLockStateView.setText(R.string.unlocked);
        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            mLockStateView.setText(R.string.locked);
        }

        @Override
        public void onAccelerometerData (Myo myo, long timestamp, Vector3 vector){

            float dx = (float) vector.x();
            float dy = (float) vector.y();
            float dz = (float) vector.z();

            if(recording) {
                writeAccelerometerDataToMemory(timestamp, vector.x(), vector.y(), vector.z());
            }

            accelX = dx;
            accelY = dy;
            accelZ = dz;

            accelerometerArrayList.add(new AccelerometerRecord(startRecordingTimestamp, accelX, accelY, accelZ));

            accelerometerTimestamp = timestamp;

        }

        @Override
        public  void onGyroscopeData (Myo myo, long timestamp, Vector3 gyro){

            float dx = (float) gyro.x();
            float dy = (float) gyro.y();
            float dz = (float) gyro.z();

            if(recording) {
                writeGyroscopeDataToMemory(timestamp, gyro.x(), gyro.y(), gyro.z());
            }

            gyroX = dx;
            gyroY = dy;
            gyroZ = dz;


            gyroscopeArrayList.add(new GyroscopeRecord(startRecordingTimestamp, gyroX, gyroY, gyroZ));

            gyroscopeTimestamp = timestamp;
        }

        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, final long timestamp, Quaternion rotation) {
            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }

            if(recording) {
                writeOrientationDataToMemory(timestamp, rotation.w(), rotation.x(), rotation.y(), rotation.z());
            }

            float dw = (float) rotation.w();
            float dx = (float) rotation.x();
            float dy = (float) rotation.y();
            float dz = (float) rotation.z();

            orientationW = dw;
            orientationX = dx;
            orientationY = dy;
            orientationZ = dz;

            orientationTimestamp = timestamp;

            orientationArrayList.add(new OrientationRecord(startRecordingTimestamp, orientationW, orientationX, orientationY, orientationZ));

            renderer.setOrientation(dw, dx, dy, dz);
            glView.requestRender();

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        txtMovename = (EditText) findViewById(R.id.movename);
        txtUsername = (EditText) findViewById(R.id.username);


        moveId=0;


        mLockStateView = (TextView) findViewById(R.id.lock_state);

        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);

        this.glView = (GLSurfaceView) findViewById(R.id.surfaceView);
        this.renderer = new OpenGLRenderer();
        glView.setRenderer(this.renderer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);

        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            Hub.getInstance().shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_scan == id) {
            onScanActionSelected();
            return true;
        }
        if (R.id.action_view_data == id) {
            viewData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewData() {
        Intent intent = new Intent(getApplicationContext(), ViewData.class);
        intent.putExtra(Constants.MOVENAME, moveName );
        intent.putExtra(Constants.USERNAME, username );
        startActivity(intent);
    }

    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void toggleRecording(View view) {
        Button btn = (Button) findViewById(R.id.btnRecord);
        EditText editText = (EditText) findViewById(R.id.txtMovename);
        if( btn.getText().equals(getString(R.string.startRecording))) {
            if(editText.getText().toString().equals("") || editText.getText().toString().equals(" ") || txtMovename.getText().toString().equals("") || txtMovename.getText().toString().equals(" ") || txtUsername.getText().toString().equals("") || txtUsername.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Please fill empty fields", Toast.LENGTH_SHORT).show();
            } else {
                moveId++;

                startRecording(editText.getText().toString().trim());
                btn.setText(getString(R.string.stopRecording));

                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                recording = true;
                startRecordingTimestamp = accelerometerTimestamp;
            }

        } else {
            stopRecording();
            //startRecording();
            btn.setText(getString(R.string.startRecording));
            recording = false;
        }

    }

    private void startRecording(String moveName) {
        this.moveName = moveName;

        CheckBox checkBox = (CheckBox) findViewById(R.id.referenceMove);
        if(checkBox!=null && checkBox.isChecked()==true)
            reference = 1;
        else
            reference = 0;

        memoryHandler.postDelayed(new Runnable() {
            public void run() {
                writeDataToDatabase();

                memoryHandler.postDelayed(this, dataCollectionDelay);
            }
        }, dataCollectionDelay);


    }

    private void stopRecording() {

        memoryHandler.removeCallbacksAndMessages(null);
        writeDataToDatabase();
    }

    public void writeAccelerometerDataToMemory( long timestamp, double x, double y, double z) {
        //accelerometerTempData.append(String.valueOf(timestamp) + "; " + String.valueOf(x) + "; " + String.valueOf(y) + "; " + String.valueOf(z));

        ArrayList<String> mylist = new ArrayList<String>();
        mylist.add(String.valueOf(x));
        mylist.add(String.valueOf(y));
        mylist.add(String.valueOf(z));

        //writeDataToDatabase(String.valueOf(timestamp), String.valueOf(x), String.valueOf(y), String.valueOf(z), "", "", "", "", "", "", "", "", "");
    }

    public void writeGyroscopeDataToMemory( long timestamp, double x, double y, double z) {
        gyroscopeTempData.append(String.valueOf(timestamp) + "; " + String.valueOf(x) + "; " + String.valueOf(y) + "; " + String.valueOf(z));
        //writeDataToDatabase("", "", "", "", "", "", "", "", "", String.valueOf(timestamp), String.valueOf(x), String.valueOf(y), String.valueOf(z));
    }

    public void writeOrientationDataToMemory( long timestamp, double w, double x, double y, double z) {
        orientationTempData.append(String.valueOf(timestamp) + "; " + String.valueOf(w) + "; " + String.valueOf(x) + "; " + String.valueOf(y) + "; " + String.valueOf(z));
        //writeDataToDatabase("", "", "", "", String.valueOf(timestamp), String.valueOf(w), String.valueOf(x), String.valueOf(y), String.valueOf(z), "", "", "", "");
    }

    private void writeDataToMemory() {

        /*accelerometerTempData.append(moveId+ "; " + new Date(accelerometerTimestamp).toString() + "; " + accelX + "; " + accelY + "; "+ accelZ + "; "+ currentArm + "; "+ username + "; "+ moveName + "\n");
        gyroscopeTempData.append(moveId+ "; " + gyroscopeTimestamp + "; " + gyroX + "; " + gyroY + "; " + gyroZ + "; " + currentArm + "; " + username + "; " + moveName + "\n");
        orientationTempData.append(moveId+ "; " + orientationTimestamp + "; " + orientationW + "; " + orientationX + "; " + orientationY + "; " + orientationZ + "; " + currentArm + "; " + username + "; " + moveName + "\n");
        moveTempData.append(moveId +"; " + accelX +"; "+ accelY +"; "+ accelZ +"; "+ gyroX + "; " + gyroY + "; " + gyroZ + "; " + orientationW + "; " + orientationX + "; " + orientationY + "; " + orientationZ + "; " + currentArm + "; " + reference + "\n");
        */

        StringBuilder regist = new StringBuilder();

        /*regist.append("\n");
        regist.append(accelerometerTempData);
        regist.append(";");
        regist.append(orientationTempData);
        regist.append(";");
        regist.append(gyroscopeTempData);*/

        /*
        accelerometerTempData.delete(0, accelerometerTempData.length());
        orientationTempData.delete(0, orientationTempData.length());
        gyroscopeTempData.delete(0, gyroscopeTempData.length());*/


    }

    public void writeDataToDatabase(){//(String atimestamp, String  ax, String  ay, String  az, String otimestamp, String  ow, String  ox, String  oy, String  oz, String  gtimestamp, String gx, String gy, String gz) {

        DbHelper dbh = new DbHelper(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

        dbh.insertAccelerometerRegister(String.valueOf(moveId), String.valueOf(new SimpleDateFormat("HH:mm:ss:SSS").format(accelerometerTimestamp)), String.valueOf(accelX), String.valueOf(accelY), String.valueOf(accelZ), String.valueOf(currentArm), username, moveName);
        dbh.insertGyroscopeRegister(String.valueOf(moveId), String.valueOf(gyroscopeTimestamp), String.valueOf(gyroX), String.valueOf(gyroY), String.valueOf(gyroZ), String.valueOf(currentArm), username, moveName);
        dbh.insertOrientationRegister(String.valueOf(moveId), String.valueOf(orientationTimestamp), String.valueOf(orientationW), String.valueOf(orientationX), String.valueOf(orientationY), String.valueOf(orientationZ), String.valueOf(currentArm), username, moveName);
        /*dbh.insertAllRegister(String.valueOf(moveId), String.valueOf(DateFormat.format("dd-MM-yyyy_hh:mm:ss.SSS", accelerometerTimestamp)), String.valueOf(accelX), String.valueOf(accelY), String.valueOf(accelZ), "", "", "", "", "", "", "", String.valueOf(currentArm), String.valueOf(reference), username, moveName);
        dbh.insertAllRegister(String.valueOf(moveId), String.valueOf(DateFormat.format("dd-MM-yyyy_hh:mm:ss.SSS", orientationTimestamp)), "", "", "", "", "", "", String.valueOf(orientationW), String.valueOf(orientationX), String.valueOf(orientationY), String.valueOf(orientationZ), String.valueOf(currentArm), String.valueOf(reference), username, moveName);
        dbh.insertAllRegister(String.valueOf(moveId), String.valueOf(DateFormat.format("dd-MM-yyyy_hh:mm:ss:SSS", gyroscopeTimestamp)), "", "", "", String.valueOf(gyroX), String.valueOf(gyroY), String.valueOf(gyroZ), "", "", "", "", String.valueOf(currentArm), String.valueOf(reference), username, moveName);
        */
       // dbh.insertAllRegister(String.valueOf(moveId), atimestamp, ax, ay, az, gtimestamp, gx, gy, gz, otimestamp, ow, ox, oy, oz, String.valueOf(currentArm), String.valueOf(reference), username, moveName);
        for(AccelerometerRecord accelerometerRegist:accelerometerArrayList)
        for(GyroscopeRecord gyroscopeRegist:gyroscopeArrayList)
        for(OrientationRecord orientationRegist:orientationArrayList)
        dbh.insertAllRegister(String.valueOf(moveId), String.valueOf(accelerometerRegist.getTimestamp()),String.valueOf(accelerometerRegist.getX()),String.valueOf(accelerometerRegist.getY()),String.valueOf(accelerometerRegist.getZ()),String.valueOf(gyroscopeRegist.getTimestamp()),String.valueOf(gyroscopeRegist.getX()),String.valueOf(gyroscopeRegist.getY()),String.valueOf(gyroscopeRegist.getZ()),String.valueOf(orientationRegist.getTimestamp()),String.valueOf(orientationRegist.getW()),String.valueOf(orientationRegist.getX()),String.valueOf(orientationRegist.getY()),String.valueOf(orientationRegist.getZ()),String.valueOf(currentArm),String.valueOf(reference),String.valueOf(username), String.valueOf(moveName));
    }


}
