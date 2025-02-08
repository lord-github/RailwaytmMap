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

public class welAda extends RecyclerView.Adapter<gysgaholder> {
    List<wels> wels;
    Context context;
    int index=0;

    public welAda(List<com.bbstudios.railwaytm.wels> wels, Context context) {
        this.wels = wels;
        this.context = context;
    }

    @NonNull
    @Override
    public gysgaholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new gysgaholder(LayoutInflater.from(context).inflate(R.layout.welayat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull gysgaholder holder, int position) {
        holder.ady.setText(wels.get(position).doly);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                notifyDataSetChanged();
            }
        });
        if (index==position){
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.wel));
            welayat(wels.get(position).gysga);
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.cal));
        }
    }

    @Override
    public int getItemCount() {
        return wels.size();
    }

    public void welayat(String ady){
        Intent intent=new Intent("custom-welayat");
        intent.putExtra("ady",ady);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
