package com.kagera.knowledgepond;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME = "registeruser";
    public static final String Col1 = "ID";
    public static final String Col2 = "username";
    public static final String Col3 = "password";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //query for creating a table
        db.execSQL("CREATE TABLE registeruser  (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String userName, String userPassword)
    {
        //call for registration
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ContentValues = new ContentValues();
        ContentValues.put("username", userName);
        ContentValues.put("password", userPassword);
        long res = db.insert("registeruser", null,ContentValues);
        db.close();
        return res;
    }

    public boolean checkUserAndPassword(String userName, String userPassword)
    {
        String[] columns = {Col1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = Col2 + " =? " + " and " + Col3 + " =? ";
        String[] selectionArgs = {userName, userPassword};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
