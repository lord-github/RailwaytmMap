package com.bbstudios.railwaytm;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class gysgaholder extends RecyclerView.ViewHolder {
    TextView ady;
    RelativeLayout relativeLayout;
    public gysgaholder(@NonNull View itemView) {
        super(itemView);
        relativeLayout=itemView.findViewById(R.id.welarka);
        ady=itemView.findViewById(R.id.welyazgy);
    }
}
