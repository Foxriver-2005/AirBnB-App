package com.lelei.airbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;

import java.util.HashMap;

public class AirBnBInfo extends AppCompatActivity {
    TextInputLayout name1, username, email,phone,star;
    String name;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_airbnb_info);
        name1=(TextInputLayout)findViewById(R.id.name);
        username=(TextInputLayout)findViewById(R.id.username);
        email=(TextInputLayout)findViewById(R.id.email);
        phone=(TextInputLayout)findViewById(R.id.phone);
        star=(TextInputLayout)findViewById(R.id.star);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SessionManagerAirBnBs sh= new SessionManagerAirBnBs(AirBnBInfo.this, SessionManagerAirBnBs.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManager.FULLNAME);
        String fullname1=hm.get(SessionManagerAirBnBs.DES);
        String fullname2=hm.get(SessionManagerAirBnBs.EMAIL);
        String fullname3=hm.get(SessionManagerAirBnBs.PHONE);
        String fullname4=hm.get(SessionManagerAirBnBs.USERNAME);
        String fullname5=hm.get(SessionManagerAirBnBs.RATING);
        String fullname6=hm.get(SessionManagerAirBnBs.PASS);
        name1.getEditText().setText(name);
        name1.getEditText().setEnabled(false);
        username.getEditText().setText(fullname4);
        star.getEditText().setText(fullname5);
        phone.getEditText().setText(fullname3);
        email.getEditText().setText(fullname2);
        email.getEditText().setEnabled(false);
        phone.getEditText().setEnabled(false);
        star.getEditText().setEnabled(false);
        username.getEditText().setEnabled(false);

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AirBnBOverview.class));
    }
}