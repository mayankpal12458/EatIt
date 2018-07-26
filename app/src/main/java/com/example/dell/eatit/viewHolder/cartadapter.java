package com.example.dell.eatit.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.Interface.Itemclicklistner;
import com.example.dell.eatit.Order;
import com.example.dell.eatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 1/26/2018.
 */


class cartviewholder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView cart_name,cart_price;
    //public ImageView cart_img;
    private Itemclicklistner itemclicklistner;
    public void setCart_name(TextView cart_name){
        this.cart_name=cart_name;
    }
    public cartviewholder(View itemView) {
        super(itemView);
        cart_name=(TextView)itemView.findViewById(R.id.cart_name);
        //cart_img=(ImageView)itemView.findViewById(R.id.cart_img);
        cart_price=(TextView)itemView.findViewById(R.id.cart_price);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

public class cartadapter extends RecyclerView.Adapter<cartviewholder>{
    List<Order> orders=new ArrayList<>();
    Context context;

    public cartadapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public cartviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row,parent,false);
        cartviewholder hldr=new cartviewholder(view);
        return hldr;
    }

    @Override
    public void onBindViewHolder(cartviewholder holder, int position) {
        TextDrawable drawable=TextDrawable.builder()
                .buildRound(""+orders.get(position).getQuantitiy(), Color.RED);
        //holder.cart_img.setImageDrawable(drawable);
        Locale locale=new Locale("en","US");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(orders.get(position).getPrice()))*(Integer.parseInt(orders.get(position).getQuantitiy()));
        holder.cart_price.setText(numberFormat.format(price));
        holder.cart_name.setText(orders.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
