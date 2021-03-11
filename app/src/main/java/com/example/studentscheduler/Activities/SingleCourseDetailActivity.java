package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.Notifications.AssessmentReceiver;
import com.example.studentscheduler.Notifications.CourseEndReceiver;
import com.example.studentscheduler.Notifications.CourseStartReceiver;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private Button notifyStartButton;
    private Button notifyEndButton;

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
        notifyStartButton = findViewById(R.id.courseStartNotificationBtn);
        notifyEndButton = findViewById(R.id.courseEndNotification);

        setCourseInfo();
        setCourseAssessmentList();
        setCourseInstructorList();
        setCourseNoteList();

        courseAssessmentsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent assessmentIntent = new Intent(getApplicationContext(), SingleAssessmentDetailActivity.class);
            assessmentIntent.putExtra("assessmentId", courseAssessment.get(position).getAssessment_Id());
            assessmentIntent.putExtra("courseId", courseId);
            startActivity(assessmentIntent);
        });

        courseInstructorsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent instructorIntent = new Intent(getApplicationContext(), SingleInstructorDetailActivity.class);
            instructorIntent.putExtra("instructorId", courseInstructor.get(position).getInstructor_id());
            instructorIntent.putExtra("courseId", courseId);
            startActivity(instructorIntent);
        });

        courseNotesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent noteIntent = new Intent(getApplicationContext(), SingleNoteDetailActivity.class);
            noteIntent.putExtra("noteId", courseNotes.get(position).getNote_id());
            noteIntent.putExtra("courseId", courseId);
            startActivity(noteIntent);
        });

        notifyStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(SingleCourseDetailActivity.this, startDateListener,year, month, day).show();
            }
        });
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myformat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                Intent intent = new Intent(SingleCourseDetailActivity.this, CourseStartReceiver.class);
                intent.putExtra("assessment", sdf.format(cal.getTime()));
                PendingIntent sender = PendingIntent.getBroadcast(SingleCourseDetailActivity.this,0,intent,0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                long trigger = cal.getTimeInMillis();
                alarmManager.set(AlarmManager.RTC_WAKEUP,trigger,sender);
            }
        };

        notifyEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar cal = Calendar.getInstance();
                int year = cal2.get(Calendar.YEAR);
                int month = cal2.get(Calendar.MONTH);
                int day = cal2.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(SingleCourseDetailActivity.this, endDateListener,year, month, day).show();
            }
        });
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal2.set(Calendar.YEAR,year);
                cal2.set(Calendar.MONTH,month);
                cal2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myformat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                Intent intent = new Intent(SingleCourseDetailActivity.this, CourseEndReceiver.class);
                intent.putExtra("assessment", sdf.format(cal2.getTime()));
                PendingIntent sender = PendingIntent.getBroadcast(SingleCourseDetailActivity.this,0,intent,0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                long trigger = cal2.getTimeInMillis();
                alarmManager.set(AlarmManager.RTC_WAKEUP,trigger,sender);
            }
        };
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

    public boolean onOptionsItemSelected(MenuItem item){
        int optionId = item.getItemId();

        if(optionId == R.id.editItem) {
            Intent intent = new Intent(getApplicationContext(), EditCourseActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("termId", termId);
            startActivity(intent);
        }

        if(optionId == R.id.deleteItem){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SingleCourseDetailActivity.this);
            builder.setTitle("Delete Course");
            builder.setMessage("Are you sure you want to delete this course?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Course course = new Course();
                    course = db.courseDao().getCurrentCourse(courseId);
                    db.courseDao().deleteCourse(course);
                    Intent intent = new Intent(SingleCourseDetailActivity.this, AllTermsActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddAssessmentToClass(View view) {
        Intent intent = new Intent(SingleCourseDetailActivity.this, AddAssessmentActivity.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("termId", termId);
        startActivity(intent);
        finish();
    }


    public void onAddInstructorToClass(View view) {
        Intent intent = new Intent(SingleCourseDetailActivity.this, AddInstructorActivity.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("termId", termId);
        startActivity(intent);
        finish();
    }

    public void onAddNoteToCourse(View view) {
        Intent intent = new Intent(SingleCourseDetailActivity.this,AddNoteActivity.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("termId", termId);
        startActivity(intent);
        finish();
    }

}
