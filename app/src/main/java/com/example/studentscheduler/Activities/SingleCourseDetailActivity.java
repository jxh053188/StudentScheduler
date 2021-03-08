package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.studentscheduler.R;

public class SingleCourseDetailActivity extends AppCompatActivity {
    TextView courseName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_course_detail);
    }
}