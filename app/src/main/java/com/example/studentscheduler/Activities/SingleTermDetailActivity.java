package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;

import java.util.List;

public class SingleTermDetailActivity extends AppCompatActivity {

    AppDatabase db;
    int termId;
    TextView termName;
    TextView termStartDate;
    TextView termEndDate;
    TextView termStatus;
    ListView termCoursesView;
    List<Course> termCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_term_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        termId = intent.getIntExtra("termId", -1);
        termName = findViewById(R.id.singleTermNameLabel);
        termStartDate = findViewById(R.id.singleTermStartDate);
        termEndDate = findViewById(R.id.singleTermEndDate);
        termStatus = findViewById(R.id.singleTermStatusLabel);
        termCoursesView = findViewById(R.id.classesAssignedToTermListView);

        setTermInfo();
        setCourseList();
    }

    private void setTermInfo(){
        Term term = new Term();
        term = db.termDao().getTerm(termId);
        String name = term.getTerm_name();
        String status = term.getTerm_status();
        String start = term.getTerm_start().toString();
        String end = term.getTerm_end().toString();

        termName.setText(name);
        termStatus.setText(status);
        termStartDate.setText(start);
        termEndDate.setText(end);
    }

    private void setCourseList(){
        List<Course> termCourses = db.courseDao().getCourseList(termId);
        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,termCourses);
        termCoursesView.setAdapter(arrayAdapter);
        this.termCourses = termCourses;
        arrayAdapter.notifyDataSetChanged();
    }
}