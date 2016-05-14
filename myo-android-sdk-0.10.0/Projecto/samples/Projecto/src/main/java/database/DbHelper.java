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


    private static final int DATABASE_VERSION = 1;

    public static final String ID = "_id";
    public static final String MOVEID = "moveid";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
    public static final String W = "w";
    public static final String TIMESTAMP = "timestamp";
    public static final String USERNAME = "username";
    public static final String MOVENAME = "movename";
    public static final String CURRENTARM = "currentarm";

    private static final String DATABASE_CREATE_TABLE_ACCELEROMETER = "create table "
            + TABLE_ACCELEROMETER_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + X
            + " int, " + Y + " int, "
            + Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_CREATE_TABLE_GYROSCOPE = "create table "
            + TABLE_GYROSCOPE_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + X
            + " int, " + Y + " int, "
            + Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_CREATE_TABLE_ORIENTATION = "create table "
            + TABLE_ORIENTATION_NAME + "( " + ID
            + " integer primary key autoincrement, " + MOVEID + " int, " + TIMESTAMP + " int, " + W + " int, " + X
            + " int, " + Y + " int, "
            + Z+" int, " + CURRENTARM + " varchar, " + USERNAME + " varchar, " + MOVENAME + " varchar );";

    private static final String DATABASE_DELETE_ACCELEROMETER_REGISTS = "delete from "
            + TABLE_ACCELEROMETER_NAME;
    private static final String DATABASE_DELETE_GYROSCOPE_REGISTS = "delete from "
            + TABLE_GYROSCOPE_NAME;
    private static final String DATABASE_DELETE_ORIENTATION_REGISTS = "delete from "
            + TABLE_ORIENTATION_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_TABLE_ACCELEROMETER);
        db.execSQL(DATABASE_CREATE_TABLE_GYROSCOPE);
        db.execSQL(DATABASE_CREATE_TABLE_ORIENTATION);

        insertData(db);
    }

    public void insertData(SQLiteDatabase db) {
        //db.execSQL("insert into " + TABLE_ACCELEROMETER_NAME + " values( 0, 0, 0, 0, 0, right, user, move);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELEROMETER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYROSCOPE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIENTATION_NAME);
        onCreate(db);
    }

    public ArrayList<String> getAllAccelerometerRegists() {

        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID, MOVEID, TIMESTAMP, X, Y, Z, CURRENTARM, USERNAME, MOVENAME };

        Cursor cursor = db.query(
                TABLE_ACCELEROMETER_NAME,  // The table to query
                columns, // The columns to return
                null,
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null                                     // don't filter by row groups
                //sortOrder                                 // The sort order
        );

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

    public ArrayList<String> getAllGyroscopeRegists() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID, MOVEID, TIMESTAMP, X, Y, Z, CURRENTARM, USERNAME, MOVENAME };

        Cursor cursor = db.query(
                TABLE_GYROSCOPE_NAME,  // The table to query
                columns, // The columns to return
                null,
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null                                     // don't filter by row groups
                //sortOrder                                 // The sort order
        );

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

    public ArrayList<String> getAllOrientationRegists() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID, MOVEID, TIMESTAMP, W, X, Y, Z, CURRENTARM, USERNAME, MOVENAME };

        Cursor cursor = db.query(
                TABLE_ORIENTATION_NAME,  // The table to query
                columns, // The columns to return
                null,
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null                                     // don't filter by row groups
                //sortOrder                                 // The sort order
        );

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

    public boolean insertAccelerometerRegister (String moveid, String timestamp, String x, String y, String z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(X, x);
        contentValues.put(Y, y);
        contentValues.put(Z, z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_ACCELEROMETER_NAME, null, contentValues);
        return true;
    }

    public boolean insertGyroscopeRegister (String moveid, String timestamp, String x, String y, String z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(X, x);
        contentValues.put(Y, y);
        contentValues.put(Z, z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_GYROSCOPE_NAME, null, contentValues);
        return true;
    }

    public boolean insertOrientationRegister (String moveid, String timestamp, String w, String x, String y, String z, String currentArm, String username, String moveName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVEID, moveid);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(X, x);
        contentValues.put(Y, y);
        contentValues.put(Z, z);
        contentValues.put(CURRENTARM, currentArm);
        contentValues.put(USERNAME, username);
        contentValues.put(MOVENAME, moveName);

        db.insert(TABLE_ORIENTATION_NAME, null, contentValues);
        return true;
    }

    public void clearAllTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DATABASE_DELETE_ACCELEROMETER_REGISTS);
        db.execSQL(DATABASE_DELETE_GYROSCOPE_REGISTS);
        db.execSQL(DATABASE_DELETE_ORIENTATION_REGISTS);
        db.close();
    }
}
