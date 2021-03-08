package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
                try {
                    term = onSaveTerm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }if (addSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), AllTermsActivity.class);
                    startActivity(intent);
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

        if(termName.trim().isEmpty()){
            Toast.makeText(this, "Please enter a term name",Toast.LENGTH_SHORT).show();
            return null;
        }

        assert startDateDate != null;
        if(startDateDate.after(endDateDate)){
            Toast.makeText(this,"Dates are invalid", Toast.LENGTH_SHORT).show();
            return null;
        }

        if(startDate.trim().isEmpty()){
            Toast.makeText(this,"Please enter a start date", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(endDate.trim().isEmpty()){
            Toast.makeText(this,"Please enter an end date", Toast.LENGTH_SHORT).show();
            return null;
        }

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