package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTermActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText termNameInput;
    private Spinner termStatusSpinner;
    private EditText termStartDateInput;
    private EditText termEndDateInput;
    private Term term;
    private boolean addSuccessful;
    private String choosenStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        termNameInput = findViewById(R.id.termNameInput);
        termStartDateInput = findViewById(R.id.startDateText);
        termEndDateInput = findViewById(R.id.endDateText);
        db = AppDatabase.getInstance(AddTermActivity.this);
        String[] arraySpinner = new String[]{
                "Pending", "In Progress", "Completed", "Dropped"
        };
        termStatusSpinner = (Spinner) findViewById(R.id.termStatusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termStatusSpinner.setAdapter(adapter);

        termStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenStatus = termStatusSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button saveButton = findViewById(R.id.saveTermButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termNameInput.getText().toString().isEmpty() || termStartDateInput.getText().toString().isEmpty() || termEndDateInput.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTermActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill in all fields and check \n that start date is before end date.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    try {
                        term = onSaveTerm();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (addSuccessful = true) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    public Term onSaveTerm() throws ParseException {
        SimpleDateFormat format;
        format = new SimpleDateFormat("MM/dd/yyyy");
        String termName = termNameInput.getText().toString();
        String startDate = termStartDateInput.getText().toString();
        String endDate = termEndDateInput.getText().toString();
        Date startDateDate = format.parse(startDate);
        Date endDateDate = format.parse(endDate);

        Term term = new Term();
        term.setTerm_name(termName);
        term.setTerm_status(choosenStatus);
        term.setTerm_start(startDateDate);
        term.setTerm_end(endDateDate);
        db.termDao().insertTerm(term);
        Toast.makeText(this,"New term has been saved",Toast.LENGTH_SHORT).show();
        addSuccessful = true;

        return term;
    }
}