package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lelei.airbnb.Adapters.rl4HR;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.Rooms;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import static com.lelei.airbnb.Adapters.rl4HR.dele;

public class AirBnBRooms extends AppCompatActivity {
    TextView h1,h2;
    String name;
    ImageView dpp;
    RecyclerView rl4HR1;
    LinearLayout layoutList;
    DrawerLayout dr;
    NavigationView nav;
    ImageView menui;
    List<Rooms> list;
    String token;
    rl4HR rl;
    static String Dele[]=new String[1000];
    static int poi=0;
    String fullname3;
    SessionManagerAirBnBs sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_airbnb_rooms);
        h1=(TextView) findViewById(R.id.h1);
        // layoutList=(LinearLayout) findViewById(R.id.linear);
        h2=(TextView) findViewById(R.id.h2);
        dpp=(ImageView) findViewById(R.id.dpp);
        dr=(DrawerLayout) findViewById(R.id.drawer);
        menui=(ImageView) findViewById(R.id.menuicon);

        sh= new SessionManagerAirBnBs(AirBnBRooms.this, SessionManagerAirBnBs.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        name=hm.get(SessionManagerAirBnBs.FULLNAME);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = Objects.requireNonNull(task.getResult()).getToken();
                            HashMap t=new HashMap();
                            t.put("token",token);
                            FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Token").updateChildren(t);

                        }

                    }
                });
        String fullname1=hm.get(SessionManagerAirBnBs.DES);
        String fullname2=hm.get(SessionManagerAirBnBs.EMAIL);
        fullname3=hm.get(SessionManagerAirBnBs.PHONE);
        String fullname4=hm.get(SessionManagerAirBnBs.USERNAME);
        String fullname5=hm.get(SessionManagerAirBnBs.RATING);
        String fullname6=hm.get(SessionManagerAirBnBs.PASS);
        delete();
        delete1();
        nav=(NavigationView) findViewById(R.id.navigation);
        rl4HR1=(RecyclerView)findViewById(R.id.rl4HR);
        list=new ArrayList<>();

        rl=new rl4HR(AirBnBRooms.this, list,name,"Rooms");
        rl4HR1.setHasFixedSize(true);
        LinearLayoutManager li=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rl4HR1.setLayoutManager(li);rl4HR1.setAdapter(rl);


        navigationDrawer();
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

        Query c = FirebaseDatabase.getInstance().getReference("Hotels").orderByChild("name").equalTo(name);
        c.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                if (d.exists()) {
                    h1.setText(name);
                    h2.setText(name);
                    String url= d.child(name).child("url").getValue(String.class);

                    //Picasso.with(HotelRooms.this).load(url).placeholder(R.drawable.star31).fit().centerCrop().into(dpp);
                    Picasso.get().load(url).placeholder(R.drawable.star31).fit().centerCrop().into(dpp);
                } else
                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    private void delete1() {
        for(int i=0;i<poi;i++)
        {
            if(Dele[i]!=null)
            {
                FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("needApp").child(Dele[i]).removeValue();
            }
        }

    }

    public void navigationDrawer()
    {
        nav.bringToFront();
        nav.setCheckedItem(R.id.home);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.hotelinfo)
                    startActivity(new Intent(getApplicationContext(), AirBnBInfo.class));
                else if(item.getItemId()==R.id.update)
                    startActivity(new Intent(getApplicationContext(),UpdateAccount.class));
                else if(item.getItemId()==R.id.updaterooms) {
                    startActivity(new Intent(getApplicationContext(), UpdateRooms.class));
                    finish();
                }
                else if(item.getItemId()==R.id.ame)
                {
                    startActivity(new Intent(getApplicationContext(), ShowAmenties.class).putExtra("name",name));
                    finish();
                }
                else if(item.getItemId()==R.id.add){
                    startActivity(new Intent(getApplicationContext(), roomCreation.class).putExtra("Name",name));
                    finish();

                }
                else if(item.getItemId()==R.id.photos){
                    startActivity(new Intent(getApplicationContext(), PhotosFor.class));
                    finish();

                }
                else if(item.getItemId()==R.id.received)
                {
                    startActivity(new Intent(getApplicationContext(), OrderGiven.class).putExtra("phone",fullname3).putExtra("name",name));
                    finish();
                }
                else if(item.getItemId()==R.id.approval)
                {
                    startActivity(new Intent(getApplicationContext(), Approval.class));
                    finish();
                }
                else if(item.getItemId()==R.id.add){
                    Intent in=new Intent(getApplicationContext(),OrderGiven.class);
                    in.putExtra("phone",name);
                    startActivity(in);
                    finish();
                }
                else if(item.getItemId()==R.id.nav_LogOut)
                {

                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            AirBnBRooms.this);

// Setting Dialog Title
                    alertDialog2.setTitle("Log Out.");

// Setting Dialog Message
                    alertDialog2.setMessage("Are you sure you want to Log Out?");
                    alertDialog2.setIcon(R.mipmap.logo);
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sh.logOut();
                                    FirebaseAuth.getInstance().signOut();

                                    dialog.cancel();
                                    startActivity(new Intent(AirBnBRooms.this, LogIn_Or_SignUp.class));

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

                return true;
            }
        });
        menui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dr.isDrawerVisible(GravityCompat.START))
                {
                    dr.closeDrawer(GravityCompat.START);
                }
                else
                    dr.openDrawer(GravityCompat.START);
            }
        });
        animateNavDrawer();
    }
    public void animateNavDrawer(){
        dr.setScrimColor(getResources().getColor(R.color.makeUplight));
    }
    public void delete()
    {
        //Toast.makeText(getApplicationContext(),"KI"+rl4HR.d, Toast.LENGTH_LONG).show();
        for(int i=0;i<rl4HR.d;i++)
        {

            if(dele[i]!=null)
            {
                //Toast.makeText(getApplicationContext(),dele[i], Toast.LENGTH_LONG).show();
                FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("rooms").child(dele[i]).removeValue();
                dele[i]=null;
            }

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AirBnBOverview.class));
        finish();
    }
}