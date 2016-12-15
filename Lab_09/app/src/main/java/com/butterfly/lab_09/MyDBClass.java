package com.butterfly.lab_09;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBClass extends SQLiteOpenHelper {

    private Context context;

    public MyDBClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //activity, имя файла, фабрика БД, версия
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE STUDGROUPS (" +
                "IDgroup INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Faculty TEXT NOT NULL, " +
                "Course INTEGER NOT NULL, " +
                "Name TEXT NOT NULL, " +
                "Head TEXT NOT NULL);");
        db.execSQL("CREATE TABLE STUDENTS (" +
                "IDgroup INTEGER, " +
                "IDstudent INTEGER NOT NULL, " +
                "Name TEXT NOT NULL, " +
                "FOREIGN KEY (IDgroup) REFERENCES STUDGROUPS(IDgroup) " +
                "ON DELETE CASCADE ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
