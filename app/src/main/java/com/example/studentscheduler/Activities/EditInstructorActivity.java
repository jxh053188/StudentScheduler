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

public class EditInstructorActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText instructorName;
    private EditText instructorEmail;
    private EditText instructorPhone;
    private int courseId;
    private int instructorId;
    private Instructor instructor;
    private boolean updateSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_instructor);
        db = AppDatabase.getInstance(EditInstructorActivity.this);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        instructorId = intent.getIntExtra("instructorId", -1);
        instructorName = findViewById(R.id.updateInstructorNameInput);
        instructorEmail = findViewById(R.id.updateInstructorEmailInput);
        instructorPhone = findViewById(R.id.updateInstructorPhoneInput);

        setInstructorInfo();
        Button saveButton = findViewById(R.id.updateInstructorButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    instructor = onUpdateInstructor();
                } catch (Exception e) {
                    e.printStackTrace();
                }if (updateSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), SingleInstructorDetailActivity.class);
                    intent.putExtra("instructorId", instructorId);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setInstructorInfo(){
        Instructor instructor = new Instructor();
        instructor = db.instructorDao().getInstructorDetail(instructorId);
        String name = instructor.getInstructor_name();
        String email = instructor.getInstructor_email();
        String phone = instructor.getInstructor_phone();

        instructorName.setText(name);
        instructorEmail.setText(email);
        instructorPhone.setText(phone);
    }

    private Instructor onUpdateInstructor(){
        String name = instructorName.getText().toString();
        String email = instructorEmail.getText().toString();
        String phone = instructorPhone.getText().toString();

        if(name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()){
            Toast.makeText(this, "Please enter all fields",Toast.LENGTH_SHORT).show();
            return null;
        }

        Instructor instructor = new Instructor();
        instructor.setInstructor_id(instructorId);
        instructor.setInstructor_name(name);
        instructor.setInstructor_email(email);
        instructor.setInstructor_phone(phone);
        instructor.setCourse_id_fk(courseId);
        db.instructorDao().updateInstructor(instructor);
        Toast.makeText(this,"Instructor Added",Toast.LENGTH_SHORT).show();
        updateSuccessful = true;

        return instructor;

    }
}