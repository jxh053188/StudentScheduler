package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.R;

import java.util.List;

public class AllCoursesActivity extends AppCompatActivity {

    AppDatabase db;
    List<Course> allCourses;
    ListView allCoursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);
        db = AppDatabase.getInstance(getApplicationContext());
        allCoursesList = findViewById(R.id.allCoursesListView);
        updateAllCoursesList();

        allCoursesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent assessmentIntent = new Intent(AllCoursesActivity.this, SingleCourseDetailActivity.class);
            assessmentIntent.putExtra("courseId", allCourses.get(position).getCourse_id());
            assessmentIntent.putExtra("termId",allCourses.get(position).getTerm_id_fk());
            startActivity(assessmentIntent);
        });
    }

    private void updateAllCoursesList(){
        List<Course> allCourses = db.courseDao().getAllCourses();
        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        allCoursesList.setAdapter(arrayAdapter);
        this.allCourses = allCourses;
        arrayAdapter.notifyDataSetChanged();
    }
}