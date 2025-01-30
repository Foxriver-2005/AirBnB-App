package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Adapters.PaymentAdapter;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.OrderShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payment extends AppCompatActivity {
    RecyclerView rl3;
    PaymentAdapter rl;
    String name;
    List<OrderShow> list=new ArrayList<>();
    TextView empty;
    List<OrderShow> list1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payment);
        FirebaseApp.initializeApp(this);
        rl3=(RecyclerView)findViewById(R.id.rl3);
        rl = new PaymentAdapter(this, list);
        empty=(TextView)findViewById(R.id.empty);
        SessionManager sh= new SessionManager(this,SessionManager.USERSESSION);
        rl3.setHasFixedSize(true);
        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManagerAirBnBs.PHONE);
        Log.d("PaymentActivity", "User Identifier (name): " + name);

        LinearLayoutManager li = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rl3.setLayoutManager(li);
        rl3.setAdapter(rl);
        FirebaseDatabase.getInstance().getReference("Users").child(name).child("Payment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d(TAG, "onDataChange: Data changed");
                list.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    OrderShow or=ds.getValue(OrderShow.class);
                    list.add(or);
                }
                rl.notifyDataSetChanged();
                Log.d("PaymentActivity", "List size after onDataChange: " + list.size());
                if(list.size()==0)
                {
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("No Results Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled: Error", databaseError.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
}