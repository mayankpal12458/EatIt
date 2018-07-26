package com.example.dell.eatit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference myref;
    FirebaseRecyclerAdapter<modelRequest,OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        rv=(RecyclerView)findViewById(R.id.rv);
        layoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        myref= FirebaseDatabase.getInstance().getReference("Requests");
         adapter=new FirebaseRecyclerAdapter<modelRequest, OrderViewHolder>(
                modelRequest.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                myref
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, modelRequest model, int position) {
                viewHolder.order_id.setText(adapter.getRef(position).getKey());
                viewHolder.order_address.setText(model.getAddress());
                viewHolder.order_phone.setText(model.getPhone());
                viewHolder.order_status.setText(Common.convertstatus(model.getStatus()));
            }
        };
        rv.setAdapter(adapter);
    }


}
