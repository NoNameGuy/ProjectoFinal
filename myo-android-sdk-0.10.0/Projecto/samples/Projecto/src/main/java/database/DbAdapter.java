package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by UTIL on 22/04/2016.
 */
public class DbAdapter {
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    //private String[] allColumns = { DbHelper.ID, DbHelper.ACCELEROMETERX, DbHelper.ACCELEROMETERY,
    //        DbHelper.ACCELEROMETERZ, DbHelper.TIMESTAMP};

    public DbAdapter(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void createAccelerometerRegister(String x, String y, String z, String timestamp) {
      /*  ContentValues values = new ContentValues();
        values.put(dbHelper.ACCELEROMETERX, x);
        values.put(dbHelper.ACCELEROMETERY, y);
        values.put(dbHelper.ACCELEROMETERZ, z);
        values.put(dbHelper.TIMESTAMP, timestamp);

        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] photo = baos.toByteArray();
        values.put(DB.FOTO, photo);*/
        /*long insertId = database.insert(dbHelper.TABLE_ACCELEROMETER_NAME, null, values);
        // To show how to query
        Cursor cursor = database.query(dbHelper.TABLE_ACCELEROMETER_NAME, allColumns, dbHelper.ID + " = " +
                insertId, null, null, null, null);
        cursor.moveToFirst();*/
        //return cursorToContacto(cursor);
    }

    public void DeleteAccelerometerRegister (int idRegister){
        database.delete(DbHelper.TABLE_ACCELEROMETER_NAME, DbHelper.ID + " = " + idRegister,
                null);
    }

    public Cursor getAccelerometerRegister(){
        Cursor cursor = database.rawQuery("select _id, accelerometerx, accelerometery, accelerometerz, timestamp from "+ DbHelper.TABLE_ACCELEROMETER_NAME+";", null);
        return cursor;
    }

    public String getAccelerometerRegister (int idRegister){
       // Cursor cursor = database.query(DbHelper.TABLE_ACCELEROMETER_NAME, allColumns, DbHelper.ID + " = " +
                //idRegister, null, null, null, null);
      //  cursor.moveToFirst();
        //return cursorToContacto(cursor);
        //return cursor.toString();
        return null;
    }
}
