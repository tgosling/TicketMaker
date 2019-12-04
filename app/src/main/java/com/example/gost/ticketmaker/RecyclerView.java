package com.example.gost.ticketmaker;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RecyclerView extends AppCompatActivity{

    Context context;
    androidx.recyclerview.widget.RecyclerView recyclerView;
    LinearLayout linearLayout;
    androidx.recyclerview.widget.RecyclerView.Adapter recyclerViewAdapter;
    LinearLayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        context = getApplicationContext();
        linearLayout = findViewById(R.id.linearLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                recyclerViewLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        String temp = "";

        try
        {
            FileInputStream fin = openFileInput("tickets.csv");
            InputStreamReader isr = new InputStreamReader(fin);
            String holder = "";

            int data = isr.read();

            while (data != -1) {
                holder+= (char) data;
                data = isr.read();
            }

            if(holder != "") {

                temp = holder;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        String ticketsWithHeader = "ID,Date,Time,License Plate,Provence,Vehicle,Infringement|" + temp;

        String[] tickets = ticketsWithHeader.split("\\|");



        recyclerViewAdapter = new MyAdapter(getApplicationContext(), tickets);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}