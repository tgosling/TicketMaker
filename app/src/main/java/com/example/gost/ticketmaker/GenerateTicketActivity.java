package com.example.gost.ticketmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GenerateTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
    }

    public void onBackToMainClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
