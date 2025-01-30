package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Adapters.rl4HR;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.Rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateRooms extends AppCompatActivity {

    RecyclerView rl4HR1;
    String name;
    List<Rooms> list;
    rl4HR rl;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_rooms);
        SessionManagerAirBnBs sh= new SessionManagerAirBnBs(UpdateRooms.this, SessionManagerAirBnBs.USERSESSION);
        img=(ImageView)findViewById(R.id.back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManagerAirBnBs.FULLNAME);
        String fullname1=hm.get(SessionManagerAirBnBs.DES);
        String fullname2=hm.get(SessionManagerAirBnBs.EMAIL);
        String fullname3=hm.get(SessionManagerAirBnBs.PHONE);
        String fullname4=hm.get(SessionManagerAirBnBs.USERNAME);
        String fullname5=hm.get(SessionManagerAirBnBs.RATING);
        String fullname6=hm.get(SessionManagerAirBnBs.PASS);
        rl4HR1=(RecyclerView)findViewById(R.id.rl4HR);
        list=new ArrayList<>();
        rl=new rl4HR(UpdateRooms.this, list,name,"Update");
        rl4HR1.setHasFixedSize(true);
        LinearLayoutManager li=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rl4HR1.setLayoutManager(li);rl4HR1.setAdapter(rl);
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("rooms");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Rooms roo=ds.getValue(Rooms.class);
                    list.add(roo);
                }
                rl.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AirBnBOverview.class));
    }
}