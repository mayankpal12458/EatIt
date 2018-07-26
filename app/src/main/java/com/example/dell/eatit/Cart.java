package com.example.dell.eatit;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.service.voice.VoiceInteractionSession;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.Database.Database;
import com.example.dell.eatit.viewHolder.cartadapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference myref;
    TextView total;
    FButton fbtncart;
    FirebaseDatabase database;
    List<Order> list=new ArrayList<>();
    cartadapter cart;
   // String status="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database=FirebaseDatabase.getInstance();
        myref=database.getReference();
        myref.child("Requests");
        rv=(RecyclerView)findViewById(R.id.rv);
        total=(TextView)findViewById(R.id.total);
        fbtncart=(FButton)findViewById(R.id.fbtncart);
        layoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        loadlistfoods();

        fbtncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()>0) {
                    showalertdialog();
                }else{
                    Toast.makeText(getApplicationContext(),"Your Cart is Empty!!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadlistfoods() {

        list=new Database(this).getCarts();
        cart=new cartadapter(list,this);
        cart.notifyDataSetChanged();
        rv.setAdapter(cart);


        //Calculate total
        int totalnew=0;
        for(Order order:list)
        {

            totalnew+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantitiy()));
        }
        Locale locale=new Locale("en","US");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        total.setText(numberFormat.format(totalnew));
    }

    private void showalertdialog() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(Cart.this);
        alertdialog.setTitle("ONE MORE STEP");
        alertdialog.setMessage("Enter Address and Phone");
        LinearLayout layout=new LinearLayout(Cart.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText alertadd=new EditText(Cart.this);
        layout.addView(alertadd);
        alertadd.setHint("Address");
        final EditText alertph=new EditText(Cart.this);
        layout.addView(alertph);
        alertph.setHint("MOBILE");
        final EditText alertname=new EditText(Cart.this);
        layout.addView(alertname);
        alertname.setHint("NAME");
        alertdialog.setView(layout);
        alertdialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modelRequest mr=new modelRequest(
                        alertph.getText().toString(),
                        alertadd.getText().toString(),
                        total.getText().toString(),
                        alertname.getText().toString()
                        //status

                );

                myref.child("Requests").push().setValue(mr);
                new Database(getBaseContext()).CleanCart();
                Toast.makeText(Cart.this,"THANK YOU...ORDER PLACED",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();




    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.DELETE))
        {
            deleteCart(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCart(int position) {
        list.remove(position);
        new Database(this).CleanCart();

        for(Order item:list)
        {
            new Database(this).addToCart(item);
        }
        loadlistfoods();

    }
}
