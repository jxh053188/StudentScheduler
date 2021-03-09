package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SingleNoteDetailActivity extends AppCompatActivity {
    private AppDatabase db;
    private int noteId;
    private int courseId;
    private TextView noteTitle;
    private TextView noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note_detail);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());

        courseId = intent.getIntExtra("courseId",-1);
        noteId = intent.getIntExtra("noteId", -1);
        noteTitle = findViewById(R.id.noteDetailTitle);
        noteText = findViewById(R.id.noteTextDetail);

        setNoteInfo();
    }

    private void setNoteInfo(){
        Note note = new Note();
        note = db.noteDao().getSingleNote(noteId);
        String title = note.getNote_title().toString();
        String text = note.getNote_text().toString();

        noteTitle.setText(title);
        noteText.setText(text);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subscreen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int optionId = item.getItemId();

        if(optionId == R.id.editItem){
            Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("noteId", noteId);
            startActivity(intent);
            finish();

        }

        if (optionId == R.id.notifyOption){

        }

        if(optionId == R.id.deleteItem){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SingleNoteDetailActivity.this);
            builder.setTitle("Delete Note");
            builder.setMessage("Are you sure you want to delete this note?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Note note = new Note();
                    note = db.noteDao().getSingleNote(noteId);
                    db.noteDao().deleteNote(note);
                    Intent intent = new Intent(SingleNoteDetailActivity.this, AllTermsActivity.class);
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