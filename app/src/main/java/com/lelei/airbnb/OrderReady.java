package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Helpers.FcmNotificationsSender;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.OrderShow;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OrderReady extends AppCompatActivity {
    String[] quas;
    String qua;
    Spinner qua1;
    DatePicker start,end;
    String start1,end1,price;
    String month,month1;
    ImageView back;
    int p=1,p1=1;
    int year,cmonth,day,day1,cmonth1,year1;
    String name;
    Button order;
    String HNAME;
    String Hname;
    String token;
    String userName,phone2;
    String emai="";
    HashMap<String,String> hm;
    static String name1,qua23,start11,end12,ne1,HNAME1,final1,price1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_ready);
        name=getIntent().getStringExtra("Name");
        HNAME=getIntent().getStringExtra("HNAME");
        SessionManager sh=new SessionManager(this,SessionManager.USERSESSION);

        hm=sh.returnData();
        phone2=hm.get(SessionManager.PHONE);
        userName=hm.get(SessionManager.FULLNAME);
        quas=getResources().getStringArray(R.array.qua);
        qua1=(Spinner)findViewById(R.id.qua);
        start=(DatePicker) findViewById(R.id.start);
        end=(DatePicker)findViewById(R.id.end);
        order=(Button)findViewById(R.id.confirm);
        back=(ImageView)findViewById(R.id.back);
        price=getIntent().getStringExtra("Price");
        if (price != null) {
            price = price.replace(",", "");
            p1 = (int) Double.parseDouble(price); // or use BigDecimal for better precision
        }
        ArrayAdapter<String> ar=new ArrayAdapter<String>(this, R.layout.education,R.id.Edu,quas);
        qua1.setAdapter(ar);
        start.setMinDate(System.currentTimeMillis()-1000);
        long now=System.currentTimeMillis()-1000;
        end.setMinDate(start.getDayOfMonth());
        start.setMaxDate(now+(1000*60*60*24*7));
        end.setMaxDate(now+(1000*60*60*24*7)+(1000*60*60*24*7));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ne=getIntent().getStringExtra("Rn");
                SessionManagerAirBnBs sh = new SessionManagerAirBnBs(getApplicationContext(), SessionManagerAirBnBs.USERSESSION);
                FirebaseDatabase.getInstance().getReference("Hotels").child(ne).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        emai=dataSnapshot.child("email").getValue().toString();
                        //Toast.makeText(getApplicationContext(),emai+"Inside", Toast.LENGTH_LONG).show();
                        String subject = "Your AirBnB Has New Order. Now go the Need Approval section of your profile to approve the booking";
                        final String Email = "foxriver2471@gmail.com";
                        final String pass = "yurn fjko ducq txkj";
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
                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emai));
                            message.setSubject("New Order");
                            message.setText(subject);
                            //Toast.makeText(getApplicationContext(),emai+"Outsdie",Toast.LENGTH_LONG).show();

                            new OrderReady.SendEmail().execute(message);


                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                year=start.getYear();
                cmonth=start.getMonth();
                day=start.getDayOfMonth();
                year1=end.getYear();
                cmonth1=end.getMonth();
                day1=end.getDayOfMonth();
                if(cmonth==1-1)
                    month="January";
                else if(cmonth==2-1)
                    month="February";
                else if(cmonth==3-1)
                    month="March";
                else if(cmonth==4-1)
                    month="April";
                else if(cmonth==5-1)
                    month="May";
                else  if(cmonth==6-1)
                    month="June";
                else if(cmonth==7-1)
                    month="July";
                else   if(cmonth==8-1)
                    month="August";
                else if(cmonth==9-1)
                    month="September";
                else if(cmonth==10-1)
                    month="October";
                else if(cmonth==11-1)
                    month="November";
                else month="December";
                qua=qua1.getSelectedItem().toString();
                if(cmonth1==1-1)
                    month1="January";
                else if(cmonth1==2-1)
                    month1="February";
                else if(cmonth1==3-1)
                    month1="March";
                else if(cmonth1==4-1)
                    month1="April";
                else if(cmonth1==5-1)
                    month1="May";
                else  if(cmonth1==6-1)
                    month1="June";
                else if(cmonth1==7-1)
                    month1="July";
                else   if(cmonth1==8-1)
                    month1="August";
                else if(cmonth1==9-1)
                    month1="September";
                else if(cmonth1==10-1)
                    month1="October";
                else if(cmonth1==11-1)
                    month1="November";
                else month1="December";
                start1=""+day+" "+month+" "+year;
                end1=""+day1+" "+month1+" "+year1;

                Intent in=new Intent(OrderReady.this,OrderConfirmation.class);
                in.putExtra("Name",name);
                in.putExtra("Qua",qua);
                in.putExtra("Start",start1);
                in.putExtra("End",end1);
                in.putExtra("Rn",ne);
                in.putExtra("HNAME",HNAME);

                LocalDate d1= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    d1 = LocalDate.of(year,cmonth-1,day);
                }
                LocalDate d2= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    d2 = LocalDate.of(year1,cmonth1-1,day1);
                }
                long diff= 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    diff = Math.abs(Period.between(d2,d1).getDays());
                }
                if(diff==0)
                    diff=1;
                in.putExtra("Diff",""+diff);

                // Get start and end dates using Calendar
                Calendar startDate = Calendar.getInstance();
                startDate.set(start.getYear(), start.getMonth(), start.getDayOfMonth());

                Calendar endDate = Calendar.getInstance();
                endDate.set(end.getYear(), end.getMonth(), end.getDayOfMonth());

                // Calculate the number of days between start and end dates
                long numberOfDays = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (1000 * 60 * 60 * 24);
                if (numberOfDays <= 0) {
                    numberOfDays = 1; // Ensure at least one day
                }
                // Get the quantity from the Spinner
                qua = qua1.getSelectedItem().toString();
                int quantity = Integer.parseInt(qua);

                //Toast.makeText(getApplicationContext(),""+p1+" "+diff+" "+start1+"   "+end1+" "+qua,Toast.LENGTH_LONG).show();
                //p=80*p1;
                // Calculate total price
                p = (int) (numberOfDays * p1 * quantity);
                in.putExtra("Price",""+p);
                //startActivity(in);
                final long finalDiff = diff;
                //Toast.makeText(getApplicationContext(),ne,Toast.LENGTH_LONG).show();
                Calendar cal = Calendar.getInstance();
                int cday = cal.get(Calendar.DAY_OF_MONTH);
                int cm = cal.get(Calendar.MONTH);
                int cy = cal.get(Calendar.YEAR);


                OrderShow or=new OrderShow(userName+"\n "+phone2,ne,p+"",qua+"",start1+"",end1+"",name,cday+" "+cm+" "+cy,hm.get(SessionManager.EMAIL));
                FirebaseDatabase.getInstance().getReference("Hotels").child(ne).child("needApp").child(start1+end1+p+""+name+cday+" "+cm+" "+cy).setValue(or);
                name1=name;
                qua23=qua;
                start11=start1;
                end12=end1;

                HNAME1=HNAME;
                final1=""+finalDiff;
                price1=price;
                FirebaseDatabase.getInstance().getReference("Hotels").child(ne).child("Token").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        token = dataSnapshot.child("token").getValue().toString();
                        FcmNotificationsSender fcm = new FcmNotificationsSender(token, "New Order", "Your AirBnB Has New Order.", getApplicationContext(), OrderReady.this);
                        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                        fcm.SendNotifications();
                        startActivity(new Intent(getApplicationContext(), AfterCallingNotification.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }
    public long difference(Date one, Date two)
    {
        long diff=(one.getTime()-two.getTime())/86400;
        return Math.abs(diff);
    }

    @Override
    public void onBackPressed() {
        OrderReady.super.onBackPressed();
    }
    private class SendEmail extends AsyncTask<Message, String, String> {

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
}