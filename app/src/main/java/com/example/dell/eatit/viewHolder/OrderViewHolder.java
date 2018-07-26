package com.example.dell.eatit.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.eatit.Interface.Itemclicklistner;
import com.example.dell.eatit.R;

/**
 * Created by dell on 1/29/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView order_id,order_status,order_phone,order_address;
    private Itemclicklistner itemclicklistner;

    public OrderViewHolder(View itemView) {
        super(itemView);
        order_id=(TextView)itemView.findViewById(R.id.order_id);
        order_status=(TextView)itemView.findViewById(R.id.order_status);
        order_phone=(TextView)itemView.findViewById(R.id.order_phone);
        order_address=(TextView)itemView.findViewById(R.id.order_address);
        itemView.setOnClickListener(this);
    }

    public void setItemclicklistner(Itemclicklistner itemclicklistner) {
        this.itemclicklistner = itemclicklistner;
    }

    @Override
    public void onClick(View v) {
        itemclicklistner.onClick(v,getAdapterPosition(),false);

    }
}
