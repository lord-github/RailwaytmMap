package com.bbstudios.railwaytm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class sAdapter extends RecyclerView.Adapter<stanholder> {

    List<stansid> stansids;
    Context context;
    int pos=-1;

    public sAdapter(List<stansid> stansids, Context context) {
        this.stansids = stansids;
        this.context = context;
    }

    @NonNull
    @Override
    public stanholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new stanholder(LayoutInflater.from(context).inflate(R.layout.wagon,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull stanholder holder, int position) {
        holder.textView.setText(stansids.get(position).ady);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                notifyDataSetChanged();
            }
        });
        if (pos==position){
    holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.gok));
    addmark(stansids.get(position).ady);
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.cal));
        }
    }

    @Override
    public int getItemCount() {
        return stansids.size();
    }

    public void addmark(String ady){
        Intent intent=new Intent("custom-stan");
        intent.putExtra("ady",ady);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
