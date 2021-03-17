package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.XMLFormatter;

public class EditAssessmentActivity extends AppCompatActivity {

    private AppDatabase db;
    private int assessmentId;
    private int courseId;
    private EditText assessmentName;
    private EditText assessmentDate;
    private EditText assessmentScore;
    private Spinner assessmentType;
    private Spinner assessmentStatus;
    private String selectedType;
    private String selectedStatus;
    private Assessment assessment;
    private boolean updateSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        db = AppDatabase.getInstance(EditAssessmentActivity.this);
        Intent intent = getIntent();

        assessmentId = intent.getIntExtra("assessmentId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        assessmentName = findViewById(R.id.editAssessmentNameInput);
        assessmentDate = findViewById(R.id.editAssessmentDateInput);
        assessmentScore = findViewById(R.id.editAssessmentGrade);

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

        Button saveButton = findViewById(R.id.editAssessmentButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assessmentName.getText().toString().isEmpty() ||
                        assessmentDate.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditAssessmentActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill in all fields.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    try {
                        assessment = onUpdateAssessment();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (updateSuccessful = true) {
                        Intent intent = new Intent(getApplicationContext(), SingleAssessmentDetailActivity.class);
                        intent.putExtra("courseId", courseId);
                        intent.putExtra("assessmentId", assessmentId);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return 0;
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
        String aScore = String.valueOf(assessment.getAssessment_score());

        assessmentName.setText(name);
        assessmentStatus.setSelection(getIndex(assessmentStatus,status));
        assessmentDate.setText(dueDate);
        assessmentType.setSelection(getIndex(assessmentType, type));
        assessmentScore.setText(aScore);
    }

    public Assessment onUpdateAssessment() throws ParseException {
        SimpleDateFormat format;
        format = new SimpleDateFormat("MM/dd/yyyy");
        String aName = assessmentName.getText().toString();
        String date = assessmentDate.getText().toString();
        Date dueDate = format.parse(date);
        int score;

        if(assessmentScore.getText().toString().isEmpty()){
            score = 0;
        } else {
            score = Integer.parseInt(assessmentScore.getText().toString());
        }

        if(aName.trim().isEmpty()){
            Toast.makeText(this,"Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if(date.trim().isEmpty()){
            Toast.makeText(this,"Date cannot be empty", Toast.LENGTH_SHORT).show();
        }

        Assessment assessment = new Assessment();
        assessment.setAssessment_Id(assessmentId);
        assessment.setCourse_id_fk(courseId);
        assessment.setAssessment_name(aName);
        assessment.setAssessment_status(selectedStatus);
        assessment.setAssessment_type(selectedType);
        assessment.setAssessment_due_date(dueDate);
        assessment.setAssessment_score(score);
        db.assessmentDao().updateAssessment(assessment);
        Toast.makeText(this,"Assessment Updated", Toast.LENGTH_SHORT).show();
        updateSuccessful = true;

        return assessment;
    }
}