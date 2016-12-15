package com.example.butterfly.lab_10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText idOfRegion, region;
    TextView textView;
    Button insert, select, update, delete;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idOfRegion = (EditText) findViewById(R.id.idOfRegion);
        region = (EditText) findViewById(R.id.region);
        textView = (TextView) findViewById(R.id.textView);
        insert = (Button) findViewById(R.id.insert);
        select = (Button) findViewById(R.id.select);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);

        control();
    }

    void control() {
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread = new Thread(new DbConnector("INSERT",
                        "set IDENTITY_INSERT dbo.Area ON; " +
                        "INSERT INTO dbo.Area (КодОбласти, Область) VALUES (" +
                        idOfRegion.getText() + ", \'" +
                        region.getText() + "\');"));
                thread.start();
                showToast("Insert OK");
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                thread = new Thread(new DbConnector("SELECT", ""));
                thread.start();
                try {
                    thread.join();
                }
                catch (Exception e) {
                    Log.d("LAB_10_Thread", e.getMessage());
                }
                for (String temp: DbConnector.getResult()) {
                    textView.setText(textView.getText() + temp + "\n");
                }
                DbConnector.clearArrayList();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread = new Thread(new DbConnector("UPDATE",
                        "UPDATE dbo.Area SET Область = \'" +
                        region.getText() + "\' WHERE КодОбласти = " +
                        idOfRegion.getText() + ";"));
                thread.start();
                showToast("Update OK");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread = new Thread(new DbConnector("DELETE",
                        "DELETE FROM dbo.Area WHERE КодОбласти = " +
                        idOfRegion.getText()));
                thread.start();
                showToast("Delete OK");
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
