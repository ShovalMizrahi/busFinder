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

    private static ArrayList<User> users = new ArrayList<User>();

    public static  void registerUser(String username, String password, String mail, Date date, String phone)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        user.put("mail", mail);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        user.put("birthDate",formatter.format(date));
        user.put("phone",phone);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });



    }







    public static void retrieveUsers()
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Boolean result [] ={true};

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public   void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User((String)document.getData().get("username"), (String)document.getData().get("password"));
                                users.add( user );

                            }
                        } else {
                            Log.w("print it", "Error getting documents.", task.getException());
                            notifyAll();
                        }
                    }
                });
    }

     public static boolean isUsernameFree(String username) {
        for(int i=0; i<users.size() ;i++)
        {
            if(users.get(i).getUsername().equals(username))
                return false;
        }

        return true;

    }


    public static boolean isCorrentLogIn(String username,String password) {
        for(int i=0; i<users.size() ;i++)
        {
            if(users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password) )
                return true;
        }

        return false;

    }



}
