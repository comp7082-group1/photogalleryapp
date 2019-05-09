package com.example.photogalleryapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView fromDate;
    private TextView toDate;
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private DatePickerDialog.OnDateSetListener fromListener;
    private DatePickerDialog.OnDateSetListener toListener;
    private boolean isStartDate = false;
    private TextView keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fromDate = findViewById(R.id.search_fromDate);
        toDate = findViewById(R.id.search_toDate);
        keyword = findViewById(R.id.search_keywordText);


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                showDateFragment();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                showDateFragment();
            }
        });
    }

    private void showDateFragment(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        if(isStartDate) {
            fromDate.setText(currentDateString);
            fromCalendar = calendar;
        } else {
            toDate.setText(currentDateString);
            toCalendar = calendar;
        }
    }

    public void cancel(View v) {
        finish();
    }

    public void search(View v) {
        Intent i = new Intent();
        i.putExtra("STARTDATE", new SimpleDateFormat("yyyyMMdd").format(fromCalendar.getTime()));
        i.putExtra("ENDDATE", new SimpleDateFormat("yyyyMMdd").format(toCalendar.getTime()));
        i.putExtra("KEYWORD", keyword.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }
}
