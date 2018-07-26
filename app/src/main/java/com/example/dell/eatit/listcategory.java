package com.example.dell.eatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.Interface.Itemclicklistner;
import com.example.dell.eatit.viewHolder.sublistviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class listcategory extends AppCompatActivity {

    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference myref;
    String categoryId="";
    FirebaseRecyclerAdapter<Modelsublist,sublistviewholder> adapter;
    FirebaseRecyclerAdapter<Modelsublist,sublistviewholder> Searchadapter;
    List<String> suggestions=new ArrayList<>();
    MaterialSearchBar searchbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcategory);

        rv=(RecyclerView)findViewById(R.id.rv);
        layoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        myref= FirebaseDatabase.getInstance().getReference().child("/Food");
        if(getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            if(Common.isConnection(getBaseContext())==true){
            loadlistFood(categoryId);}
            else{
                Toast.makeText(getApplicationContext(),"Please check your Connection.....",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        searchbar =(MaterialSearchBar)findViewById(R.id.searchbar);
        searchbar.setHint("Enter Your Food");
        loadsuggestions();
        searchbar.setLastSuggestions(suggestions);
        searchbar.setCardViewElevation(10);
        searchbar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest=new ArrayList<String>();
                for(String search:suggestions){
                    if(search.toLowerCase().contains(searchbar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchbar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    rv.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        Searchadapter =new FirebaseRecyclerAdapter<Modelsublist, sublistviewholder>(
                Modelsublist.class,
                R.layout.custom_subrow,
                sublistviewholder.class,
                myref.orderByChild("Name").equalTo(text.toString())

        ) {
            @Override
            protected void populateViewHolder(sublistviewholder viewHolder, Modelsublist model, int position) {
                viewHolder.cardtext.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.cardimg);
                final Modelsublist clickitem=model;
                viewHolder.setItemclicklistner(new Itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean islongpressed) {
                        Intent intent = new Intent(listcategory.this, Fooddetails.class);
                        intent.putExtra("Food_Id", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });


            }
        };
        rv.setAdapter(Searchadapter);
    }

    private void loadsuggestions() {
        myref.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Modelsublist model=snapshot.getValue(Modelsublist.class);
                    suggestions.add(model.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadlistFood(String categoryId) {

         adapter=new FirebaseRecyclerAdapter<Modelsublist, sublistviewholder>(
                Modelsublist.class,
                R.layout.custom_subrow,
                sublistviewholder.class,
                myref.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(sublistviewholder viewHolder, Modelsublist model, int position) {
               viewHolder.cardtext.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.cardimg);
                final Modelsublist clickitem=model;
                viewHolder.setItemclicklistner(new Itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean islongpressed) {
                        Intent intent=new Intent(listcategory.this,Fooddetails.class);
                        intent.putExtra("Food_Id",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        rv.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if(item.getItemId()==R.id.refresh)
        {
            loadlistFood(categoryId);
        }
        return super.onOptionsItemSelected(item);
    }
}




