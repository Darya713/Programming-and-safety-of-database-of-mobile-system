package com.butterfly.lab_05;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    SQLiteDatabase database;
    TextView textView;

    MyDBClass myDBClass = new MyDBClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = (TextView) findViewById(R.id.textView);
        database = myDBClass.getWritableDatabase();

        textView.setText(selectAll());
    }

    public String selectAll() {
        Cursor cursor = database.rawQuery("SELECT * FROM lab_05", null);
        String string = "";
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    string += "ID: " + String.valueOf(cursor.getInt(0)) +
                            ", F: " + String.valueOf(cursor.getFloat(1)) +
                            ", T: " + String.valueOf(cursor.getString(2)) + "\n";
                } while (cursor.moveToNext());
            }
        }
        else {
            string = "В таблице пока нет данных - она пуста.";
        }
        cursor.close();
        return string;
    }
}
