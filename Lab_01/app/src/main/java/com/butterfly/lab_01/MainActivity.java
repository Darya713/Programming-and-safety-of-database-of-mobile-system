package com.butterfly.lab_01;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "Logs";
    final String Absolute = "Absolute: ";
    final String Name = "Name: ";
    final String Path = "Path: ";
    final String ReadWrite = "Read/Write: ";
    final String External = "External: ";

    String read = "";
    String write = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void FileSystem(File file)
    {
        try {
            TextView textViewAbsolute = (TextView) findViewById(R.id.tv_absolute_caption);
            textViewAbsolute.setText(Absolute + file.getAbsolutePath());
            TextView textViewName = (TextView) findViewById(R.id.tv_name_caption);
            textViewName.setText(Name + file.getName());
            TextView textViewPath = (TextView) findViewById(R.id.tv_path_caption);
            textViewPath.setText(Path + file.getPath());
            TextView textViewReadWrite = (TextView) findViewById(R.id.tv_read_write_caption);
            if (file.canRead()) read = "yes";
            else read = "no";
            if (file.canWrite()) write = "yes";
            else write = "no";
            textViewReadWrite.setText(ReadWrite + read + "/" + write);
            TextView textViewExternal = (TextView) findViewById(R.id.tv_external_caption);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                textViewExternal.setText(External + Environment.getExternalStorageState());
            else
                Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_getFilesDir:
                FileSystem(new File(getFilesDir() + ""));
                break;
            case R.id.btn_getCacheDir:
                FileSystem(new File(getCacheDir() + ""));
                break;
            case R.id.btn_getExternalFilesDir:
                FileSystem(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + ""));
                break;
            case R.id.btn_getExternalCacheDir:
                FileSystem(new File(getExternalCacheDir() + ""));
                break;
            case R.id.btn_getExternalStorageDirectory:
                FileSystem(new File(Environment.getExternalStorageDirectory() + ""));
                break;
            case R.id.btn_getExternalStoragePublicDirectory:
                FileSystem(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + ""));
                break;
        }

    }
}
