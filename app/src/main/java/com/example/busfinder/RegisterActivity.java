package com.example.busfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText date;
    Date dateDialog;
    EditText etUserMailRegister, etUserPhoneRegister, etUserPasswordRegister, etUserNameRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        FireBase.retrieveUsers();

        date = findViewById(R.id.etUserDateRegister);
        etUserMailRegister = findViewById(R.id.etUserMailRegister);
        etUserPhoneRegister = findViewById(R.id.etUserPhoneRegister);
        etUserPasswordRegister = findViewById(R.id.etUserPasswordRegister);
        etUserNameRegister = findViewById(R.id.etUserNameRegister);


    }

    public void datePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.show();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dateDialog = datePickerDialog.getDate();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                date.setText(formatter.format(dateDialog));
            }
        });
    }


    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }


    public void register(View view) {


        if (!patternMatches(etUserNameRegister.getText().toString(), "^.{3}(.+)")) {
            Toast.makeText(this, "username provided has to consist at least 4 chars!", Toast.LENGTH_SHORT).show();
            return;
        }



        if (!patternMatches(etUserMailRegister.getText().toString(), "^(.+)@(.+)\\.com$")) {
            Toast.makeText(this, "invalid mail address", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!patternMatches(etUserPhoneRegister.getText().toString(), "^\\d{10}$")) {
            Toast.makeText(this, "invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!patternMatches(etUserPhoneRegister.getText().toString(), "^\\d{10}$")) {
            Toast.makeText(this, "invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!patternMatches(etUserPasswordRegister.getText().toString(), "^.{5}(.+)$")) {
            Toast.makeText(this, "password has to be at least 6 chars", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.getText().toString().equals("")) {
            Toast.makeText(this, "missing datebirth!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FireBase.isUsernameFree(etUserNameRegister.getText().toString()))
        FireBase.registerUser(etUserNameRegister.getText().toString(), etUserPasswordRegister.getText().toString(), etUserMailRegister.getText().toString(),dateDialog , etUserPhoneRegister.getText().toString());
        else
            Toast.makeText(this, "the username has been used already. think about another one!", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "your registeration succeeded! plesae long in!", Toast.LENGTH_LONG).show();
        finish();
    }



}