package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();


        about();

    }


    void about() {
        TextView tVabout = findViewById(R.id.tVAbout);
        tVabout.setLines(20);

        String about = "released: 28/09/2022.\n\n";
        about+=("This app is made by:\n");
        about+=("Almog Shaby:\nmaralmog8@gmail.com\n\n" + "Shoval Mizrahi:\nshovalmizrahi62@gmail.com\n\n");
        about+=("As part of a final project for a bachelor's degree in computer science.\n");
        about+=("The project was done in the Java language as part of the course number: 20503.\n At the Open University directed by Tamar Benaya.");

        tVabout.setText(about);
    }
}