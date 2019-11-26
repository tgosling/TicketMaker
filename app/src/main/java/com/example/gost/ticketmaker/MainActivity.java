package com.example.gost.ticketmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void onGenTickClick(View view){
        Intent intent = new Intent(this, GenerateTicketActivity.class);
        startActivity(intent);
    }

    public void onViewTickClick(View view){
        Intent intent = new Intent(this, ViewTicketsActivity.class);
        startActivity(intent);
    }
}
