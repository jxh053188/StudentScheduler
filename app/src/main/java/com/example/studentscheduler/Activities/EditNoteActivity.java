package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EditNoteActivity extends AppCompatActivity {

    private AppDatabase db;
    private Note note;
    private int noteId;
    private int courseId;
    private EditText noteTitle;
    private EditText noteText;
    private boolean updateSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        db = AppDatabase.getInstance(EditNoteActivity.this);
        Intent intent = getIntent();

        courseId = intent.getIntExtra("courseId",-1);
        noteId = intent.getIntExtra("noteId",-1);
        noteTitle = findViewById(R.id.editNoteTitleInput);
        noteText = findViewById(R.id.editNoteTextInput);

        setNoteInfo();

        Button saveButton = findViewById(R.id.updateNoteButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteTitle.getText().toString().isEmpty() || noteText.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditNoteActivity.this);
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
                        note = onUpdateNote();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (updateSuccessful = true) {
                        Intent intent = new Intent(getApplicationContext(), SingleNoteDetailActivity.class);
                        intent.putExtra("noteId", noteId);
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void setNoteInfo(){
        Note note = new Note();
        note = db.noteDao().getSingleNote(noteId);
        String title = note.getNote_title();
        String text = note.getNote_text();

        noteText.setText(text);
        noteTitle.setText(title);
    }

    private Note onUpdateNote(){
        String title = noteTitle.getText().toString();
        String text = noteText.getText().toString();

        Note note = new Note();
        note.setNote_id(noteId);
        note.setNote_text(text);
        note.setNote_title(title);
        note.setCourse_id_fk(courseId);
        db.noteDao().updateNote(note);
        Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show();
        updateSuccessful = true;

        return note;
    }
}