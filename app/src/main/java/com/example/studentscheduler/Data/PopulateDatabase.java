package com.example.studentscheduler.Data;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.Entities.Course;
import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.Note;
import com.example.studentscheduler.Entities.Term;

import java.util.Calendar;
import java.util.List;

public class PopulateDatabase extends AppCompatActivity {
    public String LOG_TAG = "PopData";
    Term tempTerm1 = new Term();
    Term tempTerm2 = new Term();
    Term tempTerm3 = new Term();

    Course tempCourse1 = new Course();
    Course tempCourse2 = new Course();
    Course tempCourse3 = new Course();

    Assessment tempAssessment1 = new Assessment();
    Assessment tempAssessment2 = new Assessment();
    Assessment tempAssessment3 = new Assessment();

    Instructor tempInstructor1 = new Instructor();
    Instructor tempInstructor2 = new Instructor();
    Instructor tempInstructor3 = new Instructor();

    Note tempNote1 = new Note();
    Note tempNote2 = new Note();
    Note tempNote3 = new Note();

    AppDatabase db;

    public void populate(Context context){
        db = AppDatabase.getInstance(context);
        try{
            insertTerms();
            insertCourses();
            insertAssessments();
            insertInstructors();
            insertNotes();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Populate DB Failed");
        }
    }

    private void insertTerms(){
        Calendar start;
        Calendar end;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 1);
        tempTerm1.setTerm_name("Spring 2021");
        tempTerm1.setTerm_start(start.getTime());
        tempTerm1.setTerm_end(end.getTime());
        tempTerm1.setTerm_status("In Progress");

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 2);
        end.add(Calendar.MONTH, 5);
        tempTerm2.setTerm_name("Fall 2021");
        tempTerm2.setTerm_start(start.getTime());
        tempTerm2.setTerm_end(end.getTime());
        tempTerm2.setTerm_status("Pending");

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 6);
        end.add(Calendar.MONTH, 9);
        tempTerm3.setTerm_name("Spring 2022");
        tempTerm3.setTerm_start(start.getTime());
        tempTerm3.setTerm_end(end.getTime());
        tempTerm3.setTerm_status("Pending");

        db.termDao().insertAll(tempTerm1, tempTerm2, tempTerm3);

    }

    private void insertCourses(){
        Calendar start;
        Calendar end;
        List<Term> termList = db.termDao().getTermList();
        if(termList == null) return;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, -1);
        tempCourse1.setCourse_name("History of the Jedi Order");
        tempCourse1.setCourse_start(start.getTime());
        tempCourse1.setCourse_end(end.getTime());
        tempCourse1.setCourse_status("In Progress");
        tempCourse1.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        //end.add(Calendar.MONTH, 1);
        tempCourse2.setCourse_name("Force Push 1");
        tempCourse2.setCourse_start(start.getTime());
        tempCourse2.setCourse_end(end.getTime());
        tempCourse2.setCourse_status("Completed");
        tempCourse2.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        //start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, -1);
        tempCourse3.setCourse_name("Basic Lightsaber Maintenance");
        tempCourse3.setCourse_start(start.getTime());
        tempCourse3.setCourse_end(end.getTime());
        tempCourse3.setCourse_status("Dropped");
        tempCourse3.setTerm_id_fk(termList.get(0).getTerm_id());

        db.courseDao().insertAllCourses(tempCourse1,tempCourse2,tempCourse3);
    }

    private void insertInstructors() {
        List<Term> TermList = db.termDao().getTermList();
        List<Course> CourseList = db.courseDao().getCourseList(TermList.get(0).getTerm_id());

        if (CourseList == null) return;

        tempInstructor1.setInstructor_name("Obi Wan Kenobi");
        tempInstructor1.setInstructor_email("obiwan@jediorder.com");
        tempInstructor1.setInstructor_phone("555-555-5458");
        tempInstructor1.setCourse_id_fk(CourseList.get(0).getCourse_id());

        tempInstructor2.setInstructor_name("Master Yoda");
        tempInstructor2.setInstructor_phone("555-555-5555");
        tempInstructor2.setInstructor_email("yoda@jediorder.com");
        tempInstructor2.setCourse_id_fk(CourseList.get(1).getCourse_id());

        tempInstructor3.setInstructor_name("Luke Skywalker");
        tempInstructor3.setInstructor_phone("555-458-5921");
        tempInstructor3.setInstructor_email("luke@jediorder.com");
        tempInstructor3.setCourse_id_fk(CourseList.get(2).getCourse_id());

        db.instructorDao().insertAllInstructors(tempInstructor1, tempInstructor2, tempInstructor3);
    }

    private void insertAssessments() {
        Calendar start;
        Calendar end;
        List<Term> TermList = db.termDao().getTermList();
        List<Course> CourseList = db.courseDao().getCourseList(TermList.get(0).getTerm_id());
        if (CourseList == null) return;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, -1);
        tempAssessment1.setAssessment_name("Fall of the Sith");
        tempAssessment1.setAssessment_due_date(start.getTime());
        tempAssessment1.setAssessment_type("Objective Assessment");
        tempAssessment1.setCourse_id_fk(CourseList.get(0).getCourse_id());
        tempAssessment1.setAssessment_status("Pending");
        tempAssessment1.setAssessment_score(0);

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        end.add(Calendar.MONTH, 1);
        tempAssessment2.setAssessment_name("Ewok Culture");
        tempAssessment2.setAssessment_due_date(start.getTime());
        tempAssessment2.setAssessment_type("Performance Assessment");
        tempAssessment2.setCourse_id_fk(CourseList.get(1).getCourse_id());
        tempAssessment2.setAssessment_status("Passed");
        tempAssessment2.setAssessment_score(87);

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 2);
        end.add(Calendar.MONTH, 3);
        tempAssessment3.setAssessment_name("Lightsaber Engineering");
        tempAssessment3.setAssessment_due_date(start.getTime());
        tempAssessment3.setAssessment_type("Objective");
        tempAssessment3.setCourse_id_fk(CourseList.get(2).getCourse_id());
        tempAssessment3.setAssessment_status("Failed");
        tempAssessment3.setAssessment_score(45);

        db.assessmentDao().insertAllAssessments(tempAssessment1, tempAssessment2, tempAssessment3);
    }

    private void insertNotes() {
        List<Term> TermList = db.termDao().getTermList();
        List<Course> CourseList = db.courseDao().getCourseList(TermList.get(0).getTerm_id());

        tempNote1.setNote_title("A new hope?");
        tempNote1.setNote_text("This class is boring. You'd think a bunch of magical space knights would be more interesting.");
        tempNote1.setCourse_id_fk(CourseList.get(0).getCourse_id());

        tempNote2.setNote_title("Carrie");
        tempNote2.setNote_text("I can move stuff with my mind...like Carrie.");
        tempNote2.setCourse_id_fk(CourseList.get(1).getCourse_id());

        tempNote3.setNote_title("Failed Exam");
        tempNote3.setNote_text("Will I ever be a real jedi?");
        tempNote3.setCourse_id_fk(CourseList.get(2).getCourse_id());

        db.noteDao().insertAllNotes(tempNote1, tempNote2, tempNote3);
    }
}
