package com.butterfly.lab_06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBClass extends SQLiteOpenHelper {

    public MyDBClass(Context context) {
        //activity, имя файла, фабрика БД, версия
        super(context, "STUDENTSDB ", null, 1);
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
                "Name TEXT, " +
                "FOREIGN KEY (IDgroup) REFERENCES STUDGROUPS(IDgroup) " +
                "ON DELETE CASCADE ON UPDATE CASCADE);");
        db.execSQL("DROP VIEW IF EXISTS VIEWLIST");
        db.execSQL("CREATE VIEW VIEWLIST " +
                "AS SELECT g.IDgroup, g.Head, COALESCE(s.col, 0) " +
                "FROM STUDGROUPS as g LEFT JOIN " +
                "(SELECT STUDENTS.IDgroup, Count(STUDENTS.Name) col FROM STUDENTS GROUP BY IDgroup) " +
                "AS s ON s.IDgroup = g.IDgroup");
        db.execSQL("CREATE TRIGGER STUDENTSTRIGGER " +
                "BEFORE INSERT ON STUDENTS " +
                "WHEN ((SELECT COUNT(STUDENTS.Name) FROM STUDENTS WHERE IDgroup = NEW.IDgroup) >= 6) " +
                "BEGIN " +
                "SELECT RAISE (ABORT, 'Error!!!'); " +
                "END");
        db.execSQL("CREATE TRIGGER DELETESTUDENTS " +
                "AFTER DELETE ON STUDENTS " +
                "BEGIN " +
                "SELECT " +
                "CASE " +
                "WHEN OLD.Name is null THEN RAISE(IGNORE) " +
                "WHEN ((SELECT COUNT(STUDENTS.Name) FROM STUDENTS WHERE IDgroup = OLD.IDgroup) < 3) " +
                "THEN RAISE (ABORT, 'Error!!!') " +
                "END;" +
                "END;");
        db.execSQL("CREATE TRIGGER DELETEGROUP " +
                "BEFORE DELETE ON STUDGROUPS " +
                "BEGIN " +
                "UPDATE STUDENTS set Name = null WHERE IDgroup = OLD.IDgroup; " +
                "end");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
