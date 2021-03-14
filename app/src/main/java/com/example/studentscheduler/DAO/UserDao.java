package com.example.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentscheduler.Entities.Instructor;
import com.example.studentscheduler.Entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY userId")
    List<User>getUsersList();

    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUser(String username);

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);
}
