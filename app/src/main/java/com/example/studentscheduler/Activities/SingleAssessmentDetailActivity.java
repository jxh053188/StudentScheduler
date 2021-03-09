package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleAssessmentDetailActivity extends AppCompatActivity {
    AppDatabase db;

    private int assessmentId;
    private int courseId;
    private TextView assessmentName;
    private TextView assessmentType;
    private TextView assessmentStatus;
    private TextView assessmentDueDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_assessment_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        assessmentId = intent.getIntExtra("assessmentId", -1);
        assessmentName = findViewById(R.id.assessmentDetailName);
        assessmentType = findViewById(R.id.assessmentDetailType);
        assessmentStatus = findViewById(R.id.assessmentDetailStatus);
        assessmentDueDate = findViewById(R.id.assessmentDetailDueDate);
        courseId = intent.getIntExtra("courseId" ,-1);

        setAssessmentInfo();
    }

    private void setAssessmentInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getSingleAssessment(assessmentId);
        String name = assessment.getAssessment_name();
        String status = assessment.getAssessment_status();
        String type = assessment.getAssessment_type();
        Date date = assessment.getAssessment_due_date();
        String dueDate = sdf.format(date);

        assessmentName.setText(name);
        assessmentStatus.setText(status);
        assessmentDueDate.setText(dueDate);
        assessmentType.setText(type);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int optionId = item.getItemId();

        if(optionId == R.id.editItem){
            Intent intent = new Intent(getApplicationContext(),EditAssessmentActivity.class);
            intent.putExtra("assessmentId", assessmentId);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
            finish();

        }

        if (optionId == R.id.notifyOption){

        }

        if(optionId == R.id.deleteItem){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SingleAssessmentDetailActivity.this);
            builder.setTitle("Delete Assessment");
            builder.setMessage("Are you sure you want to delete this assessment?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Assessment assessment = new Assessment();
                    assessment = db.assessmentDao().getSingleAssessment(assessmentId);
                    db.assessmentDao().deleteAssessment(assessment);
                    Intent intent = new Intent(SingleAssessmentDetailActivity.this, AllTermsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}