package com.f20.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableRow;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    // Using constants for column names
    public static final String DATABASE_NAME = "PhoneBookDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "folder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_LNAME = "lname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table for Folder
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER NOT NULL CONSTRAINT folder_pk PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FNAME + " varchar(200) NOT NULL," +
                COLUMN_LNAME + " varchar(200) NOT NULL," +
                COLUMN_PHONE + " int NOT NULL," +
                COLUMN_ADDRESS + " varchar(200) NOT NULL);";
        Log.i(TAG, "QUERY: " + sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop the table and recreate it
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME+ ";";
        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);
    }

    public boolean addContact(Contact contact) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FNAME, contact.getFname());
        cv.put(COLUMN_LNAME, contact.getLname());
        cv.put(COLUMN_PHONE, contact.getPhone());
        cv.put(COLUMN_ADDRESS, contact.getAddress());
        return sqLiteDatabase.insert(TABLE_NAME, null, cv) != -1;
    }

    public boolean deleteContact(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateContact(int id, Contact contact) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FNAME, contact.getFname());
        cv.put(COLUMN_LNAME, contact.getLname());
        cv.put(COLUMN_PHONE, contact.getPhone());
        cv.put(COLUMN_ADDRESS, contact.getAddress());
        return sqLiteDatabase.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getAllContact() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public Cursor getContact(int nid) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "
                + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(nid)});
    }
}
