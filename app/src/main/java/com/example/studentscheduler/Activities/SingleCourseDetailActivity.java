package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class SingleCourseDetailActivity extends AppCompatActivity {
    AppDatabase db;

    int courseId;
    int termId;
    TextView courseName;
    TextView courseStart;
    TextView courseEnd;
    TextView courseStatus;
    ListView courseAssessmentsList;
    ListView courseInstructorsList;
    ListView courseNotesList;
    List<Assessment> courseAssessment;
    List<Instructor> courseInstructor;
    List<Note> courseNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_course_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        courseName = findViewById(R.id.singleCourseName);
        courseStart = findViewById(R.id.singleCourseStartDate);
        courseEnd = findViewById(R.id.singleCourseEndDate);
        courseStatus = findViewById(R.id.singleCourseStatus);
        courseAssessmentsList = findViewById(R.id.singleCourseAssessmentListView);
        courseInstructorsList = findViewById(R.id.singleCourseInstructorView);
        courseNotesList = findViewById(R.id.singleCourseNotesListView);

        setCourseInfo();
        setCourseAssessmentList();
        setCourseInstructorList();
        setCourseNoteList();

        courseAssessmentsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent assessmentIntent = new Intent(getApplicationContext(), SingleAssessmentDetailActivity.class);
            assessmentIntent.putExtra("assessmentId", courseAssessment.get(position).getAssessment_Id());
            startActivity(assessmentIntent);
        });

        courseInstructorsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent instructorIntent = new Intent(getApplicationContext(), SingleInstructorDetailActivity.class);
            instructorIntent.putExtra("instructorId", courseInstructor.get(position).getInstructor_id());
            startActivity(instructorIntent);
        });

        courseNotesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent noteIntent = new Intent(getApplicationContext(), SingleNoteDetailActivity.class);
            noteIntent.putExtra("noteId", courseNotes.get(position).getNote_id());
            startActivity(noteIntent);
        });
    }

    private void setCourseInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Course course = new Course();
        course = db.courseDao().getCourse(termId, courseId);
        String name = course.getCourse_name();
        String status = course.getCourse_status();
        String start = sdf.format(course.getCourse_start());
        String end = sdf.format(course.getCourse_end());

        courseName.setText(name);
        courseStatus.setText(status);
        courseStart.setText(start);
        courseEnd.setText(end);
    }

    private void setCourseAssessmentList(){
        List<Assessment> courseAssessment = db.assessmentDao().getAssessmentList(courseId);
        ArrayAdapter<Assessment> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,courseAssessment);
        courseAssessmentsList.setAdapter(arrayAdapter);
        this.courseAssessment = courseAssessment;
        arrayAdapter.notifyDataSetChanged();
    }

    private void setCourseInstructorList(){
        List<Instructor> courseInstructor = db.instructorDao().getInstructorList(courseId);
        ArrayAdapter<Instructor> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseInstructor);
        courseInstructorsList.setAdapter(arrayAdapter);
        this.courseInstructor = courseInstructor;
        arrayAdapter.notifyDataSetChanged();
    }

    private void setCourseNoteList(){
        List<Note> courseNotes = db.noteDao().getNoteList(courseId);
        ArrayAdapter<Note> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNotes);
        courseNotesList.setAdapter(arrayAdapter);
        this.courseNotes = courseNotes;
        arrayAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }
}