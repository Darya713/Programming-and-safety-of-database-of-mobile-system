package com.butterfly.lab_04;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CalendarView calendar;
    EditText taskField;
    Button addButton, editButton, removeButton;
    Gson gson;
    String gsonObjectString, currentFileName;
    Calendar c;

    int currentDate, currentMonth, currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = (CalendarView) findViewById(R.id.calendar);
        taskField = (EditText) findViewById(R.id.taskField);
        addButton = (Button) findViewById(R.id.addButton);
        editButton = (Button) findViewById(R.id.editButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        gson = new Gson();
        currentFileName = "Lab_04.json";
        c = Calendar.getInstance();
        if (checkIsFileExist(currentFileName)) {
            readJsonStringFromFile(currentFileName);
            new Tasks().setListOfTasks(getJsonContent()); //создание массива
        } else {
            showDialog(currentFileName);
        }
        currentDate = c.get(Calendar.DAY_OF_MONTH);
        currentMonth = c.get(Calendar.MONTH);
        currentYear = c.get(Calendar.YEAR);
        check(currentDate, currentMonth, currentYear);
        controlCalendar();
    }

    public void controlCalendar() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewTask(currentDate, currentMonth, currentYear);
                showToast("Add success");
                check(currentDate, currentMonth, currentYear);
                writeJsonStringToFile(currentFileName);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeTaskForToday(currentDate, currentMonth, currentYear);
                createNewTask(currentDate, currentMonth, currentYear);
                showToast("Edit success");
                check(currentDate, currentMonth, currentYear);
                writeJsonStringToFile(currentFileName);
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeTaskForToday(currentDate, currentMonth, currentYear);
                showToast("Remove success");
                check(currentDate, currentMonth, currentYear);
                writeJsonStringToFile(currentFileName);
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                currentDate = dayOfMonth;
                currentMonth = month;
                currentYear = year;
                check(currentDate, currentMonth, currentYear);
            }
        });
    }

    public void createNewTask(int date, int month, int year) {
        Task task = new Task(taskField.getText().toString(), date, month, year);
        Tasks.add(task); //добавление заметки в список заметок
    }

    public void removeTaskForToday(int date, int month, int year) {
        try {
            Task task = Tasks.getTaskForToday(date, month, year);
            Tasks.remove(task);
        }
        catch (NullPointerException e) {
            showToast("Error while removing.");
        }
    }

    //проверка даты на наличие заметки
    public void check(int date, int month, int year) {
        taskField.setText("");
        if (!Tasks.isTaskForToday(date, month, year)) {
            addButton.setEnabled(true);
            editButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
        else {
            taskField.setText(Tasks.getTaskForTodayString(date, month, year));
            addButton.setEnabled(false);
            editButton.setEnabled(true);
            removeButton.setEnabled(true);
        }
    }

    //запись в json-файл
    public String setJsonContent() {
        return gson.toJson(Tasks.getListOfTasks());
    }

    //выгрузка заметок из json-файла
    public ArrayList<Task> getJsonContent() {
        return gson.fromJson(gsonObjectString, new TypeToken<ArrayList<Task>>() {}.getType());
    }

    public void writeJsonStringToFile(String filename) {
        try {
            Context context = getApplicationContext(); //предоставляет доступ к базовым функциям приложения: к файловой системе
            OutputStreamWriter outputStreamWriter = new
                    OutputStreamWriter(context.openFileOutput(filename, context.MODE_PRIVATE));
            outputStreamWriter.write(setJsonContent());
            outputStreamWriter.close();
            showToast("JSON to file write success.");
        }
        catch (IOException e) {
            showToast("JSON write error.");
        }
    }

    public void readJsonStringFromFile(String filename) {
        try {
            Context context = getApplicationContext();
            FileInputStream fileInputStream = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            gsonObjectString = stringBuilder.toString();
            showToast("JSON from file read success.");
        }
        catch (IOException e) {
            showDialog("JSON read error.");
        }
    }

    public boolean checkIsFileExist(String filename) {
        boolean result;
        File file = new File(super.getFilesDir(), filename);
        if (result = file.exists()) {
            showToast("File " + filename + " already exist.");
        }
        else {
            showToast("File " + filename + " not found.");
        }
        return result;
    }

    public void showDialog(final String filename) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("File is now creating " + filename).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showToast("File " + filename + " will be created now.");
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void createFile(String fileName) {
        File file = new File(super.getFilesDir(), fileName);
        try {
            file.createNewFile();
            showToast("File " + fileName + " is created.");
        }
        catch (IOException e) {
            showToast("File " + fileName + "is NOT created.");
        }
    }

    public void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
