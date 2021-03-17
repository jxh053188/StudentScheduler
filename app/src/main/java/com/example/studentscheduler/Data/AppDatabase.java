package com.example.studentscheduler.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.studentscheduler.DAO.AssessmentDao;
import com.example.studentscheduler.DAO.CourseDao;
import com.example.studentscheduler.DAO.InstructorDao;
import com.example.studentscheduler.DAO.NoteDao;
import com.example.studentscheduler.DAO.TermDao;
import com.example.studentscheduler.DAO.UserDao;
import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.Entities.Term;
import com.example.studentscheduler.Entities.User;

@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class, Note.class, User.class}, exportSchema = false, version = 6)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "scheduler_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract InstructorDao instructorDao();
    public abstract NoteDao noteDao();
    public abstract UserDao userDao();

}

