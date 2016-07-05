package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sportsmove.db";

    public static final String TABLE_ACCELEROMETER_NAME = "accelerometer";
    public static final String TABLE_GYROSCOPE_NAME = "gyroscope";
    public static final String TABLE_ORIENTATION_NAME = "orientation";
    public static final String TABLE_COMMON_DATA_NAME = "common";
    public static final String TABLE_ALL_REGISTS = "allregists";

    private static final int DATABASE_VERSION = 1;

    public static final String ID = "_id";
    public static final String MOVEID = "moveid";
    public static final String Accel_X = "Accel_x";
    public static final String Accel_Y = "Accel_y";
    public static final String Accel_Z = "Accel_z";
    public static final String Gyro_X = "Gyro_x";
    public static final String Gyro_Y = "Gyro_y";
    public static final String Gyro_Z = "Gyro_z";
    public static final String Orient_X = "Orient_x";
    public static final String Orient_Y = "Orient_y";
    public static final String Orient_Z = "Orient_z";
    public static final String Orient_W = "Orient_w";
    public static final String TIMESTAMP = "timestamp";
    public static final String USERNAME = "username";
    public static final String MOVENAME = "movename";
    public static final String CURRENTARM = "currentarm";
    public static final String REFERENCE = "reference";
    public static final String COMMENT = "comment";

    private static final String DATABASE_CREATE_TABLE_ACCELEROMETER = "create table "
            + TABLE_ACCELEROMETER_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + Accel_X
            + " int, " + Accel_Y + " int, "
            + Accel_Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_CREATE_TABLE_GYROSCOPE = "create table "
            + TABLE_GYROSCOPE_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + Gyro_X
            + " int, " + Gyro_Y + " int, "
            + Gyro_Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_CREATE_TABLE_ORIENTATION = "create table "
            + TABLE_ORIENTATION_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + Orient_W + " int, " + Orient_X
            + " int, " + Orient_Y + " int, "
            + Orient_Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_CREATE_TABLE_COMMON_DATA = "create table "
            + TABLE_COMMON_DATA_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, "
            + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar, " +REFERENCE+ " boolean, " + COMMENT + "varchar );";

    private static final String DATABASE_CREATE_TABLE_ALL_REGISTS = "create table "
            + TABLE_ALL_REGISTS + "( "+ ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " varchar, "
            + Accel_X + " int, " + Accel_Y + " int, " + Accel_Z + " int, "
            + Gyro_X + " int, " + Gyro_Y + " int, "+ Gyro_Z + " int, "
            + Orient_W + " int, " + Orient_X + " int, " + Orient_Y + " int, " + Orient_Z+ " int, "
            + CURRENTARM + " varchar," + REFERENCE + " int, " + USERNAME + " varchar, " + MOVENAME + " varchar );";


    private static final String DATABASE_DELETE_ACCELEROMETER_REGISTS = "delete from "
            + TABLE_ACCELEROMETER_NAME;
    private static final String DATABASE_DELETE_GYROSCOPE_REGISTS = "delete from "
            + TABLE_GYROSCOPE_NAME;
    private static final String DATABASE_DELETE_ORIENTATION_REGISTS = "delete from "
            + TABLE_ORIENTATION_NAME;
    private static final String DATABASE_DELETE_COMMON_REGISTS = "delete from "
            + TABLE_COMMON_DATA_NAME;
    private static final String DATABASE_DELETE_ALL_REGISTS = "delete from "
            + TABLE_ALL_REGISTS;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_TABLE_ACCELEROMETER);
        db.execSQL(DATABASE_CREATE_TABLE_GYROSCOPE);
        db.execSQL(DATABASE_CREATE_TABLE_ORIENTATION);
        db.execSQL(DATABASE_CREATE_TABLE_COMMON_DATA);
        db.execSQL(DATABASE_CREATE_TABLE_ALL_REGISTS);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELEROMETER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYROSCOPE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIENTATION_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMON_DATA_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_REGISTS);
        onCreate(db);
    }

    public ArrayList<String> getAllAccelerometerRegists() {
        String[] columns = {ID, MOVEID, TIMESTAMP, Accel_X, Accel_Y, Accel_Z, CURRENTARM, USERNAME, MOVENAME };

        return getDataFromDatabase(false, TABLE_ACCELEROMETER_NAME, columns, null, null);
    }

    public ArrayList<String> getAllGyroscopeRegists() {
        String[] columns = {ID, MOVEID, TIMESTAMP, Gyro_X, Gyro_Y, Gyro_Z, CURRENTARM, USERNAME, MOVENAME };

        return getDataFromDatabase(false, TABLE_GYROSCOPE_NAME, columns, null, null);
    }

    public ArrayList<String> getAllOrientationRegists() {
        String[] columns = {ID, MOVEID, TIMESTAMP, Orient_W, Orient_X, Orient_Y, Orient_Z, CURRENTARM, USERNAME, MOVENAME };

        return getDataFromDatabase(false, TABLE_ORIENTATION_NAME, columns, null, null);
    }

    public ArrayList<String> getAllUsernames() {
        String[] columns = {USERNAME};
        return getDataFromDatabase(true, TABLE_ORIENTATION_NAME, columns, null, null);
    }

    public ArrayList<String> getAllDates() {
        String[] columns = {TIMESTAMP};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, null, null);
    }

    public ArrayList<String> getAllMovenames() {
        String[] columns = {MOVENAME};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, null, null);
    }


    public ArrayList<String> getAllRegists() {
        String[] columns = {MOVEID, TIMESTAMP, Accel_X, Accel_Y, Accel_Z, Gyro_X, Gyro_Y, Gyro_Z, Orient_W, Orient_X, Orient_Y, Orient_Z, CURRENTARM, REFERENCE, USERNAME, MOVENAME};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, null, null);
    }

    public ArrayList<String> getAllRegistsByUsername(String username) {
        String[] columns = {MOVEID, TIMESTAMP, Accel_X, Accel_Y, Accel_Z, Gyro_X, Gyro_Y, Gyro_Z, Orient_W, Orient_X, Orient_Y, Orient_Z, CURRENTARM, REFERENCE, USERNAME, MOVENAME};
        String[] usrName = {username};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, USERNAME+"=?", usrName);
    }

    public ArrayList<String> getAllRegistsByDate(String timestamp) {
        String[] columns = {MOVEID, TIMESTAMP, Accel_X, Accel_Y, Accel_Z, Gyro_X, Gyro_Y, Gyro_Z, Orient_W, Orient_X, Orient_Y, Orient_Z, CURRENTARM, REFERENCE, USERNAME, MOVENAME};
        String[] time = new String[] {timestamp};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, TIMESTAMP+"=?", time);
    }

    public ArrayList<String> getAllRegistsByMovename(String movename) {
        String[] columns = {MOVEID, TIMESTAMP, Accel_X, Accel_Y, Accel_Z, Gyro_X, Gyro_Y, Gyro_Z, Orient_W, Orient_X, Orient_Y, Orient_Z, CURRENTARM, REFERENCE, USERNAME, MOVENAME};
        String[] mvname = new String[] {movename};
        return getDataFromDatabase(false, TABLE_ALL_REGISTS, columns, MOVENAME+"=?", mvname);
    }

    public ArrayList<String> getAccelerometerMovename(String moveId) {
        String[] columns = {MOVENAME};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ MOVENAME + " FROM "+ TABLE_ACCELEROMETER_NAME+ " WHERE " + MOVEID + "="+moveId+";", null);

        cursor.moveToFirst();

        return getDataFromDatabase(false, TABLE_ACCELEROMETER_NAME, columns, null, null);
    }

    public ArrayList<String> getDataFromDatabase(boolean distinct, String tableName ,String[] columns,String whereColumn,String[] whereValues) {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(distinct,tableName,columns,whereColumn,whereValues, null, null,null,null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) //itera linhas
        {
            StringBuilder paragraph=new StringBuilder();

            for(int i=0; i<cursor.getColumnCount();i++) {
                paragraph.append(cursor.getString(i) + " ");
            }

            arrayList.add(paragraph.toString());
            cursor.moveToNext();
        }
        return arrayList;
    }

    public boolean insertAccelerometerRegister (String moveid, String timestamp, String accel_X, String accel_Y, String accel_Z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(Accel_X, accel_X);
        contentValues.put(Accel_Y, accel_Y);
        contentValues.put(Accel_Z, accel_Z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_ACCELEROMETER_NAME, null, contentValues);
        return true;
    }

    public boolean insertGyroscopeRegister (String moveid, String timestamp, String gyro_X, String gyro_Y, String gyro_Z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(Gyro_X, gyro_X);
        contentValues.put(Gyro_Y, gyro_Y);
        contentValues.put(Gyro_Z, gyro_Z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_GYROSCOPE_NAME, null, contentValues);
        return true;
    }

    public boolean insertOrientationRegister (String moveid, String timestamp, String orient_W, String orient_X, String orient_Y, String orient_Z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(Orient_W, orient_W);
        contentValues.put(Orient_X, orient_X);
        contentValues.put(Orient_Y, orient_Y);
        contentValues.put(Orient_Z, orient_Z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_ORIENTATION_NAME, null, contentValues);
        return true;
    }

    public boolean insertAllRegister (String moveid, String timestamp, String accel_X, String accel_Y, String accel_Z, String gyro_X, String gyro_Y, String gyro_Z, String orient_W, String orient_X, String orient_Y, String orient_Z, String currentArm, String reference, String username, String movename )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(Accel_X, accel_X);
        contentValues.put(Accel_Y, accel_Y);
        contentValues.put(Accel_Z, accel_Z);
        contentValues.put(Gyro_X, gyro_X);
        contentValues.put(Gyro_Y, gyro_Y);
        contentValues.put(Gyro_Z, gyro_Z);
        contentValues.put(Orient_W, orient_W);
        contentValues.put(Orient_X, orient_X);
        contentValues.put(Orient_Y, orient_Y);
        contentValues.put(Orient_Z, orient_Z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(REFERENCE, reference);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, movename);

        db.insert(TABLE_ALL_REGISTS, null, contentValues);
        return true;
    }
    public void clearAllTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DATABASE_DELETE_ACCELEROMETER_REGISTS);
        db.execSQL(DATABASE_DELETE_GYROSCOPE_REGISTS);
        db.execSQL(DATABASE_DELETE_ORIENTATION_REGISTS);
        db.execSQL(DATABASE_DELETE_ALL_REGISTS);
        db.close();
    }
}
