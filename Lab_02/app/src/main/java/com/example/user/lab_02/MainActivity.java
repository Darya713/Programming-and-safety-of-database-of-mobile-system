package com.example.user.lab_02;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    String filename = "Base_Lab_02.txt";
    BufferedWriter bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ExistBase("Base_Lab_02.txt")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Создается файл Base_Lab_02.txt")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    });

            File f = new File(super.getFilesDir(), filename);
            try {
                f.createNewFile();
                Log.d("Log_02", "Файл " + filename + " создан");
            }
            catch (IOException e) {
                Log.d("Log_02", "Файл " + filename + " не создан");
            }

            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    private boolean ExistBase(String fname) {
        boolean rc = false;
        File file = new File(super.getFilesDir(), fname);
        if (rc = file.exists())
            Log.d("Log_02", "Файл " + fname + " существует");
        else
            Log.d("Log_02", "Файл " + fname + " не найден");
        return rc;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_enter:
                EditText lastName = (EditText) findViewById(R.id.editText_last_name);
                EditText firstName = (EditText) findViewById(R.id.editText_first_name);
                File f = new File(super.getFilesDir(), filename);
                try {
                    FileWriter fw = new FileWriter(f, true);
                    bw = new BufferedWriter(fw);
                } catch (IOException e) {
                    Log.d("Log_02", "Файл" + firstName + "не открыт " + e.getMessage());
                }
                WriteFile(lastName.getText().toString(), firstName.getText().toString());
                break;
            case R.id.button_print:
                ReadFile();
        }
    }

    private void WriteFile(String surname, String name) {
        String s = surname + "; " + name + "; " + "\r\n";
        try {
            FileOutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
            Log.d("Log_02", "Данные записаны");
        }
        catch (IOException e) {
            Log.d("Log_02", e.getMessage());
        }
    }

    private void ReadFile() {
        //File file = new File(super.getFilesDir(), filename);
        String str = "";
        FileInputStream stream = null;
        StringBuilder sb = new StringBuilder();
        try {
            stream = openFileInput(filename);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            }
            finally {
                stream.close();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(sb.toString())
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
