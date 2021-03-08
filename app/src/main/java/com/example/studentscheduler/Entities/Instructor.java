package com.example.studentscheduler.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "instructor_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = CASCADE
        )
)

public class Instructor {
    @PrimaryKey(autoGenerate = true)
    private int instructor_id;
    @ColumnInfo(name = "course_id_fk")
    private int course_id_fk;
    @ColumnInfo(name = "instructor_name")
    private String instructor_name;
    @ColumnInfo(name = "instructor_phone")
    private String instructor_phone;
    @ColumnInfo(name = "instructor_email")
    private String instructor_email;


    public Instructor(int instructor_id,
                      int course_id_fk,
                      String instructor_name,
                      String instructor_phone,
                      String instructor_email) {
        this.instructor_id = instructor_id;
        this.course_id_fk = course_id_fk;
        this.instructor_name = instructor_name;
        this.instructor_phone = instructor_phone;
        this.instructor_email = instructor_email;
    }

    public Instructor() {

    }

    public int getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(int mentor_id) {
        this.instructor_id = mentor_id;
    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getInstructor_phone() {
        return instructor_phone;
    }

    public void setInstructor_phone(String instructor_phone) {
        this.instructor_phone = instructor_phone;
    }

    public String getInstructor_email() {
        return instructor_email;
    }

    public void setInstructor_email(String instructor_email) {
        this.instructor_email = instructor_email;
    }

    @Override
    public String toString(){
        return getInstructor_name();
    }
}