package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.R;

public class EditTermActivity extends AppCompatActivity {
    AppDatabase db;

    EditText termName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
    }
}