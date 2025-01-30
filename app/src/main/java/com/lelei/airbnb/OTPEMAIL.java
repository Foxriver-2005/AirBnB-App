package com.lelei.airbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;

import java.util.HashMap;

public class OTPEMAIL extends AppCompatActivity {
    PinView pin;
    ImageView cancel;
    String otp1,otp,email1,username1,rating1,name;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpemail);
        otp=getIntent().getStringExtra("OTP").toString();
        username1=getIntent().getStringExtra("USERNAME").toString();
        email1=getIntent().getStringExtra("EMAIL").toString();
        rating1=getIntent().getStringExtra("STAR").toString();
        SessionManagerAirBnBs sh= new SessionManagerAirBnBs(OTPEMAIL.this, SessionManagerAirBnBs.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManager.FULLNAME);
        final String fullname1=hm.get(SessionManagerAirBnBs.DES);
        final String fullname2=hm.get(SessionManagerAirBnBs.EMAIL);
        final String fullname3=hm.get(SessionManagerAirBnBs.PHONE);
        String fullname4=hm.get(SessionManagerAirBnBs.USERNAME);
        String fullname5=hm.get(SessionManagerAirBnBs.RATING);
        final String fullname6=hm.get(SessionManagerAirBnBs.PASS);
        final String fullname7=hm.get(SessionManagerAirBnBs.URL);
        pin = (PinView) findViewById(R.id.pin);
        verify = (Button) findViewById(R.id.verify);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AirBnBRooms.class));
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp1=pin.getText().toString();

                if(otp1.equals(otp))
                {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Hotels");
                    HashMap hm1 = new HashMap<>();
                    hm1.put("email", email1);
                    hm1.put("username", username1);
                    hm1.put("star", rating1);
                    //   Hotel d=new Hotel(name,fullname6,email.getEditText().getText(),fullname3,fullname1, rating.getEditText().getText(),username.getEditText(),getText(),fullname7);
                    db.child(name).updateChildren(hm1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SessionManagerAirBnBs sh = new SessionManagerAirBnBs(OTPEMAIL.this, SessionManagerAirBnBs.USERSESSION);

                            sh.loginSession(name, email1, rating1, fullname3, fullname6, fullname1, username1, fullname7);
                            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AirBnBRooms.class));
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),otp+"Fox"+otp1,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}