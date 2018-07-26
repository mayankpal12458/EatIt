package com.example.dell.eatit;

import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.Database.Database;
import com.example.dell.eatit.Interface.Itemclicklistner;
import com.example.dell.eatit.Service.Notificationlisten;
import com.example.dell.eatit.viewHolder.mainlistviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Listfoods extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rv;
    List<Order> list=new ArrayList<>();
    GridLayoutManager layoutManager;
    DatabaseReference myref;
    DrawerLayout mydrawer;
    ActionBarDrawerToggle mtoggle;
    NavigationView mynavigation;
    FloatingActionButton fab;
    FirebaseRecyclerAdapter<Modellist,mainlistviewholder> adapter;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfoods);

        swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.colorPrimary);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Common.isConnection(getBaseContext())==true) {

                    loadmenu();
                }else{
                    Toast.makeText(getApplicationContext(),"Please check your Connection.....",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        swipe.post(new Runnable() {
            @Override
            public void run() {
                if(Common.isConnection(getBaseContext())==true) {

                    loadmenu();
                }else{
                    Toast.makeText(getApplicationContext(),"Please check your Connection.....",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Listfoods.this,Cart.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
            }
        });
        list=new Database(this).getCarts();

        mydrawer = (DrawerLayout) findViewById(R.id.mydrawer);
        mtoggle = new ActionBarDrawerToggle(this, mydrawer, R.string.open, R.string.close);
        mydrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mynavigation = (NavigationView) findViewById(R.id.mynavigation);
        mynavigation.bringToFront();
        mynavigation.setNavigationItemSelectedListener(Listfoods.this);

        myref = FirebaseDatabase.getInstance().getReference().child("/Category");
        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new GridLayoutManager(this,2);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(rv.getContext(),R.anim.layout_fall_down);
        rv.setLayoutAnimation(controller);
        if(Common.isConnection(getBaseContext())==true) {

            loadmenu();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your Connection.....",Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent=new Intent(Listfoods.this, Notificationlisten.class);
        startService(intent);
    }

    private void loadmenu() {
        adapter = new FirebaseRecyclerAdapter<Modellist, mainlistviewholder>(
                Modellist.class,
                R.layout.custom_row,
                mainlistviewholder.class,
                myref
        ) {
            @Override
            protected void populateViewHolder(mainlistviewholder viewHolder, Modellist model, int position) {
                viewHolder.cardtextmain.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.cardimgmain);
                Modellist clickitem = model;
                viewHolder.setItemclicklistner(new Itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean islongpressed) {
                        Intent intent = new Intent(Listfoods.this, listcategory.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });

            }
        };
        rv.setAdapter(adapter);
        rv.getAdapter().notifyDataSetChanged();
        rv.scheduleLayoutAnimation();
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_signout){
            new Database(this).CleanCart();
            Intent intent1=new Intent(Listfoods.this,SignIn.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);

        }
        if(id==R.id.nav_cart){
            Intent intent1=new Intent(Listfoods.this,Cart.class);
            startActivity(intent1);
        }
        if(id==R.id.nav_order){
            Intent mintent=new Intent(Listfoods.this,OrderStatus.class);
            startActivity(mintent);
        }
        if(id==R.id.nav_profile){
            Intent mintent=new Intent(Listfoods.this,Profile.class);
            startActivity(mintent);
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(mtoggle.onOptionsItemSelected(item))
            return true;
        if(item.getItemId()==R.id.refresh)
        {
            loadmenu();
        }
        return super.onOptionsItemSelected(item);
    }
}
