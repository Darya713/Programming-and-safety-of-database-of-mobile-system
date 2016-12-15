package com.butterfly.lab_05;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ID, F, T;
    Button insert, select, selectRaw, update, delete;
    FloatingActionButton fab;

    MyDBClass myDBClass = new MyDBClass(this);
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID = (EditText) findViewById(R.id.editText_id);
        F = (EditText) findViewById(R.id.editText_f);
        T = (EditText) findViewById(R.id.editText_t);
        insert = (Button) findViewById(R.id.buttonInsert);
        select = (Button) findViewById(R.id.buttonSelect);
        selectRaw = (Button) findViewById(R.id.buttonSelectRaw);
        update = (Button) findViewById(R.id.buttonUpdate);
        delete = (Button) findViewById(R.id.buttonDelete);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        database = myDBClass.getWritableDatabase(); //получить БД

        control();
    }

    public void control() {
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (ID.getText().length() != 0)
                    values.put("ID", ID.getText().toString());
                values.put("F", F.getText().toString());
                values.put("T", T.getText().toString());
                long rowID = database.insert("lab_05", null, values);
                ID.setText("");
                F.setText("");
                T.setText("");
                showToast("Insert was successfully.\nRowID = " + rowID);
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.query("lab_05", new String[]{"ID", "F", "T"}, "ID = ?",
                        new String[] { ID.getText().toString() }, null, null, null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            F.setText(String.valueOf(cursor.getFloat(1)));
                            T.setText(String.valueOf(cursor.getString(2)));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    showToast("Select was successfully.");
                }
                else {
                    F.setText("");
                    T.setText("");
                    showToast("ID not found.");
                }
            }
        });
        selectRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.rawQuery("SELECT * FROM lab_05 WHERE ID = " + ID.getText().toString(), null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            F.setText(String.valueOf(cursor.getFloat(1)));
                            T.setText(String.valueOf(cursor.getString(2)));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    showToast("Select raw was successfully.");
                }
                else {
                    F.setText("");
                    T.setText("");
                    showToast("ID not found.");
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("F", F.getText().toString());
                values.put("T", T.getText().toString());
                if (database.update("lab_05", values, "ID = ?", new String[] { ID.getText().toString() }) == 1) {
                    showToast("Update was successfully.");
                    ID.setText("");
                    F.setText("");
                    T.setText("");
                }
                else {
                    showToast("Update was NOT successfully.");
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((database.delete("lab_05", "ID = ?", new String[]{ ID.getText().toString() })) == 1) {
                    showToast("Delete was successfully.");
                    ID.setText("");
                }
                else {
                    showToast("Delete was NOT successfully.");
                }
            }
        });
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
