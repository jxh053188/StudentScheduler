package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.R;

public class AddNoteActivity extends AppCompatActivity {
    private AppDatabase db;
    private Note note;
    private int courseId;
    private EditText noteTitle;
    private EditText noteText;
    private boolean addSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        db = AppDatabase.getInstance(AddNoteActivity.this);
        Intent intent = new Intent(getIntent());

        courseId = intent.getIntExtra("courseId",-1);
        noteTitle = findViewById(R.id.noteTitleInput);
        noteText = findViewById(R.id.noteTextInput);

        Button saveButton = findViewById(R.id.saveNoteButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    note = onAddNote();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (addSuccessful = true) {
                    Intent intent = new Intent(getApplicationContext(), AllTermsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private Note onAddNote(){
        String title = noteTitle.getText().toString();
        String text = noteText.getText().toString();

        if(title.trim().isEmpty() || text.trim().isEmpty()){
            Toast.makeText(this, "Please enter all fields",Toast.LENGTH_SHORT).show();
            return null;
        }

        Note note = new Note();
        note.setNote_text(text);
        note.setNote_title(title);
        note.setCourse_id_fk(courseId);
        db.noteDao().insertNote(note);
        Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show();
        addSuccessful = true;

        return note;
    }
}