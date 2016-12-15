package com.butterfly.lab_05;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBClass extends SQLiteOpenHelper{

    public MyDBClass(Context context) {
        //activity, имя файла, фабрика БД, версия
        super(context, "Lab_05DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE lab_05 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "F REAL, " +
                "T TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST lab_05");
        db.execSQL("CREATE TABLE lab_05 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "F REAL, " +
                "T TEXT);");
    }
}
