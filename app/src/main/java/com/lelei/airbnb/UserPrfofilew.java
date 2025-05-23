package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Models.OrderShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserPrfofilew extends AppCompatActivity {
    TextView name,study,date,place,aboutme,fb;
    ImageView profile_image;
    CardView fav,save,order,upcoming,pay;
    String phone;
    BottomNavigationView bm;
    String[] url = new String[1];

    List<OrderShow> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_prfofilew);
        name=(TextView)findViewById(R.id.name);
        fb=(TextView)findViewById(R.id.fb);
        profile_image=(ImageView) findViewById(R.id.profile_image);
        fav=(CardView)findViewById(R.id.fav);
        pay=(CardView)findViewById(R.id.pay);
        final SessionManager sh=new SessionManager(this,SessionManager.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        final String phone2=hm.get(SessionManager.PHONE);
        order=(CardView)findViewById(R.id.order);
        upcoming=(CardView)findViewById(R.id.upcoming);
        save=(CardView)findViewById(R.id.save);
        bm = (BottomNavigationView) findViewById(R.id.bottomnav);
        bm.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.s) {
                    startActivity(new Intent(UserPrfofilew.this, All_AirBnBs.class).putExtra("com","no"));
                } else if (item.getItemId() == R.id.fav) {
                    startActivity(new Intent(UserPrfofilew.this, Fav.class));

                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(UserPrfofilew.this, UserPrfofilew.class));

                }
                else if(item.getItemId()==R.id.watch)
                {
                    startActivity(new Intent(UserPrfofilew.this, WatchLater.class));

                }else if(item.getItemId()==R.id.logout)
                {


                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            UserPrfofilew.this);

// Setting Dialog Title
                    alertDialog2.setTitle("Log Out.");

// Setting Dialog Message
                    alertDialog2.setMessage("Are you sure you want to Log Out?");
                    alertDialog2.setIcon(R.drawable.apptitle);
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sh.logOut();
                                    FirebaseAuth.getInstance().signOut();

                                    dialog.cancel();
                                    startActivity(new Intent(UserPrfofilew.this, DashBoard.class));

                                }
                            });
// Setting Negative "NO" Btn
                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();


                }

            }
        });
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
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPrfofilew.this,Fav.class));
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPrfofilew.this,WatchLater.class));
                finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPrfofilew.this,Payment.class));
                finish();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  BottomSheetDialog bsd=new BottomSheetDialog(UserPrfofilew.this, R.style.BottomSheetDialogTheme);
                View bs= LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet,(LinearLayout)findViewById(R.id.bottomsheet));
                bs.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(),UserImage.class).putExtra("url",url[0]));
                        finish();
                    }
                });
                bs.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(),UserImage.class).putExtra("url",url[0]));
                        finish();
                    }
                });

                bsd.setContentView(bs);
                bsd.show();
            }
        });
        study=(TextView)findViewById(R.id.study);
        date=(TextView)findViewById(R.id.date);
        place=(TextView)findViewById(R.id.place);
        aboutme=(TextView)findViewById(R.id.aboutme);

        String fullname=hm.get(SessionManager.FULLNAME);
        String dob=hm.get(SessionManager.DOB);
        String email=hm.get(SessionManager.EMAIL);
        phone=hm.get(SessionManager.PHONE);
        String username=hm.get(SessionManager.USERNAME);
        String gender=hm.get(SessionManager.GENDER);
        String pass=hm.get(SessionManager.PASS);
        String socialmedia=hm.get(SessionManager.SOCIALMEDIA);
        String bio=hm.get(SessionManager.BIO);
        String edu=hm.get(SessionManager.EDUCATION);
        String location=hm.get(SessionManager.LOCATION);
        String signupdate=hm.get(SessionManager.signupdate);

        Query c = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(phone);
        c.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                url[0] = d.child(phone).child("url").getValue(String.class);
                //Picasso.with(UserPrfofilew.this).load(url[0]).fit().centerCrop().into(profile_image);
                Picasso.get().load(url[0]).fit().centerCrop().into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name.setText("    "+fullname);
        study.setText("    "+edu);
        place.setText("    "+location);
        aboutme.setText("    "+bio);
        fb.setText("    "+fullname);


        int cday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(getApplicationContext(),OrderGiven.class);
                in.putExtra("phone",phone);
                in.putExtra("name","upcoming");

                startActivity(in);
            }
        });
        if(signupdate==null) {

            date.setText("   10 April 2024");
        }
        else
            date.setText("    "+signupdate);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(getApplicationContext(),OrderGiven.class);
                in.putExtra("phone",phone);
                in.putExtra("name","given");

                startActivity(in);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
        finish();
    }
}