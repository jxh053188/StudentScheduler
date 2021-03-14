package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.R;

import java.util.List;

public class AllAssessmentsActivity extends AppCompatActivity {
    AppDatabase db;
    List<Assessment> allAssessments;
    ListView allAssessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_instructors);
        db = AppDatabase.getInstance(getApplicationContext());
        allAssessmentList = findViewById(R.id.instructorListView);
        updateAllAssessmentList();

        allAssessmentList.setOnItemClickListener((parent, view, position, id) -> {
            Intent assessmentIntent = new Intent(getApplicationContext(), SingleAssessmentDetailActivity.class);
            assessmentIntent.putExtra("assessmentId", allAssessments.get(position).getAssessment_Id());
            assessmentIntent.putExtra("courseId",allAssessments.get(position).getCourse_id_fk());
            startActivity(assessmentIntent);
        });
    }

    private void updateAllAssessmentList(){
        List<Assessment> allAssessments = db.assessmentDao().getAllAssessments();
        ArrayAdapter<Assessment> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAssessments);
        allAssessmentList.setAdapter(arrayAdapter);
        this.allAssessments = allAssessments;
        arrayAdapter.notifyDataSetChanged();
    }
}