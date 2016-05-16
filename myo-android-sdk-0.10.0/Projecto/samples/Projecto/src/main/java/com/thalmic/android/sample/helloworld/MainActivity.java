package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.DbHelper;

public class MainActivity extends Activity {

    private TextView mLockStateView;
    private TextView mTextView;
    private TextView posX;
    private TextView posY;
    private TextView posZ;

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

    private final static String FILE_ORIENTATION = "/sdcard/orientation_data.csv";
    private final static String FILE_ACCELEROMETER = "/sdcard/accelerometer_data.csv";
    private final static String FILE_GYROSCOPE = "/sdcard/gyroscope_data.csv";

    private StringBuilder accelerometerTempData = new StringBuilder("");
    private StringBuilder gyroscopeTempData = new StringBuilder("");
    private StringBuilder orientationTempData = new StringBuilder("");

    private Arm currentArm;
    private String username;
    private String moveName;

    private Date initialTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private int moveId;

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
            mTextView.setTextColor(Color.CYAN);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.
            mTextView.setTextColor(Color.RED);
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            mTextView.setText(myo.getArm() == Arm.LEFT ? R.string.arm_left : R.string.arm_right);
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            mTextView.setText(R.string.sport_move);
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

            // Next, we apply a rotation to the text view using the roll, pitch, and yaw.
            mTextView.setRotation(roll);
            mTextView.setRotationX(pitch);
            mTextView.setRotationY(yaw);


            float dw = (float) rotation.w();
            float dx = (float) rotation.x();
            float dy = (float) rotation.y();
            float dz = (float) rotation.z();

            orientationW = dw;
            orientationX = dx;
            orientationY = dy;
            orientationZ = dz;

            TextView textW = (TextView) findViewById(R.id.orientationW);
            textW.setText(String.format("%.2f", orientationW));
            TextView textX = (TextView) findViewById(R.id.orientationX);
            textX.setText(String.format("%.2f", orientationX));
            TextView textY = (TextView) findViewById(R.id.orientationY);
            textY.setText(String.format("%.2f", orientationY));
            TextView textZ = (TextView) findViewById(R.id.orientationZ);
            textZ.setText(String.format("%.2f", orientationZ));

