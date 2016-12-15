package com.butterfly.lab_06;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyDBClass myDBClass = new MyDBClass(this);
    SQLiteDatabase database;

    EditText idGroup_PK, idGroup_PK_new, head, idStudent, nameOfStudent, idGroup;
    Spinner faculty, course, nameOfGroup, idGroup_FK;
    Button save_PK, save_FK, find, delete, update_PK, update_FK, delete_FK, get_view;
    ListView view;

    TabHost tabHost;

    List<Integer> arrayOfIdGroup;

    ArrayAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("STUDGROUPS");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("STUDENTS");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("FIND & DELETE");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        database = myDBClass.getWritableDatabase();

        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, selectIDGroupPK());

        idGroup_PK = (EditText) findViewById(R.id.idGroup_PK);
        idGroup_PK_new = (EditText) findViewById(R.id.idGroup_PK_new);
        head = (EditText) findViewById(R.id.head);
        idStudent = (EditText) findViewById(R.id.idStudent);
        nameOfStudent = (EditText) findViewById(R.id.nameOfStudent);
        idGroup = (EditText) findViewById(R.id.idGroup);
        faculty = (Spinner) findViewById(R.id.faculty);
        course = (Spinner) findViewById(R.id.course);
        nameOfGroup = (Spinner) findViewById(R.id.nameOfGroup);
        idGroup_FK = (Spinner) findViewById(R.id.idGroup_FK);
        idGroup_FK.setAdapter(adapter);
        save_PK = (Button) findViewById(R.id.save_PK);
        save_FK = (Button) findViewById(R.id.save_FK);
        find = (Button) findViewById(R.id.find);
        delete = (Button) findViewById(R.id.delete);
        update_PK = (Button) findViewById(R.id.update_PK);
        update_FK = (Button) findViewById(R.id.update_FK);
        delete_FK = (Button) findViewById(R.id.delete_FK);
        get_view = (Button) findViewById(R.id.get_view);
        view = (ListView) findViewById(R.id.view);

        control();
    }

    public void control() {
        save_PK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues valuesStudGroup = new ContentValues();
                    if (idGroup_PK.getText().length() != 0)
                        valuesStudGroup.put("IDgroup", Integer.parseInt(idGroup_PK.getText().toString()));
                    valuesStudGroup.put("Faculty", faculty.getSelectedItem().toString());
                    valuesStudGroup.put("Course", Integer.parseInt(course.getSelectedItem().toString()));
                    valuesStudGroup.put("Name", nameOfGroup.getSelectedItem().toString());
                    valuesStudGroup.put("Head", head.getText().toString());
                    long rowStudGroup = database.insert("STUDGROUPS", null, valuesStudGroup);
                    showToast(String.valueOf(rowStudGroup));
                } catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
            }
        });

        save_FK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues valuesStudent = new ContentValues();
                    valuesStudent.put("IDgroup", Integer.parseInt(idGroup_FK.getSelectedItem().toString()));
                    valuesStudent.put("IDstudent", Integer.parseInt(idStudent.getText().toString()));
                    valuesStudent.put("Name", nameOfStudent.getText().toString());
                    long rowStudent = database.insertOrThrow("STUDENTS", null, valuesStudent);
                    showToast(String.valueOf(rowStudent));
                }
                catch (SQLiteConstraintException e) {
                    showToast("Нельзя добавить студентов больше 6!");
                }
                catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.rawQuery("SELECT * FROM STUDGROUPS WHERE IDgroup = " + Integer.parseInt(idGroup.getText().toString()), null);
                Cursor cursor1 = database.rawQuery("SELECT * FROM STUDENTS WHERE IDgroup = " + Integer.parseInt(idGroup.getText().toString()), null);

                ArrayList<UserListView> listViews = new ArrayList<UserListView>();
                if (cursor.moveToFirst()) {
                    do {
                        String item = "IDgroup = " + cursor.getInt(0) + "\n" +
                                "Faculty = " + cursor.getString(1) + "\n" +
                                "Course = " + cursor.getInt(2) + "\n" +
                                "Name = " + cursor.getString(3) + "\n" +
                                "Head = " + cursor.getString(4) + "\n";
                        if (cursor1.moveToFirst()) {
                            do {
                                String subitem = "\tIDstudent = " + cursor1.getInt(1) + "\n" +
                                        "\tName = " + cursor1.getString(2) + "\n";
                                listViews.add(new UserListView(item, subitem));
                            }
                            while (cursor1.moveToNext());
                        }
                        else
                            listViews.add(new UserListView(item, "В группе нет студентов!"));
                    }
                    while (cursor.moveToNext());
                }
                else {
                    listViews.add(new UserListView("Записи с таким IDgroup не существует!"));
                }
                UserListViewAdapter adapter = new UserListViewAdapter(MainActivity.this, R.layout.items, listViews);
                view.setAdapter(adapter);
                cursor.close();
                cursor1.close();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int del = database.delete("STUDGROUPS", "IDgroup = ?", new String[]{idGroup.getText().toString()});
                    //view.setText(String.format("Удалено %d строк", del));
                }
                catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
            }
        });

        update_PK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues values = new ContentValues();
                    values.put("IDgroup", Integer.parseInt(idGroup_PK_new.getText().toString()));
                    values.put("Faculty", faculty.getSelectedItem().toString());
                    values.put("Course", Integer.parseInt(course.getSelectedItem().toString()));
                    values.put("Name", nameOfGroup.getSelectedItem().toString());
                    values.put("Head", head.getText().toString());
                    int upd = database.update("STUDGROUPS", values, "IDgroup = ?", new String[] {idGroup_PK.getText().toString()} );
                    showToast(String.valueOf(upd));
                }
                catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
            }
        });

        update_FK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete_FK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int del = database.delete("STUDENTS", "IDgroup = ? AND IDstudent = ?",
                            new String[]{idGroup_FK.getSelectedItem().toString(), idStudent.getText().toString()});
                }
                catch (SQLiteConstraintException e) {
                    showToast("Нельзя удалить студентов, если их меньше 3!");
                }
                catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
            }
        });

        get_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                intent.putExtra("view", getView());
                startActivity(intent);
            }
        });
    }

    public String getView() {
        String string = "";
        Cursor cursor = database.rawQuery("SELECT * FROM VIEWLIST", null);
        if (cursor.moveToFirst()) {
            do {
                string += "Группа: " + cursor.getInt(0) + "\n" +
                        "\tСтароста: " + cursor.getString(1) + "\n" +
                        "\tКоличество студентов: " + cursor.getInt(2) + "\n\n";
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return string;
    }

    public List<Integer> selectIDGroupPK() {
        Cursor cursor = database.rawQuery("SELECT IDgroup FROM STUDGROUPS", null);
        arrayOfIdGroup = new ArrayList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                arrayOfIdGroup.add(cursor.getInt(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return arrayOfIdGroup;
    }

    public void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
