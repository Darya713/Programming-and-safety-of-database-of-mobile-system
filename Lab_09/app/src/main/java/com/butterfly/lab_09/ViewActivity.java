package com.butterfly.lab_09;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    TextView textViewForView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        textViewForView = (TextView) findViewById(R.id.text_view_for_view);
        textViewForView.setText(getIntent().getStringExtra("view"));
    }
}
