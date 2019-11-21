package com.example.gost.ticketmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GenerateTicketActivity extends AppCompatActivity {

    EditText timeStamp;
    EditText dateStamp;
    EditText licenseET;
    EditText provET;
    EditText carManET;
    EditText carModET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
        timeStamp = findViewById(R.id.timeET);
        dateStamp = findViewById(R.id.dateET);
        licenseET = findViewById(R.id.plateNum);
        provET = findViewById(R.id.provET);
        carManET = findViewById(R.id.carManET);
        carModET = findViewById(R.id.carModelET);


        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String formatDate = date.format(new Date());
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
        String formatTime = time.format(new Date());
        timeStamp.setText(formatTime);
        dateStamp.setText(formatDate);
        timeStamp.setEnabled(false);
        dateStamp.setEnabled(false);
        licenseET.setEnabled(false);
        carManET.setEnabled(false);
        carModET.setEnabled(false);
        provET.setEnabled(false);

    }

    public void onBackToMainClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onEditTicClick(View view){
        carManET.setEnabled(true);
        carModET.setEnabled(true);
    }
}
