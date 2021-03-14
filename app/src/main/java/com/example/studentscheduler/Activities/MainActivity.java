package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Data.PopulateDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    TextView termDataText;
    TextView courseInProgressData;
    TextView courseCompletedData;
    TextView courseDroppedData;
    TextView assessmentInProgressData;
    TextView assessmentPassedData;
    TextView assessmentFailedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getInstance(getApplicationContext());
        termDataText = findViewById(R.id.termDataText);
        courseInProgressData = findViewById(R.id.courseProgressDataLabel);
        courseCompletedData = findViewById(R.id.courseCompletedDataLabel);
        courseDroppedData = findViewById(R.id.courseDroppedDataLabel);
        assessmentInProgressData = findViewById(R.id.assessmentInProgressDataLabel);
        assessmentPassedData = findViewById(R.id.assessmentPassedDataLabel);
        assessmentFailedData = findViewById(R.id.assessmentFailedDataLabel);
        updateViews();
    }

    public void onTermsScreen(View view) {
        Intent intent = new Intent(MainActivity.this, AllTermsActivity.class);
        startActivity(intent);
    }

    public void onInstructorsList(View view) {
        Intent intent = new Intent(MainActivity.this, AllAssessmentsActivity.class);
        startActivity(intent);
    }

    public void onCourseList(View view) {
        Intent intent = new Intent(MainActivity.this, AllCoursesActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.populateDBMenu:
                PopulateDatabase populateDatabase = new PopulateDatabase();
                populateDatabase.populate(getApplicationContext());
                updateViews();
                return true;
            case R.id.resetDBMenu:
                db.clearAllTables();
                updateViews();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateViews() {
        int term = 0;
        int termComplete = 0;
        int termPending = 0;
        int course = 0;
        int assessment = 0;
        int coursesPending = 0;
        int coursesCompleted = 0;
        int coursesDropped = 0;
        int assessmentsPending = 0;
        int assessmentsPassed = 0;
        int assessmentsFailed = 0;

        try {
            List<Term> termList = db.termDao().getAllTerms();
            List<Course> courseList = db.courseDao().getAllCourses();
            List<Assessment> assessmentList = db.assessmentDao().getAllAssessments();

            try {
                for (int i = 0; i < termList.size(); i++) {
                    term = termList.size();}
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < courseList.size(); i++) {
                    course = courseList.size();
                    if (courseList.get(i).getCourse_status().contains("Pending")) coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("In Progress"))
                        coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("Completed"))
                        coursesCompleted++;
                    if (courseList.get(i).getCourse_status().contains("Dropped")) coursesDropped++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < assessmentList.size(); i++) {
                    assessment = assessmentList.size();
                    if (assessmentList.get(i).getAssessment_status().contains("Pending"))
                        assessmentsPending++;
                    if (assessmentList.get(i).getAssessment_status().contains("Passed"))
                        assessmentsPassed++;
                    if (assessmentList.get(i).getAssessment_status().contains("Failed"))
                        assessmentsFailed++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        termDataText.setText(String.valueOf(term));
        courseInProgressData.setText(String.valueOf(coursesPending));
        courseCompletedData.setText(String.valueOf(coursesCompleted));
        courseDroppedData.setText(String.valueOf(coursesDropped));
        assessmentInProgressData.setText(String.valueOf(assessmentsPending));
        assessmentFailedData.setText(String.valueOf(assessmentsFailed));
        assessmentPassedData.setText(String.valueOf(assessmentsPassed));
    }


}