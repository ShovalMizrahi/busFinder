package com.example.busfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Runnable {
    Thread t;

    static int amountProgresses = 7;
    static int countProgress =  0;


    Button btLogoutMain, btLoginMain, btRegisterMain, btMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new ProgressingThread()).start();


        btLogoutMain = findViewById(R.id.btLogoutMain);
        btLoginMain = findViewById(R.id.btLoginMain);
        btRegisterMain = findViewById(R.id.btRegisterMain);
        btMenu = findViewById(R.id.btMenu);


        if (checkIfLoggedin()) {
            logInButtons();

            SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
            String username = sp1.getString("username", null);

        } else {

            notLoggedInButtons();
        }


        MainActivity mainActivity = new MainActivity();
        t = new Thread(mainActivity);
        t.start();


    }

    public void register(View view) {


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
        btMenu.setVisibility(View.VISIBLE);


    }


    public void notLoggedInButtons() {
        btLoginMain.setVisibility(View.VISIBLE);
        btRegisterMain.setVisibility(View.VISIBLE);
        btLogoutMain.setVisibility(View.GONE);
        btMenu.setVisibility(View.GONE);

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

        countProgress++;

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


        countProgress++;

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

        countProgress++;
        success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showRoutes");
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


        countProgress++;
        success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showCompanies");
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

        countProgress++;
        RestApi.stations.bindLinesToStations();
        countProgress++;
        ArrayListLine.bindLineToCompany();


        countProgress++;

    }

    public void map(View view) {


        if (!t.isAlive()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "waiting for stations to load", Toast.LENGTH_SHORT).show();
        }


    }

    public void back(View view) {
        finish();
    }

    public void menu(View view) {



        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        String username = sp1.getString("username", null);
        if(FireBase.favoriteStations.size()==0)
        FireBase.retrieveFavoriteStations(username);
        if(FireBase.favoriteLines.size()==0)
            FireBase.retrieveFavoriteLines(username);




        //   if(MenuActivity.getLatitude()!=0)
        //  Toast.makeText(this, "the p is "+MenuActivity.getLatitude(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }


    class ProgressingThread implements Runnable {
        @Override
        public void run() {


            TextView tVProgress = findViewById(R.id.tVProgress);
            while (countProgress != 7) {


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tVProgress.setText("download data from server: \n" + (int) (100 * ( (double) countProgress / (double) amountProgresses)) + "%");


                    }
                });
            }
        }
    }

}