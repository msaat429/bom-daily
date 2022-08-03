package com.example.bookofmormondaily;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private TextView day;
    private TextView verse;
    private TextView text;
    private Button button;
    int month;
    int dayofmonth;
    LocalDate date;
    ImageButton imageButton;

    DatePickerDialog DateDialog;

    public static final String[] MONTHS = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October",
            "November", "December"};


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerOpen(Gravity.LEFT)) {
//                    drawer.closeDrawer(Gravity.LEFT);
//                } else {
//                    drawer.openDrawer(Gravity.LEFT);
//                }
//            }
//        });


        day = findViewById(R.id.day);
        date = LocalDate.now();
        day.setText(date.getMonth().toString() + " " + date.getDayOfMonth());

        Verses verses = new Verses();
        int month = date.getMonthValue();
        String[] scripture;
        verse = findViewById(R.id.verse);
        text = findViewById(R.id.text);
        text.setHyphenationFrequency(100);

        scripture = verses.getScripture(month-1, date.getDayOfMonth());
        verse.setText(scripture[0]);
        text.setText(scripture[1]);

        LocalDate ld = LocalDate.now();
        month = ld.getMonthValue() - 1;
        dayofmonth = ld.getDayOfMonth();
        final int[] finalMonth = {month};
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                day.setText(MONTHS[month] + " " + dayOfMonth);
                                String[] scripture;
                                scripture = verses.getScripture(month,dayOfMonth);
                                verse.setText(scripture[0]);
                                text.setText(scripture[1]);
                                finalMonth[0] = month;
                                dayofmonth = dayOfMonth;
                            }
                        }, 2022, finalMonth[0], dayofmonth);
                DateDialog.show();
            }
        });

        imageButton = findViewById(R.id.feedbackButton);


    }
}