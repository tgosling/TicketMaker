package com.example.gost.ticketmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class ViewTicketsActivity extends AppCompatActivity {

    private DBAdapter db;


    Context context;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    ArrayList<String> tickets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tickets = new ArrayList<String>();
        //get the existing db file or from assets folder
        db = new DBAdapter(this);
        try{
            String destPath = "/data/data" + getPackageName() + "/database";
            File f = new File(destPath);
            if(!f.exists()){
                f.mkdirs();
                f.createNewFile();
                //copy from the db from the assets folder
                CopyDB(getBaseContext().getAssets().open("ticketdb"), new FileOutputStream(destPath + "/TicketDB"));
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //get all tickets
        Cursor c;
        db.open();
        c = db.getAllTickets();

        context = getApplicationContext();
        linearLayout = findViewById(R.id.LinearLayout);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new ViewAdapter(getApplicationContext(), tickets);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        // COpy one byte at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();  // close streams
        outputStream.close();
    }

    public void onBackToMainClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
