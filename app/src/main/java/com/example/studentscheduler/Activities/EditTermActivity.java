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
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTermActivity extends AppCompatActivity {
    private AppDatabase db;
    private int termId;
    private Term term;
    private EditText termName;
    private Spinner termStatusSpinner;
    private EditText termStart;
    private EditText termEnd;
    private boolean updateSuccessful;
    private String selectedStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        db = AppDatabase.getInstance(EditTermActivity.this);
        Intent intent = getIntent();
        termName = findViewById(R.id.editTermNameInput);
        termStart = findViewById(R.id.editTermStartDateInput);
        termEnd = findViewById(R.id.editTermEndDateInput);
        termId = intent.getIntExtra("termId", -1);

        String[] arraySpinner = new String[]{
                "Pending", "In Progress", "Completed", "Dropped"
        };

        //Set items into term status spinner
        termStatusSpinner = (Spinner) findViewById(R.id.editTermStatusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termStatusSpinner.setAdapter(adapter);

        //create listener for term status spinner
        termStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = termStatusSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setTermInfo();

        Button saveButton = findViewById(R.id.updateTermButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    term = onUpdateTerm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }if (updateSuccessful = true){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void setTermInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
        Term term = new Term();
        term = db.termDao().getTerm(termId);
        String name = term.getTerm_name();
        String status = term.getTerm_status();
        Date sDate = term.getTerm_start();
        Date eDate = term.getTerm_end();
        String startDate = sdf.format(sDate);
        String endDate = sdf.format(eDate);

        termName.setText(name);
        termStatusSpinner.setSelection(getIndex(termStatusSpinner,status));
        termStart.setText(startDate);
        termEnd.setText(endDate);
    }

    private Term onUpdateTerm() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String name = termName.getText().toString();
        String startDate = termStart.getText().toString();
        String endDate = termEnd.getText().toString();
        Date startDateDate = sdf.parse(startDate);
        Date endDateDate = sdf.parse(endDate);

        if(name.trim().isEmpty()){
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
        term.setTerm_id(termId);
        term.setTerm_name(name);
        term.setTerm_status(selectedStatus);
        term.setTerm_start(startDateDate);
        term.setTerm_end(endDateDate);
        db.termDao().updateTerm(term);
        Toast.makeText(this,"Term has been updated",Toast.LENGTH_SHORT).show();
        updateSuccessful = true;

        return term;

    }
}