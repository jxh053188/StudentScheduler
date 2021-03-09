package com.example.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentscheduler.Entities.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID ORDER BY course_id")
    List<Course> getCourseList(int termID);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID and course_id = :courseID")
    Course getCourse(int termID, int courseID);

    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course_table WHERE course_id = :courseID")
    Course getCurrentCourse(int courseID);

    @Insert
    void insertCourse(Course course);

    @Insert
    void insertAllCourses(Course... course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM course_table")
    public void nukeCourseTable();


}

