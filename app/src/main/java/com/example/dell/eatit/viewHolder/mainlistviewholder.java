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

public class mainlistviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
   public ImageView cardimgmain;
    public TextView cardtextmain;
    private Itemclicklistner itemclicklistner;

    public mainlistviewholder(View itemView) {
        super(itemView);

        cardimgmain=(ImageView)itemView.findViewById(R.id.cardimgmain);
        cardtextmain= (TextView) itemView.findViewById(R.id.cardtextmain);
        itemView.setOnClickListener(this);
    }
    public void setItemclicklistner(Itemclicklistner itemclicklistner){
        this.itemclicklistner=itemclicklistner;
    }

    @Override
    public void onClick(View v) {
        itemclicklistner.onClick(v,getAdapterPosition(),false);

    }
}
