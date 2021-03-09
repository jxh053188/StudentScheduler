package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.R;

import java.text.ParseException;

public class AddInstructorActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText instructorName;
    private EditText instructorEmail;
    private EditText instructorPhone;
    private int courseId;
    private Instructor instructor;
    private boolean addSuccessful;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor);
        db = AppDatabase.getInstance(AddInstructorActivity.this);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        instructorName = findViewById(R.id.instructorNameInput);
        instructorEmail = findViewById(R.id.instructorEmailInput);
        instructorPhone = findViewById(R.id.instructorPhoneInput);
        termId = intent.getIntExtra("termId", -1);

        Button saveButton = findViewById(R.id.saveInstructorButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    instructor = onSaveInstructor();
                } catch (Exception e) {
                    e.printStackTrace();
                }if (addSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), SingleCourseDetailActivity.class);
                    intent.putExtra("termId", termId);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private Instructor onSaveInstructor(){
        String name = instructorName.getText().toString();
        String email = instructorEmail.getText().toString();
        String phone = instructorPhone.getText().toString();

        if(name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()){
            Toast.makeText(this, "Please enter all fields",Toast.LENGTH_SHORT).show();
            return null;
        }

        Instructor instructor = new Instructor();
        instructor.setInstructor_name(name);
        instructor.setInstructor_email(email);
        instructor.setInstructor_phone(phone);
        instructor.setCourse_id_fk(courseId);
        db.instructorDao().insertInstructor(instructor);
        Toast.makeText(this,"Instructor Added",Toast.LENGTH_SHORT).show();
        addSuccessful = true;

        return instructor;
    }
}