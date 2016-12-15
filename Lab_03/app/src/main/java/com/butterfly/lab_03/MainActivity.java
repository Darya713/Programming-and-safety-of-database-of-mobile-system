package com.butterfly.lab_03;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {

    RandomAccessFile randomAccessFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeToFile();
            }
        });
        final Button readButton = (Button) findViewById(R.id.buttonGetValue);
        readButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readFromFile();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void init() {
        checkIsFileExist("Lab3.txt");
        File file = new File(super.getFilesDir(), "Lab3.txt");
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            showToast("RAF file init success");
        } catch (IOException e) {
            showToast("RAF file init error");
        }
    }

    public boolean checkIsFileExist(String filename) {
        boolean result;
        File file = new File (super.getFilesDir(), filename);
        if (result = file.exists()) {
            showToast("File " + filename + " already exist");
        } else {
            showDialog(filename);
        }
        return result;
    }

    public void showDialog(final String filename) {
        final String fileName = filename;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("File is now creating " + fileName).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        showToast("File " + filename + " will be created now");
                        createFile(filename);
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void createFile(String filename) {
        File file = new File(super.getFilesDir(), filename);
        try {
            file.createNewFile();
            showToast("File " + filename + " is created");
            createEmptyTemplate();
        } catch (IOException e) {
            showToast("File " + filename + " is NOT created");
        }
    }

    // creating init template of RAF (200 symbols = 400 bytes in UTF-16)
    public void createEmptyTemplate() {
        String astericks = new String(new char[200]).replace('\0', '*');
        try {
            randomAccessFile.writeChars(astericks);
            showToast("RAF template creating success");
        } catch (IOException e) {
            showToast("RAF template creating error");
        }
    }


    public void writeCurrentFieldToFile(int pos, String key, String value) {
        try {
            randomAccessFile.seek(pos);
            randomAccessFile.writeChars(key);
            randomAccessFile.seek(pos + 10);
            randomAccessFile.writeChars(value);
            randomAccessFile.seek(pos + 30);
            randomAccessFile.writeChars("*****");
            showToast("New record add success");
        } catch (IOException e) {
            showToast(e.getMessage());
        }
    }

    public void writeToFile() {
        String key = ((EditText) findViewById(R.id.editText_inputKey)).getText().toString();
        String value = ((EditText) findViewById(R.id.editText_inputValue)).getText().toString();
        if (key.length() < 5) {
            StringBuilder bld = new StringBuilder(key);
            while (bld.length() != 5) {
                bld.append("*");
            }
            key = bld.toString();
        }
        if (value.length() < 10) {
            StringBuilder stb = new StringBuilder(value);
            while (stb.length() != 10) {
                stb.append("*");
            }
            value = stb.toString();
        }
        HashTable table = new HashTable(key, value);
        writeCurrentFieldToFile(calculateNewAddress(table.getCalculatedKey(), key, value), table.getKey(),
                table.getValue());
    }

    String tag = "rel";

    public int calculateNewAddress(int currentAddress, String key, String value) {
        try {
            if (tag.equals("rel")) {
                randomAccessFile.seek(40 * currentAddress);
            } else {
                randomAccessFile.seek(currentAddress);
            }
            byte [] currentField = new byte[40];
            randomAccessFile.read(currentField);
            String currentKeyFromFile = new String(currentField, "UTF-16")
                    .substring(0, 5).replaceAll("\\*", "");
            String currentValueFromFile = new String(currentField, "UTF-16")
                    .substring(5, 15).replaceAll("\\*", "");
            String currentRelFromFile = new String(currentField, "UTF-16")
                    .substring(15, 20).replaceAll("\\*", "");
            if (currentKeyFromFile.equals("")) {
                tag = "rel";
                return 40 * currentAddress;
            }
            else {
                StringBuilder stringBuilder = new StringBuilder(currentKeyFromFile);
                while (stringBuilder.length() != 5) {
                    stringBuilder.append('*');
                }
                currentKeyFromFile = stringBuilder.toString();
                if (currentKeyFromFile.equals(key)) {
                    return 40 * currentAddress;
                }
                if (currentValueFromFile.equals("")) {
                    tag = "rel";
                    return 40 * currentAddress;
                }
                else {

                    if (currentRelFromFile.equals("")) {
                        if (tag.equals("rel")) {
                            overrideAddress(40 * currentAddress, (int) randomAccessFile.length());
                        } else {
                            overrideAddress(currentAddress, (int) randomAccessFile.length());
                        }
                        return (int) randomAccessFile.length();
                    } else {
                        tag = "abs";
                        return calculateNewAddress(Integer.parseInt(currentRelFromFile), key, value);
                    }
                }
            }

        } catch (IOException e) {
            showToast(e.getMessage());
        }
        return 0;
    }

    public void overrideAddress(int oldAddress, int newAddress) {
        try {
            randomAccessFile.seek(oldAddress + 30);
            randomAccessFile.writeChars(Integer.valueOf(newAddress).toString());
        } catch (IOException e) {
            showToast(e.getMessage());
        }
        tag = "rel";
    }

    public void readFromFile() {
        String key = ((EditText) findViewById(R.id.editText_outputKey)).getText().toString();
        String currentKey = key;
        String value = "**********";
        if (key.length() < 5) {
            StringBuilder bld = new StringBuilder(key);
            while (bld.length() != 5) {
                bld.append("*");
            }
            key = bld.toString();
        }
        HashTable table = new HashTable(key, value);
        try {
            byte [] currentRow = getCurrentRow(getFinalPositionForRead(currentKey,
                    40 * table.getCalculatedKey()));
            String currentValue = new String(currentRow, "UTF-16")
                    .substring(5, 15).replaceAll("\\*", "");
            ((TextView) findViewById(R.id.textView_outputValue)).setText(currentValue);
        } catch (Exception e) {
            showToast("Nothing found");
        }
    }

    public int getFinalPositionForRead(String key, int currentPos) {
        try {
            randomAccessFile.seek(currentPos);
            byte [] currentField = new byte[40];
            randomAccessFile.read(currentField);
            String currentKey = new String(currentField, "UTF-16")
                    .substring(0, 5).replaceAll("\\*", "");
            String currentRelFromFile = new String(currentField, "UTF-16")
                    .substring(15, 20).replaceAll("\\*", "");
            if (currentKey.equals(key)) {
                return currentPos;
            } else {
                return getFinalPositionForRead(key, Integer.parseInt(currentRelFromFile));
            }
        } catch (IOException e) {
            showToast(e.getMessage());
        }
        return 0;
    }

    public byte [] getCurrentRow(int pos) {
        try {
            randomAccessFile.seek(pos);
            byte [] currentField = new byte[40];
            randomAccessFile.read(currentField);
            return currentField;
        } catch (IOException e) {
            showToast(e.getMessage());
        }
        return new byte[40];
    }


}
