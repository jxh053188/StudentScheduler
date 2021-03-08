package com.example.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note_table WHERE course_id_fk = :courseID ORDER BY note_id")
    List<Note> getNoteList(int courseID);

    @Query("SELECT * FROM note_table WHERE note_id = :noteID")
    Note getSingleNote(int noteID);

    @Insert
    void insertNote(Note note);

    @Insert
    void insertAllNotes(Note... note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote (Note note);

    @Query("DELETE FROM note_table")
    public void nukeNoteTable();
}
