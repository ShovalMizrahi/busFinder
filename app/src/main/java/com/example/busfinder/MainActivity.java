package com.example.busfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Runnable {
    Thread t;

    static int amountProgresses = 7;
    static int countProgress = 0;


    Button btLogoutMain, btLoginMain, btRegisterMain, btMenu, btMapMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countProgress = 0;
        new Thread(new ProgressingThread()).start();


        btLogoutMain = findViewById(R.id.btLogoutMain);
        btLoginMain = findViewById(R.id.btLoginMain);
        btRegisterMain = findViewById(R.id.btRegisterMain);
        btMenu = findViewById(R.id.btMenu);
        btMapMain = findViewById(R.id.btMapMain);

        if (User.checkIfLoggedin(this)) {
            logInButtons();


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


        if (User.checkIfLoggedin(this)) {
            logInButtons();
            Toast.makeText(this, "you are logged in!", Toast.LENGTH_SHORT).show();


        }


    }

    public void logout(View view) {

        User.logout(this);

        finish();
        startActivity(getIntent());

    }


    public void logInButtons() {
        btLoginMain.setVisibility(View.GONE);
        btRegisterMain.setVisibility(View.GONE);
        btLogoutMain.setVisibility(View.VISIBLE);
        btMenu.setVisibility(View.VISIBLE);
        btMapMain.setVisibility(View.VISIBLE);


    }


    public void notLoggedInButtons() {
        btLoginMain.setVisibility(View.VISIBLE);
        btRegisterMain.setVisibility(View.VISIBLE);
        btLogoutMain.setVisibility(View.GONE);
        btMenu.setVisibility(View.GONE);
        btMapMain.setVisibility(View.GONE);


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


        success = false;

        while (!success) {

            try {
                restApi.doInBackground("function=showCities");
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
        ArrayListStation.bindStationToCity();


        countProgress++;

    }

    public void map(View view) {

        Toast.makeText(this," "+ RestApi.stations.get(0).getCity(), Toast.LENGTH_SHORT).show();

        if (!t.isAlive()) {
            moveToMapActivity();

        } else {
            Toast.makeText(this, "waiting for stations to load", Toast.LENGTH_SHORT).show();
        }


    }


    void moveToMapActivity()
    {

        Context context =this;

        new CountDownTimer(2 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(context, MapActivity.class);
                startActivity(intent);
            }
        }.start();


    }

    public void back(View view) {
        finish();
    }

    public void menu(View view) {

        if (t.isAlive()) {
            Toast.makeText(this, "waiting for stations to load", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }

    public void linesearch(View view) {
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

                        tVProgress.setText("download data from server: \n" + (int) (100 * ((double) countProgress / (double) amountProgresses)) + "%");


                    }
                });
            }
        }
    }

}