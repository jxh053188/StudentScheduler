package com.example.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentscheduler.Entities.Instructor;

import java.util.List;

@Dao
public interface InstructorDao {
    @Query("SELECT * FROM instructor_table ORDER BY instructor_id")
    List<Instructor> getAllInstructors();

    @Query("SELECT * FROM instructor_table WHERE course_id_fk = :courseID ORDER BY instructor_id")
    List<Instructor> getInstructorList(int courseID);

    @Query("SELECT * FROM instructor_table WHERE instructor_id = :instructorId")
    Instructor getInstructorDetail(int instructorId);

    @Insert
    void insertInstructor(Instructor instructor);

    @Insert
    void insertAllInstructors(Instructor... instructors);

    @Update
    void updateInstructor(Instructor instructor);

    @Delete
    void deleteInstructor (Instructor instructor);

    @Query("DELETE FROM instructor_table")
    public void nukeInstructorTable();

}
