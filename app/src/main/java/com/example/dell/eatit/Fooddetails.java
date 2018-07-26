package com.example.dell.eatit;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.Database.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fooddetails extends AppCompatActivity {
    TextView food_name,food_price,food_des;
    ImageView collapsingimg;
    CollapsingToolbarLayout collapsingbar;
    ElegantNumberButton number_button;
    String food_id="";
    FirebaseDatabase database;
    DatabaseReference myref;
    FloatingActionButton fab2;
    Modelsublist modellist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetails);
        fab2=(FloatingActionButton)findViewById(R.id.fab2);


        database=FirebaseDatabase.getInstance();
        myref=database.getReference().child("/Food");
        food_name=(TextView)findViewById(R.id.food_name);
        food_price=(TextView)findViewById(R.id.food_price);
        food_des=(TextView)findViewById(R.id.food_des);
        collapsingbar=(CollapsingToolbarLayout)findViewById(R.id.collapsingbar);
        collapsingbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingimg=(ImageView)findViewById(R.id.collapsingimg);
        number_button=(ElegantNumberButton)findViewById(R.id.number_button);

        if(getIntent()!=null)
        {
            food_id=getIntent().getStringExtra("Food_Id");
        }
        if(!food_id.isEmpty())
        {
            if(Common.isConnection(getBaseContext())==true){
            loadfooddetails(food_id);}
            else{
                Toast.makeText(getApplicationContext(),"Please check your Connection.....",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(food_id,modellist.getName(),number_button.getNumber(),modellist.getPrice(),modellist.getDiscount()));

                Toast.makeText(getApplicationContext(),"ADDED TO CART",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadfooddetails(String food_id) {
        myref.child(food_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modellist=dataSnapshot.getValue(Modelsublist.class);
                Picasso.with(getBaseContext()).load(modellist.getImage()).into(collapsingimg);
                collapsingbar.setTitle(modellist.getName());
                food_name.setText(modellist.getName());
                food_price.setText(modellist.getPrice());
                food_des.setText(modellist.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}


