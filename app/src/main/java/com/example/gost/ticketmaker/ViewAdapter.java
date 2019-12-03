package com.example.gost.ticketmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
//    String[] SubjectValues;
    ArrayList<String> SubjectValues;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;

    public ViewAdapter(Context context1, ArrayList<String> SubjectValues1){

        SubjectValues = SubjectValues1;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ViewHolder(View v){

            super(v);

            //textView = (TextView)v.findViewById(R.id.textViewName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        //view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout,parent,false);
        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        holder.textView.setText(SubjectValues.get(position));
    }

    @Override
    public int getItemCount(){

        return SubjectValues.size();
    }
}

