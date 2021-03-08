package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;

public class SingleAssessmentDetailActivity extends AppCompatActivity {
    AppDatabase db;

    int assessmentId;
    TextView assessmentName;
    TextView assessmentType;
    TextView assessmentStatus;
    TextView assessmentDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_assessment_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        assessmentId = intent.getIntExtra("assessmentId", -1);
        assessmentName = findViewById(R.id.assessmentDetailName);
        assessmentType = findViewById(R.id.assessmentDetailType);
        assessmentStatus = findViewById(R.id.assessmentDetailStatus);
        assessmentDueDate = findViewById(R.id.assessmentDetailDueDate);

        setAssessmentInfo();
    }

    private void setAssessmentInfo(){
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getSingleAssessment(assessmentId);
        String name = assessment.getAssessment_name();
        String status = assessment.getAssessment_status();
        String type = assessment.getAssessment_type();
        String date = assessment.getAssessment_due_date().toString();

        assessmentName.setText(name);
        assessmentStatus.setText(status);
        assessmentDueDate.setText(date);
        assessmentType.setText(type);
    }
}