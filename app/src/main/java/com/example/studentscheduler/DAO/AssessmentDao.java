package com.example.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentscheduler.Entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id")
    List<Assessment> getAssessmentList(int courseID);

    @Query("Select * from assessment_table WHERE assessment_id = :assessmentID")
    Assessment getSingleAssessment(int assessmentID);

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id DESC LIMIT 1")
    Assessment getCurrentAssessment(int courseID);

    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertAllAssessments(Assessment... assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    public void nukeAssessmentTable();


}
