package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAssessmentActivity extends AppCompatActivity {
    AppDatabase db;
    EditText assessmentName;
    Spinner assessmentType;
    EditText assessmentDate;
    Spinner assessmentStatus;
    String selectedType;
    String selectedStatus;
    boolean addSuccessful;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        db = AppDatabase.getInstance(AddAssessmentActivity.this);
        Intent intent = getIntent();
        assessmentName = findViewById(R.id.assessmentNameInput);
        assessmentDate = findViewById(R.id.assessmentDateInput);

        String[] typeSpinner = new String[]{
                "Performance Assessment", "Objective Assessment"
        };

        String[] statusSpinner = new String[]{
                "Pending", "Passed", "Failed"
        };

        //set options to type spinner
        assessmentType = findViewById(R.id.assessmentTypeSpinner);
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
        assessmentStatus = findViewById(R.id.assessmentStatusSpinner);
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

        Button saveButton = findViewById(R.id.saveAssessmentButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    onSaveAssessment();
                } catch (ParseException e){
                    e.printStackTrace();
                } if (addSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), SingleCourseDetailActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void onSaveAssessment() throws ParseException {
        SimpleDateFormat format;
        format = new SimpleDateFormat("MM/dd/yyyy");
        String aName = assessmentName.getText().toString();
        String date = assessmentDate.getText().toString();
        Date dueDate = format.parse(date);

        if(aName.trim().isEmpty()){
            Toast.makeText(this,"Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if(date.trim().isEmpty()){
            Toast.makeText(this,"Date cannot be empty", Toast.LENGTH_SHORT).show();
        }

        Assessment assessment = new Assessment();
        assessment.setAssessment_name(aName);
        assessment.setAssessment_status(selectedStatus);
        assessment.setAssessment_type(selectedType);
        assessment.setAssessment_due_date(dueDate);
        db.assessmentDao().insertAssessment(assessment);
        Toast.makeText(this,"New Assessment is saved", Toast.LENGTH_SHORT).show();
        addSuccessful = true;
    }
}