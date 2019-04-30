package com.example.photogalleryapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    private EditText fromDate;
    private EditText toDate;
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private DatePickerDialog.OnDateSetListener fromListener;
    private DatePickerDialog.OnDateSetListener toListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void cancel(View v) {
        finish();
    }

    public void search(View v) {
        Intent i = new Intent();
        i.putExtra("STARTDATE", fromDate.getText().toString());
        i.putExtra("ENDDATE", toDate.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }
}
