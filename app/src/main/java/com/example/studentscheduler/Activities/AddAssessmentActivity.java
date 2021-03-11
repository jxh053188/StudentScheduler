package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAssessmentActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText assessmentName;
    private Spinner assessmentType;
    private EditText assessmentDate;
    private Spinner assessmentStatus;
    private String selectedType;
    private String selectedStatus;
    private boolean addSuccessful;
    private Assessment assessment;
    private int courseId;
    private int numAssessments;
    private List<Assessment> assessmentList;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        db = AppDatabase.getInstance(AddAssessmentActivity.this);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
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
                if (numAssessments > 4) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddAssessmentActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("A class cannot have more than 5 assessments");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                } else if (assessmentDate.getText().toString().isEmpty() ||
                        assessmentName.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddAssessmentActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill in all fields and check \n that start date is before end date.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    try {
                        onSaveAssessment();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (addSuccessful = true) {
                        Intent intent = new Intent(getApplicationContext(), SingleCourseDetailActivity.class);
                        intent.putExtra("courseId", courseId);
                        intent.putExtra("termId", termId);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        assessmentList = db.assessmentDao().getAssessmentList(courseId);
        numAssessments = assessmentList.size();

    }

    public void onSaveAssessment() throws ParseException {
            SimpleDateFormat format;
            format = new SimpleDateFormat("MM/dd/yyyy");
            String aName = assessmentName.getText().toString();
            String date = assessmentDate.getText().toString();
            Date dueDate = format.parse(date);

            Assessment assessment = new Assessment();
            assessment.setAssessment_name(aName);
            assessment.setAssessment_status(selectedStatus);
            assessment.setAssessment_type(selectedType);
            assessment.setAssessment_due_date(dueDate);
            assessment.setCourse_id_fk(courseId);
            db.assessmentDao().insertAssessment(assessment);
            Toast.makeText(this, "New Assessment is saved", Toast.LENGTH_SHORT).show();
            addSuccessful = true;
        }

}
