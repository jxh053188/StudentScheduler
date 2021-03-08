package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.R;

public class SingleNoteDetailActivity extends AppCompatActivity {
    AppDatabase db;
    int noteId;
    TextView noteTitle;
    TextView noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        noteId = intent.getIntExtra("noteId", -1);
        noteTitle = findViewById(R.id.noteDetailTitle);
        noteText = findViewById(R.id.noteTextDetail);

        setNoteInfo();
    }

    private void setNoteInfo(){
        Note note = new Note();
        note = db.noteDao().getSingleNote(noteId);
        String title = note.getNote_title();
        String text = note.getNote_text();

        noteTitle.setText(title);
        noteText.setText(text);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }
}