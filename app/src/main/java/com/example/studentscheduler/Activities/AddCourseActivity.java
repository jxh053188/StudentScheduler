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
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText courseName;
    private Spinner courseStatus;
    private EditText courseStart;
    private EditText courseEnd;
    private int termId;
    private boolean addSuccessful;
    private String selectedStatus;
    private Course course;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        db = AppDatabase.getInstance(AddCourseActivity.this);
        courseName = findViewById(R.id.courseNameInput);
        courseStart = findViewById(R.id.courseStartDateInput);
        courseEnd = findViewById(R.id.courseEndDateInput);
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);


        String[] courseSpinner = new String[]{
                "In Progress", "Completed", "Dropped", "Plan To Take"
        };
         //Set status spinner options
        courseStatus = findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatus.setAdapter(adapter);

        courseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = courseStatus.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button saveButton = findViewById(R.id.saveCourseButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    course = onSaveCourse();
                } catch (ParseException e) {
                    e.printStackTrace();
                }if (addSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), SingleTermDetailActivity.class);
                    intent.putExtra("termId", termId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public Course onSaveCourse() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String name = courseName.getText().toString();
        String start = courseStart.getText().toString();
        String end = courseEnd.getText().toString();
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a term name", Toast.LENGTH_SHORT).show();
            return null;
        }

        assert startDate != null;
        if (startDate.after(endDate)) {
            Toast.makeText(this, "Dates are invalid", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (start.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a start date", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (end.trim().isEmpty()) {
            Toast.makeText(this, "Please enter an end date", Toast.LENGTH_SHORT).show();
            return null;
        }

        Course course = new Course();
        course.setCourse_name(name);
        course.setCourse_status(selectedStatus);
        course.setCourse_start(startDate);
        course.setCourse_end(endDate);
        course.setTerm_id_fk(termId);
        db.courseDao().insertCourse(course);
        Toast.makeText(this,"Course added",Toast.LENGTH_SHORT).show();
        addSuccessful = true;

        return course;
    }
}