package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditAssessmentActivity extends AppCompatActivity {

    private AppDatabase db;
    private int assessmentId;

    private EditText assessmentName;
    private EditText assessmentDate;
    private Spinner assessmentType;
    private Spinner assessmentStatus;
    private String selectedType;
    private String selectedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        db = AppDatabase.getInstance(EditAssessmentActivity.this);
        Intent intent = getIntent();

        assessmentId = intent.getIntExtra("assessmentId", -1);
        assessmentName = findViewById(R.id.editAssessmentNameInput);
        assessmentDate = findViewById(R.id.editAssessmentDateInput);

        String[] typeSpinner = new String[]{
                "Performance Assessment", "Objective Assessment"
        };

        String[] statusSpinner = new String[]{
                "Pending", "Passed", "Failed"
        };

        //set options to type spinner
        assessmentType = findViewById(R.id.editAssessmentTypeSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeSpinner);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentType.setAdapter(typeAdapter);

        //type spinner listener
        assessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = assessmentType.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set options to status spinner
        assessmentStatus = findViewById(R.id.editAssessmentStatusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusSpinner);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentStatus.setAdapter(statusAdapter);

        //options status listener
        assessmentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = assessmentStatus.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAssessmentInfo();

    }

    private void setAssessmentInfo(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getSingleAssessment(assessmentId);
        String name = assessment.getAssessment_name();
        String status = assessment.getAssessment_status();
        String type = assessment.getAssessment_type();
        Date date = assessment.getAssessment_due_date();
        String dueDate = dateFormat.format(date);

        assessmentName.setText(name);
        assessmentStatus.setPrompt(status);
        assessmentDate.setText(dueDate);
        assessmentType.setPrompt(type);
    }
}