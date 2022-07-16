package com.example.bookofmormondaily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.theme.MaterialComponentsViewInflater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView day;
    private TextView verse;
    private TextView text;
    private Button button;
    LocalDate date;

    DatePickerDialog DateDialog;

    public static final String[] MONTHS = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October",
            "November", "December"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        day = findViewById(R.id.day);
        date = LocalDate.now();
        day.setText(date.getMonth().toString() + " " + date.getDayOfMonth());

        Verses verses = new Verses();
        int month = date.getMonthValue();
        String[] scripture;
        verse = findViewById(R.id.verse);
        text = findViewById(R.id.text);

        scripture = verses.getScripture(month-1, date.getDayOfMonth());
        verse.setText(scripture[0]);
        text.setText(scripture[1]);

        LocalDate ld = LocalDate.now();
        button = findViewById(R.id.change_day);
        button.setOnClickListener(view -> {
            DateDialog = new DatePickerDialog(MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            day.setText(MONTHS[month] + " " + dayOfMonth);
                            String[] scripture;
                            scripture = verses.getScripture(month,dayOfMonth);
                            verse.setText(scripture[0]);
                            text.setText(scripture[1]);
                        }
                    }, 2022, ld.getMonthValue() - 1, ld.getDayOfMonth());
            DateDialog.show();
        });
    }
}