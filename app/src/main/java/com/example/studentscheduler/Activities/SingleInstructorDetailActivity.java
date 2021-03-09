package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.R;

public class SingleInstructorDetailActivity extends AppCompatActivity {

    AppDatabase db;
    int instructorId;
    TextView instructorName;
    TextView instructorEmail;
    TextView instructorPhone;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_instructor_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        instructorId = intent.getIntExtra("instructorId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        instructorName = findViewById(R.id.instructorDetailName);
        instructorEmail = findViewById(R.id.instructorDetailEmail);
        instructorPhone = findViewById(R.id.instructorDetailPhone);

        setInstructorDetails();
    }

    public void setInstructorDetails(){
        Instructor instructor = new Instructor();
        instructor = db.instructorDao().getInstructorDetail(instructorId);
        String name = instructor.getInstructor_name();
        String email = instructor.getInstructor_email();
        String phone = instructor.getInstructor_phone();

        instructorName.setText(name);
        instructorEmail.setText(email);
        instructorPhone.setText(phone);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int optionId = item.getItemId();

        if(optionId == R.id.editItem){
            Intent intent = new Intent(getApplicationContext(),EditInstructorActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("instructorId", instructorId);
            startActivity(intent);

        }

        if (optionId == R.id.notifyOption);{

        }

        if(optionId == R.id.deleteItem);{

        }
        return super.onOptionsItemSelected(item);
    }
}