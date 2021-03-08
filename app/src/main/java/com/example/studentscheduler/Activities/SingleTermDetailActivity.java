package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Term term;
    boolean updateSuccessful;

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

        termCoursesView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(getApplicationContext(), SingleCourseDetailActivity.class);
            intent1.putExtra("termId", termId);
            intent1.putExtra("courseId", termCourses.get(position).getCourse_id());
            startActivity(intent1);
            System.out.println(id);
        });

    }

    private void setTermInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Term term = new Term();
        term = db.termDao().getTerm(termId);
        String name = term.getTerm_name();
        String status = term.getTerm_status();
        String start = sdf.format(term.getTerm_start());
        String end = sdf.format(term.getTerm_end());

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int optionId = item.getItemId();

        if(optionId == R.id.editItem){
            Intent intent = new Intent(getApplicationContext(),EditTermActivity.class);
            intent.putExtra("termId", termId);
            startActivity(intent);

        }

        if (optionId == R.id.notifyOption);{

        }

        if(optionId == R.id.deleteItem);{

        }
        return super.onOptionsItemSelected(item);
    }
}