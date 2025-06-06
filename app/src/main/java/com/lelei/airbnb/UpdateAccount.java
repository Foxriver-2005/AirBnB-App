package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UpdateAccount extends AppCompatActivity {
    TextInputLayout name1, username, email,rating;
    String name;
    Button update;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        name1=(TextInputLayout)findViewById(R.id.name);
        update=(Button)findViewById(R.id.update);

        username=(TextInputLayout)findViewById(R.id.username);
        email=(TextInputLayout)findViewById(R.id.email);
        rating=(TextInputLayout)findViewById(R.id.star);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SessionManagerAirBnBs sh= new SessionManagerAirBnBs(UpdateAccount.this, SessionManagerAirBnBs.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManager.FULLNAME);
        final String fullname1=hm.get(SessionManagerAirBnBs.DES);
        final String fullname2=hm.get(SessionManagerAirBnBs.EMAIL);
        final String fullname3=hm.get(SessionManagerAirBnBs.PHONE);
        String fullname4=hm.get(SessionManagerAirBnBs.USERNAME);
        String fullname5=hm.get(SessionManagerAirBnBs.RATING);
        final String fullname6=hm.get(SessionManagerAirBnBs.PASS);
        final String fullname7=hm.get(SessionManagerAirBnBs.URL);
        name1.getEditText().setText(name);

        name1.getEditText().setEnabled(false);
        username.getEditText().setText(fullname4);
        email.getEditText().setText(fullname2);



        rating.getEditText().setText(fullname5);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email1 = email.getEditText().getText().toString().trim();
                FirebaseDatabase.getInstance().getReference("Hotels").orderByChild("email").equalTo(email1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            email.getEditText().setError("Email Already Exists!!");
                            email.getEditText().requestFocus();
                        }
                        else
                        {
                            final String rating1 = rating.getEditText().getText().toString().trim();
                            final String username1 = username.getEditText().getText().toString().trim();
                            if(fullname2.equals(email1)){
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Hotels");
                                HashMap hm1 = new HashMap<>();
                                hm1.put("email", email1);
                                hm1.put("username", username1);
                                hm1.put("star", rating1);
                                //   Hotel d=new Hotel(name,fullname6,email.getEditText().getText(),fullname3,fullname1, rating.getEditText().getText(),username.getEditText(),getText(),fullname7);
                                db.child(name).updateChildren(hm1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SessionManagerAirBnBs sh = new SessionManagerAirBnBs(UpdateAccount.this, SessionManagerAirBnBs.USERSESSION);

                                        sh.loginSession(name, email1, rating1, fullname3, fullname6, fullname1, username1, fullname7);
                                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), AirBnBRooms.class));
                                    }
                                });
                            }
                            else
                            {
                                AlertDialog.Builder al= new AlertDialog.Builder(UpdateAccount.this);
                                al.setTitle(R.string.title);
                                al.setMessage(R.string.message);
                                al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Random ra=new Random();
                                        long p=100000+ra.nextInt(899999);
                                        final String Email="foxriver2471@gmail.com";
                                        final String pass="yurn fjko ducq txkj";
                                        Properties prop = new Properties();
                                        prop.put("mail.smtp.host", "smtp.gmail.com");
                                        prop.put("mail.smtp.port", "465");
                                        prop.put("mail.smtp.auth", "true");
                                        prop.put("mail.smtp.socketFactory.port", "465");
                                        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                                return new PasswordAuthentication(Email, pass);
                                            }
                                        });
                                        try {

                                            Message message = new MimeMessage(session);
                                            message.setFrom(new InternetAddress(Email));
                                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email1));
                                            message.setSubject("Sending Otp");
                                            message.setText("Your OTP to change email address is"+ p);

                                            new SendEmail().execute(message);

                                        } catch (MessagingException e) {
                                            e.printStackTrace();
                                        }

                                        Intent cl=new Intent(UpdateAccount.this, OTPEMAIL.class);
                                        cl.putExtra("OTP",""+p);
                                        cl.putExtra("EMAIL",email1);
                                        cl.putExtra("USERNAME",username1);
                                        cl.putExtra("STAR",rating1);
                                        startActivity(cl);
                                    }
                                });
                                al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"You have choosen not to update your account information", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                                AlertDialog ald=al.create();
                                ald.show();


                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }});
    }


    class SendEmail extends AsyncTask<Message, String, String> {

        @Override
        protected String doInBackground(Message... messages) {

            try {
                Transport.send(messages[0]);
                return "success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "error";
            }


        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AirBnBOverview.class));
    }
}