package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.R;

import java.util.List;

public class AllCoursesActivity extends AppCompatActivity {

    AppDatabase db;
    List<Course> allCourses;
    ListView allCoursesList;
    EditText courseSearch;
    ArrayAdapter<Course> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);
        db = AppDatabase.getInstance(getApplicationContext());
        allCoursesList = findViewById(R.id.allCoursesListView);
        courseSearch = findViewById(R.id.courseSearchInput);
        //updateAllCoursesList();

        List<Course> allCourses = db.courseDao().getAllCourses();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        allCoursesList.setAdapter(arrayAdapter);
        this.allCourses = allCourses;
        arrayAdapter.notifyDataSetChanged();

        courseSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (AllCoursesActivity.this).arrayAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        allCoursesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent assessmentIntent = new Intent(AllCoursesActivity.this, SingleCourseDetailActivity.class);
            assessmentIntent.putExtra("courseId", allCourses.get(position).getCourse_id());
            assessmentIntent.putExtra("termId",allCourses.get(position).getTerm_id_fk());
            startActivity(assessmentIntent);
        });
    }
/*
    private void updateAllCoursesList(){
        List<Course> allCourses = db.courseDao().getAllCourses();
        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        allCoursesList.setAdapter(arrayAdapter);
        this.allCourses = allCourses;
        arrayAdapter.notifyDataSetChanged();
    }*/
}