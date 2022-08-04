package com.example.bookofmormondaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {

    private TextView day;
    private TextView verse;
    private TextView text;
    int dayofmonth;
    LocalDate date;
    ImageButton imageButton;

    DatePickerDialog DateDialog;

    DatabaseReference databaseReference;



    public static final String[] MONTHS = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October",
            "November", "December"};


    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance("https://bomdaily-b0aea-default-rtdb.firebaseio.com/").getReference();

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
        fab.setOnClickListener(view -> {
            DateDialog = new DatePickerDialog(MainActivity.this,
                    (view1, year, month1, dayOfMonth) -> {
                        day.setText(MONTHS[month1] + " " + dayOfMonth);
                        String[] scripture1;
                        scripture1 = verses.getScripture(month1,dayOfMonth);
                        verse.setText(scripture1[0]);
                        text.setText(scripture1[1]);
                        finalMonth[0] = month1;
                        dayofmonth = dayOfMonth;
                    }, 2022, finalMonth[0], dayofmonth);
            DateDialog.show();
        });

        imageButton = findViewById(R.id.feedbackButton);


        imageButton.setOnClickListener(v ->{
            @SuppressLint("InflateParams") View popupView = getLayoutInflater().inflate(R.layout.feedback, null);

            EditText feedback = popupView.findViewById(R.id.feedback_text);
            Button submit;
            submit = popupView.findViewById(R.id.submit);

            Button cancel;
            cancel = popupView.findViewById(R.id.cancel);

            PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0,-20);

            submit.setOnClickListener(w ->{
                LocalDateTime localDateTime = LocalDateTime.now();
                String feedbackString = feedback.getText().toString();
                localDateTime = localDateTime.truncatedTo(ChronoUnit.SECONDS);
                String localdate = localDateTime.toString();
                if(TextUtils.isEmpty(feedbackString)) {
                    Toast.makeText(this, "Please fill out the text box.", Toast.LENGTH_LONG).show();
                }
                else{
                    databaseReference.child("Feedback").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("Feedback").child(localdate).setValue(feedbackString);
                                Toast.makeText(MainActivity.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Failed to send feedback " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });
            cancel.setOnClickListener(w -> popupWindow.dismiss());
        });


    }
}