package com.example.dell.eatit.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.eatit.Interface.Itemclicklistner;
import com.example.dell.eatit.R;

/**
 * Created by dell on 1/24/2018.
 */

public class sublistviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView cardimg;
    public TextView cardtext;
    private Itemclicklistner itemclicklistner;
    public void setItemclicklistner(Itemclicklistner itemclicklistner) {
        this.itemclicklistner = itemclicklistner;
    }
    public sublistviewholder(View itemView) {
        super(itemView);
        cardimg = (ImageView) itemView.findViewById(R.id.cardimg);
        cardtext = (TextView) itemView.findViewById(R.id.cardtext);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemclicklistner.onClick(v,getAdapterPosition(),false);
    }
}

