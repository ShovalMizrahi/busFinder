package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {


    EditText etPasswordLogin, etUsernameLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();


        FireBase.retrieveUsers();

        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
    }

    public void login(View view) {

        String password = etPasswordLogin.getText().toString();
        String username = etUsernameLogin.getText().toString();
        if (password.equals("") || username.equals("")) {
            Toast.makeText(this, "one or more from the deatils is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

      
        if (FireBase.isCorrentLogIn(username, password)) {
            Toast.makeText(this, "logged in successfully!", Toast.LENGTH_SHORT).show();

            User.login(this, username);



            FireBase.retrieveFavoriteStations(User.getCurrentUsername());
            FireBase.retrieveFavoriteLines(User.getCurrentUsername());


            Intent intent = new Intent(this, MenuActivity.class);
            finish();
            startActivity(intent);

        } else {

            Toast.makeText(this, "wrong details", Toast.LENGTH_SHORT).show();
        }


    }



}