            orientationTimestamp = timestamp;
        }



        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            TextView textView = (TextView) findViewById(R.id.txtPose);
            TextView txtArm = (TextView) findViewById(R.id.txtArm);

            if(pose.equals(Pose.DOUBLE_TAP)) {
                Toast.makeText(getApplicationContext(), "Double tap, current arm: "+myo.getArm(), Toast.LENGTH_SHORT).show();
                currentArm = myo.getArm();
                txtArm.setText(myo.getArm().toString());
            }
            switch (pose) {
                case UNKNOWN:
                    mTextView.setText(getString(R.string.hello_world));
                    break;
                case REST:
                case DOUBLE_TAP:
                    int restTextId = R.string.hello_world;
                    switch (myo.getArm()) {
                        case LEFT:
                            textView.setText(getText(R.string.arm_left));
                            restTextId = R.string.arm_left;
                            break;
                        case RIGHT:
                            textView.setText(getText(R.string.arm_right));
                            restTextId = R.string.arm_right;
                            break;
                    }
                    mTextView.setText(getString(restTextId));
                    break;
                case FIST:
                    textView.setText(getText(R.string.pose_fist));
                    mTextView.setText(getString(R.string.pose_fist));
                    break;
                case WAVE_IN:
                    textView.setText(getText(R.string.pose_wavein));
                    mTextView.setText(getString(R.string.pose_wavein));
                    break;
                case WAVE_OUT:
                    textView.setText(getText(R.string.pose_waveout));
                    mTextView.setText(getString(R.string.pose_waveout));
                    break;
                case FINGERS_SPREAD:
                    textView.setText(getText(R.string.pose_fingersspread));
                    mTextView.setText(getString(R.string.pose_fingersspread));
                    break;
            }

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);

                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }

        @Override
        public void onAccelerometerData (Myo myo, long timestamp, Vector3 vector){

            float dx = (float) vector.x();
            float dy = (float) vector.y();
            float dz = (float) vector.z();

            accelX = dx;
            accelY = dy;
            accelZ = dz;

            //posY = vector.y();
            //posZ = vector.z();

            //setContentView(R.layout.activity_project);

            //TextView tv = (TextView) findViewById(R.id.accelDataX);
            TextView textX = (TextView) findViewById(R.id.accelValueX);
            textX.setText(String.format("%.2f", accelX));
            TextView textY = (TextView) findViewById(R.id.accelValueY);
            textY.setText(String.format("%.2f", accelY));
            TextView textZ = (TextView) findViewById(R.id.accelValueZ);
            textZ.setText(String.format("%.2f", accelZ));

            accelerometerTimestamp = timestamp;
            if(myo.getXDirection()==XDirection.TOWARD_WRIST){
                Toast.makeText(getApplicationContext(),"toward wrist", Toast.LENGTH_SHORT).show();
            }
            if(myo.getXDirection()==XDirection.TOWARD_ELBOW){
                Toast.makeText(getApplicationContext(),"toward elbow", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public  void onGyroscopeData (Myo myo, long timestamp, Vector3 gyro){

            float dx = (float) gyro.x();
            float dy = (float) gyro.y();
            float dz = (float) gyro.z();

            gyroX = dx;
            gyroY = dy;
            gyroZ = dz;

            TextView textX = (TextView) findViewById(R.id.gyroX);
            textX.setText(String.format("%.2f" ,gyroX));
            TextView textY = (TextView) findViewById(R.id.gyroY);
            textY.setText(String.format("%.2f", gyroY));
            TextView textZ = (TextView) findViewById(R.id.gyroZ);
            textZ.setText(String.format("%.2f", gyroZ));

            gyroscopeTimestamp = timestamp;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        moveId=0;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Username");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


        mLockStateView = (TextView) findViewById(R.id.lock_state);
        mTextView = (TextView) findViewById(R.id.text);

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
        Intent intent = new Intent(getApplicationContext(), OpenGLES20.class);
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
            //stopRecording();
            if(editText.getText().toString().equals("") || editText.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Please fill the move name field", Toast.LENGTH_SHORT).show();
            } else {
                moveId++;

                startRecording(editText.getText().toString().trim());
                btn.setText(getString(R.string.stopRecording));

                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }

        } else {
            stopRecording();
            //startRecording();
            btn.setText(getString(R.string.startRecording));
        }

    }

    private void startRecording(String moveName) {
        this.moveName = moveName;
        initialTime  = new Date();

        memoryHandler.postDelayed(new Runnable() {
            public void run() {
                writeDataToMemory();

                memoryHandler.postDelayed(this, dataCollectionDelay);
            }
        }, dataCollectionDelay);


    }

    private void stopRecording() {

        /*fileHandler.postDelayed(new Runnable() {
            public void run() {
                //writeDataToFile();
                writeDataToDatabase();
                fileHandler.postDelayed(this, dataWriteDelay);
            }
        }, dataWriteDelay);*/

        memoryHandler.removeCallbacksAndMessages(null);
        writeDataToDatabase();
    }

    private void writeDataToMemory() {

        accelerometerTempData.append(moveId+ "; " + sdf.format(initialTime.getTime()) + "; " + accelX + "; " + accelY + "; "+ accelZ + "; "+ currentArm + "; "+ username + "; "+ moveName + "\n");
        gyroscopeTempData.append(moveId+ "; " + gyroscopeTimestamp + "; " + gyroX + "; " + gyroY + "; " + gyroZ + "; " + currentArm + "; " + username + "; " + moveName + "\n");
        orientationTempData.append(moveId+ "; " + orientationTimestamp + "; " + orientationW + "; " + orientationX + "; " + orientationY + "; " + orientationZ + "; " + currentArm + "; " + username + "; " + moveName + "\n");
        writeDataToDatabase();
    }

    public void writeDataToDatabase() {

        DbHelper dbh = new DbHelper(getApplicationContext());

        dbh.insertAccelerometerRegister(String.valueOf(moveId), String.valueOf(accelerometerTimestamp), String.valueOf(accelX), String.valueOf(accelY), String.valueOf(accelZ), String.valueOf(currentArm), username, moveName);
        dbh.insertGyroscopeRegister(String.valueOf(moveId), String.valueOf(gyroscopeTimestamp), String.valueOf(gyroX), String.valueOf(gyroY), String.valueOf(gyroZ), String.valueOf(currentArm), username, moveName);
        dbh.insertOrientationRegister(String.valueOf(moveId), String.valueOf(orientationTimestamp), String.valueOf(orientationW), String.valueOf(orientationX), String.valueOf(orientationY), String.valueOf(orientationZ), String.valueOf(currentArm), username, moveName);

    }

    public void cubeActivity(View view) {
        Intent intent = new Intent();
        startActivity(intent);
    }

    public void writeDataToFileCsv(){

        //accelerometer data
        try {
            File myFile = new File(FILE_ACCELEROMETER);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; X; Y; Z; Current Arm; Username; Move Name; \n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);
            //fw.append(currentTime/1000 + "; " + accelX + "; " + accelY + "; " + accelZ + "\n");
            //fw.append(accelerometerTimestamp/1000 + "; " + accelX + "; " + accelY + "; " + accelZ + "\n");
            fw.append(accelerometerTempData);
            accelerometerTempData.delete(0, accelerometerTempData.length());
            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        //gyroscope data
        try {
            File myFile = new File(FILE_GYROSCOPE);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; X; Y; Z; Current Arm; Username; Move Name \n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);
            //fw.append(currentTime/1000 + "; " + gyroX + "; " + gyroY + "; " + gyroZ + "\n");
            fw.append(gyroscopeTempData);
            gyroscopeTempData.delete(0, gyroscopeTempData.length());
            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        //orientation data
        try {
            File myFile = new File(FILE_ORIENTATION);
            if( !myFile.exists() ) {
                myFile.createNewFile();
                FileWriter fw  = new FileWriter(myFile, true);
                fw.append("Time; W; X; Y; Z; Current Arm; Username; Move Name\n");
                fw.close();
            }
            FileWriter fw  = new FileWriter(myFile, true);
            //fw.append(currentTime/1000 + "; " + orientationW + "; " + orientationX + "; " + orientationY + "; " + orientationZ + "\n");
            fw.append(orientationTempData);
            orientationTempData.delete(0, orientationTempData.length());
            fw.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    //@Override
    private void onAccelerometerData (Myo myo, long timestamp, Vector3 vector){

        double dx = vector.x();

        String sX = Double.toString(dx);

        //posY = vector.y();
        //posZ = vector.z();

        //setContentView(R.layout.activity_project);

        //TextView tv = (TextView) findViewById(R.id.accelDataX);
        TextView tv = (TextView) findViewById(R.id.accelValueX);
        tv.setText(sX);

       //posY = (TextView) findViewById(R.id.accelDataY);
        //posZ = (TextView) findViewById(R.id.accelDataZ);

    }

}
