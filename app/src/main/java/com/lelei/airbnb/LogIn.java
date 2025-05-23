package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;

import java.util.HashMap;
import java.util.Objects;

public class LogIn extends AppCompatActivity {
    ImageView im1;
    Button b1, forgot, create;
    Button log_login;
    TextInputLayout phone, password;
    String p, p1, p2;
    Dialog d;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);

        log_login = (Button) findViewById(R.id.log_login);
        forgot = (Button) findViewById(R.id.forgot);
        create = (Button) findViewById(R.id.create);
        remember = (CheckBox) findViewById(R.id.checkBox);

        phone = (TextInputLayout) findViewById(R.id.name);
        password = (TextInputLayout) findViewById(R.id.password);

        im1 = (ImageView) findViewById(R.id.back);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        b1 = (Button) findViewById(R.id.forgot);
        b1.setTextColor(Color.BLACK);
        log_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogIn();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, Signup.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, forgot.class));
            }
        });


    }

    public boolean isWiConnected(LogIn l) {
        ConnectivityManager c = (ConnectivityManager) l.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wi = c.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mi = c.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wi != null && wi.isConnected()) || (mi != null && mi.isConnected())) {
            return true;
        } else
            return false;

    }

    private void showCustomDialog() {
        d = new Dialog(LogIn.this);
        d.setContentView(R.layout.custom);
        d.getWindow().setBackgroundDrawable(getDrawable(R.drawable.customdraw));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.setCancelable(false);
        d.getWindow().getAttributes().windowAnimations = R.style.animate;
        d.show();
        Button b1 = d.findViewById(R.id.cancel);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, LogIn_Or_SignUp.class));
                finish();
            }

        });
        Button b2 = d.findViewById(R.id.connect);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    public void userLogIn() {
        p2 = phone.getEditText().getText().toString().trim();
        p = "+254" + p2;

        Toast.makeText(getApplicationContext(), p, Toast.LENGTH_LONG).show();
        p1 = password.getEditText().getText().toString().trim();
        if (!isWiConnected(this)) {

            showCustomDialog();

        } else if (p.isEmpty()) {
            phone.setError("Field needs to be filled");
            phone.requestFocus();
            return;
        } else if (p1.isEmpty()) {
            password.setError("Field needs to be filled");
            password.requestFocus();
            return;
        } else {
            Query c = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(p);
            c.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot d) {
                    if (d.exists()) {
                        String pass = d.child(p).child("password").getValue(String.class);
                        if (p1.equals(pass)) {

                            String email = d.child(p).child("email").getValue(String.class);
                            String dob = d.child(p).child("dob").getValue(String.class);
                            String gender = d.child(p).child("gender").getValue(String.class);
                            String fullname = d.child(p).child("name").getValue(String.class);
                            final String phone2 = d.child(p).child("phone").getValue(String.class);
                            String username = d.child(p).child("username").getValue(String.class);
                            String edu = d.child(p).child("education").getValue(String.class);
                            String location = d.child(p).child("location").getValue(String.class);
                            String bio = d.child(p).child("bio").getValue(String.class);
                            String signupdate = d.child(p).child("signupdate").getValue(String.class);
                            String socialmedia = d.child(p).child("socialmedia").getValue(String.class);
                            SessionManager sh = new SessionManager(LogIn.this, SessionManager.USERSESSION);
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (task.isSuccessful()) {
                                                String token = Objects.requireNonNull(task.getResult()).getToken();
                                                HashMap t=new HashMap();
                                                t.put("token",token);
                                                FirebaseDatabase.getInstance().getReference("Users").child(phone2).child("Token").updateChildren(t);

                                            }

                                        }
                                    });
                            sh.loginSession(fullname, email, gender, phone2, pass, dob, username, location, socialmedia, bio, edu, signupdate);
                            startActivity(new Intent(LogIn.this, UserPrfofilew.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "Phone and Password does not match. Please enter right informations", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        Query c1 = FirebaseDatabase.getInstance().getReference("Hotels").child("HotelsPassword").orderByChild("phone").equalTo(p2);

                        c1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    final  String pass = dataSnapshot.child(p2).child("password").getValue(String.class);
                                    if (p1.equals(pass)) {
                                        final String n=dataSnapshot.child(p2).child("name").getValue(String.class);
                                        FirebaseDatabase.getInstance().getReference("Hotels").child(n).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                String des=dataSnapshot1.child("des").getValue().toString();
                                                String email=dataSnapshot1.child("email").getValue().toString();
                                                String user=dataSnapshot1.child("username").getValue().toString();
                                                String url=dataSnapshot1.child("url").getValue().toString();
                                                String star=dataSnapshot1.child("star").getValue().toString();
                                                SessionManagerAirBnBs sh = new SessionManagerAirBnBs(LogIn.this, SessionManagerAirBnBs.USERSESSION);
                                                //Toast.makeText(getApplicationContext(),star+"",Toast.LENGTH_LONG).show();
                                                sh.loginSession(n,email,star,p2,pass,des,user,url);
                                                FirebaseInstanceId.getInstance().getInstanceId()
                                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    String token = Objects.requireNonNull(task.getResult()).getToken();
                                                                    HashMap t=new HashMap();
                                                                    t.put("token",token);
                                                                    FirebaseDatabase.getInstance().getReference("Hotels").child(n).child("Token").updateChildren(t);

                                                                }

                                                            }
                                                        });
                                                startActivity(new Intent(LogIn.this, AirBnBRooms.class));
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), p1+"/"+pass, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Phone and Password does not match"+p2+"/"+p2+"Please enter right informations", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        LogIn.super.onBackPressed();
    }
}