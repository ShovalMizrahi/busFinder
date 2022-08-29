package com.example.busfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

public class User {

    private String username;
    private String password;

    private static String currentUsername = "";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        User.currentUsername = currentUsername;
    }


    public static void login(Context context, String username) {
        SaveFileXML.saveString(context, "username", username);
        currentUsername = username;
        retreiveInfo();
    }


    public static void logout(Context context) {
        SaveFileXML.saveString(context, "username", null);
        SaveFileXML.saveString(context, "password", null);

        User.setCurrentUsername("");


    }


    public static boolean checkIfLoggedin(Context context) {

        String username = SaveFileXML.getStringValue(context, "username");

        if (username == null)
            return false;

        currentUsername = username;
        retreiveInfo();

        return true;

    }



    public static void retreiveInfo()
    {


        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (FireBase.favoriteStations.size() == 0)
                    FireBase.retrieveFavoriteStations(currentUsername);
                if (FireBase.favoriteLines.size() == 0)
                    FireBase.retrieveFavoriteLines(currentUsername);            }
        }.start();





    }


}
