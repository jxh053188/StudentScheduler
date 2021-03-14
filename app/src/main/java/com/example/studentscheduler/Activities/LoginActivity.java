package com.example.studentscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscheduler.Data.AppDatabase;
import com.example.studentscheduler.Data.PopulateDatabase;
import com.example.studentscheduler.Entities.User;
import com.example.studentscheduler.R;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submitButton;
    private Button createUserButton;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = AppDatabase.getInstance(getApplicationContext());
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);
        submitButton = findViewById(R.id.loginButton);
        createUserButton = findViewById(R.id.createUser);

        if(!db.userDao().getUsersList().isEmpty()){
            createUserButton.setVisibility(View.GONE);
        }

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name = username.getText().toString();
               String pw = password.getText().toString();

               if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                   Toast toast = Toast.makeText(LoginActivity.this, "Please enter username and password.", Toast.LENGTH_LONG);
                   toast.show();
               } else {
                   User newUser = new User();
                   newUser.setUsername(name);
                   newUser.setPassword(pw);
                   db.userDao().insertUser(newUser);
                   Toast toast = Toast.makeText(LoginActivity.this, "User created", Toast.LENGTH_SHORT);
                   toast.show();
                   finish();
                   startActivity(getIntent());
               }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().isEmpty() || username.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG);
                    toast.show();
                } else {

                if (!loginCheck()) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG);
                    toast.show();

                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.populateDBMenu:
                PopulateDatabase populateDatabase = new PopulateDatabase();
                populateDatabase.populate(getApplicationContext());
                return true;
            case R.id.resetDBMenu:
                db.clearAllTables();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean loginCheck(){
        String inputName = username.getText().toString();
        String inputPassword = password.getText().toString();

        User loginUser = new User();
        loginUser = db.userDao().getUser(inputName);
        String savedPassword = loginUser.getPassword();

        if(savedPassword.equals(inputPassword))
            return true;
        else{
            Toast toast = Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }


}