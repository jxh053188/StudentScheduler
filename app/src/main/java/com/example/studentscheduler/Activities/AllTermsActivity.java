package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.R;

import java.util.List;

public class AllTermsActivity extends AppCompatActivity {
    AppDatabase db;
    List<Term> allTerms;
    ListView allTermsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_terms);
        db = AppDatabase.getInstance(getApplicationContext());
        allTermsList = findViewById(R.id.termListView);
        updateAllTermsList();

        allTermsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), SingleTermDetailActivity.class);
            intent.putExtra("termId",allTerms.get(position).getTerm_id());
            startActivity(intent);
        });
    }

    private void updateAllTermsList(){
        List<Term>allTerms = db.termDao().getAllTerms();
        ArrayAdapter<Term> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,allTerms);
        allTermsList.setAdapter(arrayAdapter);
        this.allTerms = allTerms;
        arrayAdapter.notifyDataSetChanged();

    }

    public void onAddTermButton(View view) {
        Intent intent = new Intent(AllTermsActivity.this, AddTermActivity.class);
        startActivity(intent);
    }
}