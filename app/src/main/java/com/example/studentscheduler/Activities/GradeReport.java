package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GradeReport extends AppCompatActivity {

    AppDatabase db;
    ListView gradeReportList;
    int courseId;
    int termId;
    TextView courseName;
    TextView dateGenerated;
    TextView courseGrade;
    String courseGradePer;

    List<Assessment> courseAssessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_report);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());
        gradeReportList = findViewById(R.id.gradeListView);
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        courseName = findViewById(R.id.GRHeaderName);
        dateGenerated = findViewById(R.id.GRHeaderDate);
        courseGrade = findViewById(R.id.GRHeaderGrade);

        courseAssessments = db.assessmentDao().getAssessmentList(courseId);
        List<Assessment> completedAssessments = new ArrayList<>();
        for(Assessment assessment : courseAssessments){
            if (assessment.getAssessment_status().equals("Passed") || assessment.getAssessment_status().equals("Failed"))
                completedAssessments.add(assessment);
        }

        GradeReportAdapter adapter = new GradeReportAdapter(this, R.layout.adapter_view_layout, completedAssessments);
        gradeReportList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        courseGradePer = String.valueOf(db.assessmentDao().assessmentAvg(courseId));
        int gradeInt = db.assessmentDao().assessmentAvg(courseId);
        String letterGrade;

        if(gradeInt >= 90){
            letterGrade = "A";
        }
        else if(gradeInt < 90 && gradeInt >= 80){
            letterGrade = "B";
        }
        else if(gradeInt < 80 && gradeInt >= 70){
            letterGrade = "C";
        }
        else if(gradeInt < 70 && gradeInt >= 60){
            letterGrade = "D";
        }
        else{
            letterGrade = "F";
        }



        courseGrade.setText(courseGradePer + "%   " + letterGrade);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
        dateGenerated.setText(sdf.format(date));

        Course course = new Course();
        course = db.courseDao().getCurrentCourse(courseId);
        String cName = course.getCourse_name();
        courseName.setText(cName);


    }



}