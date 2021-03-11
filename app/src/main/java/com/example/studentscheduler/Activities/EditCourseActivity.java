package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText courseName;
    private EditText courseStart;
    private EditText courseEnd;
    private Spinner courseStatus;
    private Course course;
    private boolean updateSuccessful;
    private String selectedStatus;
    private int courseId;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        db = AppDatabase.getInstance(EditCourseActivity.this);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        courseName = findViewById(R.id.editCourseNameInput);
        courseStart = findViewById(R.id.editCourseStartDateInput);
        courseEnd = findViewById(R.id.editCourseEndDateInput);

        String[] courseSpinner = new String[]{
                "In Progress", "Completed", "Dropped", "Plan To Take"
        };
        //Set status spinner options
        courseStatus = findViewById(R.id.editCourseStatusSpinner);
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

        setCourseInfo();

        Button saveButton = findViewById(R.id.updateCourseButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseName.getText().toString().isEmpty() || courseStart.getText().toString().isEmpty() || courseEnd.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditCourseActivity.this);
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
                        course = onUpdateCourse();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (updateSuccessful = true) {
                        Intent intent = new Intent(getApplicationContext(), SingleCourseDetailActivity.class);
                        intent.putExtra("courseId", courseId);
                        intent.putExtra("termId", termId);
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

    private void setCourseInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Course course = new Course();
        course = db.courseDao().getCourse(termId, courseId);
        String name = course.getCourse_name();
        String status = course.getCourse_status();
        String start = sdf.format(course.getCourse_start());
        String end = sdf.format(course.getCourse_end());

        courseName.setText(name);
        courseStatus.setSelection(getIndex(courseStatus, status));
        courseStart.setText(start);
        courseEnd.setText(end);
    }

    private Course onUpdateCourse() throws ParseException {
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
        course.setCourse_id(courseId);
        db.courseDao().updateCourse(course);
        Toast.makeText(this,"Course Updated",Toast.LENGTH_SHORT).show();
        updateSuccessful = true;

        return course;
    }
}