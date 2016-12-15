package com.butterfly.lab_09;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
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

    EditText idGroup_PK, idGroup_PK_new, head, idStudent, nameOfStudent, idGroup;
    Spinner faculty, course, nameOfGroup, idGroup_FK;
    Button save_PK, save_FK, find, delete, update_PK, update_FK, get_view;
    ListView view;
    TabHost tabHost;
    List<Integer> arrayOfIdGroup;
    ArrayAdapter<Integer> adapter;

    public static final String AUTHORITY = "com.example.butterfly.contentprovider.StudentsProvider";

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
        get_view = (Button) findViewById(R.id.get_view);
        view = (ListView) findViewById(R.id.view);

        control();
    }

    public void control() {
        save_PK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues valuesStudGroup = new ContentValues();
                if (idGroup_PK.getText().length() != 0)
                    valuesStudGroup.put("IDgroup", Integer.parseInt(idGroup_PK.getText().toString()));
                valuesStudGroup.put("Faculty", faculty.getSelectedItem().toString());
                valuesStudGroup.put("Course", Integer.parseInt(course.getSelectedItem().toString()));
                valuesStudGroup.put("Name", nameOfGroup.getSelectedItem().toString());
                valuesStudGroup.put("Head", head.getText().toString());
                try {
                    Uri uri = Uri.parse("content://" + AUTHORITY + "/GroupsList");
                    Uri rowid = getContentResolver().insert(uri, valuesStudGroup);
                    showToast("Insert " + rowid);
                } catch (SQLiteConstraintException e) {
                    Log.d("LAB6", e.getMessage());
                }
            }
        });

        save_FK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues valuesStudent = new ContentValues();
                valuesStudent.put("IDgroup", Integer.parseInt(idGroup_FK.getSelectedItem().toString()));
                valuesStudent.put("IDstudent", Integer.parseInt(idStudent.getText().toString()));
                valuesStudent.put("Name", nameOfStudent.getText().toString());
                try {
                    Uri uri = Uri.parse("content://" + AUTHORITY + "/StudentsList/g/" +
                            valuesStudent.get("IDgroup"));
                    Uri rowStudent = getContentResolver().insert(uri, valuesStudent);
                    showToast("Insert " + rowStudent);
                } catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://" + AUTHORITY + "/StudentsList/" +
                        Integer.parseInt(idGroup.getText().toString()));
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                ArrayList<UserListView> listViews = new ArrayList<UserListView>();
                if (cursor.moveToFirst()) {
                    do {
                        String item = "IDgroup = " + cursor.getInt(0) + "\n" +
                                "Faculty = " + cursor.getString(1) + "\n" +
                                "Course = " + cursor.getInt(2) + "\n" +
                                "Name = " + cursor.getString(3) + "\n" +
                                "Head = " + cursor.getString(4) + "\n";
                        if (cursor.moveToFirst() && cursor.getInt(5) != 0 && cursor.getString(6) != null) {
                            do {
                                String subitem = "\tIDstudent = " + cursor.getInt(5) + "\n" +
                                        "\tName = " + cursor.getString(6) + "\n";
                                listViews.add(new UserListView(item, subitem));
                            }
                            while (cursor.moveToNext());
                        }
                        else
                            listViews.add(new UserListView(item, "В группе нет студентов"));
                    }
                    while (cursor.moveToNext());
                }
                else {
                    listViews.add(new UserListView("Записи с таким IDgroup не существует!"));
                }
                UserListViewAdapter adapter = new UserListViewAdapter(MainActivity.this, R.layout.items, listViews);
                view.setAdapter(adapter);
                cursor.close();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("content://" + AUTHORITY + "/GroupsList/g/" +
                            idGroup.getText().toString());
                    int delete = getContentResolver().delete(uri, null, null);
                    showToast("Delete " + delete);
                } catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
            }
        });

        update_PK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put("IDgroup", Integer.parseInt(idGroup_PK_new.getText().toString()));
                values.put("Faculty", faculty.getSelectedItem().toString());
                values.put("Course", Integer.parseInt(course.getSelectedItem().toString()));
                values.put("Name", nameOfGroup.getSelectedItem().toString());
                values.put("Head", head.getText().toString());
                try {
                    Uri uri = Uri.parse("content://" + AUTHORITY + "/GroupsList/g/" +
                            idGroup_PK.getText().toString());
                    int update = getContentResolver().update(uri, values, null, null);
                    showToast("Update " + update);
                } catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
            }
        });

        update_FK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("content://" + AUTHORITY + "/StudentsList/g/s/" +
                            idStudent.getText().toString());
                    int delete = getContentResolver().delete(uri, null, null);
                    showToast("Delete " + delete);
                } catch (Exception e) {
                    Log.d("LAB6", e.getMessage());
                }
                adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, selectIDGroupPK());
                idGroup_FK.setAdapter(adapter);
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
        Uri uri = Uri.parse("content://" + AUTHORITY + "/GroupsList");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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
        Uri uri =  Uri.parse("content://" + AUTHORITY + "/StudentsList");
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        arrayOfIdGroup = new ArrayList<Integer>();
        if (cursor != null && cursor.moveToFirst()) {
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
