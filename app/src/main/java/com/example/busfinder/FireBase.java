package com.example.busfinder;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FireBase {

    private static final String FAVORITE_LINE_COLLECTION = "favoriteLines";
    private static final String FAVORITE_STATION_COLLECTION = "favoriteStations";


    private static ArrayList<User> users = new ArrayList<User>();
    // private static  ArrayListStation favoriteSta
    public static ArrayListStation favoriteStations = new ArrayListStation();
    public static ArrayListLine favoriteLines = new ArrayListLine();


    public static void registerUser(String username, String password, String mail, Date date, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        user.put("mail", mail);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        user.put("birthDate", formatter.format(date));
        user.put("phone", phone);


        db.collection("users").document(username).set(user);

    }


    public static void addFavoriteLines(String username, String stationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> station = new HashMap<>();
        station.put("lineId", stationId);

        db.collection("users").document(username).collection("favoriteLines").document(stationId).set(station);

        User.retreiveInfo();
    }


    public static void retrieveFavoriteLines(String username) {

        favoriteLines.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Boolean result[] = {true};

        db.collection("users").document(username).collection("favoriteLines")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String lineId = (String) document.getData().get("lineId");

                                Line line = null;
                                if (lineId != null) {
                                    line = favoriteLines.findLineById(lineId);

                                    if (line != null) {
                                        favoriteLines.add(new Line(line));

                                    }

                                }
                            }
                        } else {
                            Log.w("print it", "Error getting documents.", task.getException());
                            notifyAll();
                        }
                    }
                });
    }


    public static void addFavoriteStation(String username, String stationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> station = new HashMap<>();
        station.put("stationId", stationId);

        db.collection("users").document(username).collection("favoriteStations").document(stationId).set(station);


        User.retreiveInfo();
    }


    public static void retrieveFavoriteStations(String username) {

        favoriteStations.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Boolean result[] = {true};

        db.collection("users").document(username).collection("favoriteStations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String stationId = (String) document.getData().get("stationId");

                                Station station = null;
                                if (stationId != null) {
                                    station = favoriteStations.findStationById(stationId);

                                    if (station != null) {
                                        favoriteStations.add(new Station(station));


                                    }

                                }
                            }
                        } else {
                            Log.w("print it", "Error getting documents.", task.getException());
                            notifyAll();
                        }
                    }
                });
    }

    public static void retrieveUsers() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Boolean result[] = {true};

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User((String) document.getData().get("username"), (String) document.getData().get("password"));
                                users.add(user);

                            }
                        } else {
                            Log.w("print it", "Error getting documents.", task.getException());
                            notifyAll();
                        }
                    }
                });
    }

    public static boolean isUsernameFree(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username))
                return false;
        }

        return true;

    }


    public static boolean isCorrentLogIn(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password))
                return true;
        }

        return false;

    }


    public static void deleteFavoriteStation(String username, String stationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(username).collection("favoriteStations").document(stationId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        User.retreiveInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }



    public static void deleteFavoriteLine(String username, String lineId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(username).collection(FAVORITE_LINE_COLLECTION).document(lineId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        User.retreiveInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}
