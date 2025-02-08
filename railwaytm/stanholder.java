package com.bbstudios.railwaytm;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class stanholder extends RecyclerView.ViewHolder {
    RelativeLayout relativeLayout;
    TextView textView;
    public stanholder(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.stanady);
        relativeLayout=itemView.findViewById(R.id.backreal);

    }
}
