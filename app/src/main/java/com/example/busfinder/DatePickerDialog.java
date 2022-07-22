package com.example.busfinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;

public class DatePickerDialog extends Dialog  implements DatePicker.OnDateChangedListener {


    public static final int AGE_LIMIT = 9;
    public static final int YEAR_DIFF = 1900;

    private Date date ;

    Button buttonDatePicker;
    public DatePickerDialog(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_dialog);

        buttonDatePicker = findViewById(R.id.buttonDatePicker);

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Date date = new Date();

        date.setYear( date.getYear()-AGE_LIMIT  );
        long milliseconds = date.getTime();


        DatePicker datePicker = findViewById(R.id.datePickerRegister);
        datePicker.setMaxDate(milliseconds);

        datePicker.setOnDateChangedListener(this::onDateChanged);


    }


    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        date = new Date(i-YEAR_DIFF, i1, i2);

    }

    public Date getDate() {
        return date;
    }
}

