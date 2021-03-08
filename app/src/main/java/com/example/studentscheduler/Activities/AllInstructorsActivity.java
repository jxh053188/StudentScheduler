package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.R;

import java.util.List;

public class AllInstructorsActivity extends AppCompatActivity {
    AppDatabase db;
    List<Instructor> allInstructors;
    ListView allInstructorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_instructors);
        db = AppDatabase.getInstance(getApplicationContext());
        allInstructorList = findViewById(R.id.instructorListView);
        updateAllInstructorList();
    }

    private void updateAllInstructorList(){
        List<Instructor> allInstructors = db.instructorDao().getAllInstructors();
        ArrayAdapter<Instructor> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allInstructors);
        allInstructorList.setAdapter(arrayAdapter);
        this.allInstructors = allInstructors;
        arrayAdapter.notifyDataSetChanged();
    }
}