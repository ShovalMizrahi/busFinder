package com.example.busfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Runnable {

    Button btLogoutMain, btLoginMain, btRegisterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btLogoutMain = findViewById(R.id.btLogoutMain);
        btLoginMain = findViewById(R.id.btLoginMain);
        btRegisterMain = findViewById(R.id.btRegisterMain);


        if (checkIfLoggedin()) {
            logInButtons();

        } else {

            notLoggedInButtons();
        }


        MainActivity mainActivity = new MainActivity();
        Thread t = new Thread(mainActivity);
        t.start();

    }

    public void register(View view) {

        /*
        if(RestApi.stations.size()>0)
        Toast.makeText(this, RestApi.stations.get(0).getName() + "", Toast.LENGTH_SHORT).show();
*/

        if (RestApi.lines.size() > 0)
            Toast.makeText(this, RestApi.stations.get(0).getName() + "", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void login(View view) {


        Intent intent = new Intent(this, LogInActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (checkIfLoggedin()) {
            logInButtons();
            Toast.makeText(this, "you are logged in!", Toast.LENGTH_SHORT).show();

        }


    }

    public void logout(View view) {

        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("username", null);
        Ed.putString("password", null);
        Ed.commit();


        finish();
        startActivity(getIntent());

    }


    public boolean checkIfLoggedin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

        String username = sp1.getString("username", null);
        String password = sp1.getString("password", null);

        if (username != null) {
            return true;

        }
        return false;

    }


    public void logInButtons() {
        btLoginMain.setVisibility(View.GONE);
        btRegisterMain.setVisibility(View.GONE);
        btLogoutMain.setVisibility(View.VISIBLE);


    }


    public void notLoggedInButtons() {
        btLoginMain.setVisibility(View.VISIBLE);
        btRegisterMain.setVisibility(View.VISIBLE);
        btLogoutMain.setVisibility(View.GONE);

    }


    @Override
    public void run() {
        RestApi restApi = new RestApi();

        Boolean success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showStations");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (RestApi.stations.size() > 0)
                success = true;

            else {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showLines");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (RestApi.stations.size() > 0)
                success = true;

            else {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showTracks");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (RestApi.stations.size() > 0)
                success = true;

            else {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void map(View view) {
        if(RestApi.stations.size()>0) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "waiting for stations to load", Toast.LENGTH_SHORT).show();
        }

    }

    public void back(View view) {
        finish();
    }
}