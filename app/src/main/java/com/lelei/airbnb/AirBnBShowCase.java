package com.lelei.airbnb;

import static java.lang.Double.NaN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lelei.airbnb.Adapters.rl4HR;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Models.CommentShow;
import com.lelei.airbnb.Models.RatingClass;
import com.lelei.airbnb.Models.Rooms;
import com.lelei.airbnb.Models.SaveAndFav;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AirBnBShowCase extends AppCompatActivity {
    ImageView back, save;
    String name;
    TextView ad,HNAME;
    Button amenities;
    TextView lo;
    int E=0;
    float k=0;
    TextView des;
    String count;
    TextView na;
    RecyclerView rl4HR1;
    BottomNavigationView bm;
    List<Rooms> list;
    ImageView fav;
    rl4HR rl;
    List<String> am = new ArrayList<>();
    TextView house;
    TextView review,profileName;
    ImageView profile_Image,map;
    Button allreviews;
    RatingBar rating;
    List<CommentShow>list1=new ArrayList<>();
    TextView count1;
    float r;
    int co = 1, cow = 1;
    boolean isP=false;
    LinearLayout profile;
    TextView noR,hallt;
    ImageView hall;
    ImageView display;
    Button hallb;

    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_airbnb_show_case);
        back = (ImageView) findViewById(R.id.back);
        map = (ImageView) findViewById(R.id.map);
        hallb = (Button) findViewById(R.id.hallb);
        display = (ImageView) findViewById(R.id.display);
        ad = (TextView) findViewById(R.id.address);
        hallt = (TextView) findViewById(R.id.hallt);
        hall = (ImageView) findViewById(R.id.hall);
        lo = (TextView) findViewById(R.id.location);
        noR = (TextView) findViewById(R.id.noR);
        profile = (LinearLayout) findViewById(R.id.profile);
        na = (TextView) findViewById(R.id.name);
        count1 = (TextView) findViewById(R.id.count);
        HNAME = (TextView) findViewById(R.id.name);
        rating = (RatingBar) findViewById(R.id.rating);
        rate=rating.getNumStars();

        name = getIntent().getStringExtra("name");
        review = (TextView) findViewById(R.id.review);
        profile_Image = (ImageView) findViewById(R.id.profile_image);
        allreviews = (Button) findViewById(R.id.allreview);
        allreviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),Reviews.class);
                in.putExtra("name",name);

                startActivity(in);
                finish();
            }
        });
        hallb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WatchAll.class).putExtra("name",name));
            }
        });
        FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Photos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        //Picasso.with(HotelShowCase.this).load(ds.getValue().toString()).into(hall);
                        Picasso.get().load(ds.getValue().toString()).into(hall);
                        hallt.setText("Hall of"+" "+name);
                        break;

                    }
                }
                else
                {

                    hallt.setText("No photos have benn added by the AirBnB.");
                    hallb.setVisibility(View.GONE);
                    hall.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dgh=  FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Rating");
        dgh.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot e: dataSnapshot.getChildren())
                {
                    E++;
                    k+=Float.parseFloat(e.getValue(String.class));
                    //Toast.makeText(getApplicationContext(),e.getValue(String.class),Toast.LENGTH_LONG).show();
                }
                if(E!=0)
                    r= (float) (k/E);
                if(r==NaN)
                    r= (float) 0.0;


                FirebaseDatabase.getInstance().getReference("Hotels").child("Rating").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            HashMap hmj=new HashMap();
                            hmj.put("rating",r);
                            FirebaseDatabase.getInstance().getReference("Hotels").child("Rating").child(name).updateChildren(hmj);
                        }
                        else
                        {
                            RatingClass rs=new RatingClass(name,r);
                            FirebaseDatabase.getInstance().getReference("Hotels").child("Rating").child(name).setValue(rs);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                rating.setRating( r);
                // Toast.makeText(getApplicationContext()," "+rating.getNumStars(),Toast.LENGTH_LONG).show();
                count1.setText(E+" reviews");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileName = (TextView) findViewById(R.id.profileName);
        des = (TextView) findViewById(R.id.des);

        bm = (BottomNavigationView) findViewById(R.id.bottomnav);
        bm.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.s) {
                    startActivity(new Intent(AirBnBShowCase.this, All_AirBnBs.class).putExtra("com","no"));
                } else if (item.getItemId() == R.id.fav) {
                    startActivity(new Intent(AirBnBShowCase.this, Fav.class));

                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(AirBnBShowCase.this, UserPrfofilew.class));

                }
                else
                {
                    startActivity(new Intent(AirBnBShowCase.this, WatchLater.class));

                }
            }
        });
        house = (TextView) findViewById(R.id.house);
        rl4HR1 = (RecyclerView) findViewById(R.id.rl4HR);
        list = new ArrayList<>();
        SessionManager sh = new SessionManager(AirBnBShowCase.this, SessionManager.USERSESSION);

        HashMap<String, String> hm = sh.returnData();
        final String phone = hm.get(SessionManager.PHONE);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Review");


        db.addValueEventListener(new ValueEventListener() {
            int f=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   list.clear();
                noR.setText("No reviews yet.");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    profile.setVisibility(View.VISIBLE);
                    noR.setVisibility(View.GONE);
                    RatingBar rating1=(RatingBar)findViewById(R.id.rating1);
                    CommentShow roo= ds.getValue(CommentShow.class);
                    //Picasso.with(HotelShowCase.this).load(roo.getUrl()).fit().centerCrop().into(profile_Image);
                    Picasso.get().load(roo.getUrl()).fit().centerCrop().into(profile_Image);
                    profileName.setText(roo.getName());
                    review.setText(roo.getComment());
                    rating1.setVisibility(View.VISIBLE);
                    rating1.setRating(Float.parseFloat(roo.getStar()));
                    f++;
                    break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        fav = (ImageView) findViewById(R.id.fav);
        save = (ImageView) findViewById(R.id.save);
        Query SAVE = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("save").orderByChild("name").equalTo(name);
        SAVE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    save.setImageResource(R.drawable.do1);

                } else {

                    save.setImageResource(R.drawable.save);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query FAV=FirebaseDatabase.getInstance().getReference("Users").child(phone).child("fav").child(name).orderByChild("name").equalTo(name);
        FAV.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    fav.setImageResource(R.drawable.fav);

                } else {
                    fav.setImageResource(R.drawable.download);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final int[] k = {0};
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query FAV=FirebaseDatabase.getInstance().getReference("Users").child(phone).child("fav").orderByChild("name").equalTo(name);
                FAV.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("fav").child(name);
                            db.removeValue();
                            Toast.makeText(getApplicationContext(), "AirBnB Removed From Your Favorite List", Toast.LENGTH_LONG).show();
                            fav.setImageResource(R.drawable.fav);

                        } else {
                            k[0]++;
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("fav").child(name);
                            SaveAndFav sf=new SaveAndFav(name);
                            db.child("name").setValue(name);
                            Toast.makeText(getApplicationContext(), "AirBnB Added To Your Favorite List", Toast.LENGTH_LONG).show();
                            co++;
                            fav.setImageResource(R.drawable.download);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query SAVE = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("save").orderByChild("name").equalTo(name);
                SAVE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("save").child(name);
                            db.removeValue();
                            Toast.makeText(getApplicationContext(), "AirBnB Removed From Your Watch Later List", Toast.LENGTH_LONG).show();
                            save.setImageResource(R.drawable.do1);

                        } else {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("save").child(name);
                            SaveAndFav sf=new SaveAndFav(name);
                            db.child("name").setValue(name);
                            Toast.makeText(getApplicationContext(), "AirBnB Added To Your Watch Later List", Toast.LENGTH_LONG).show();
                            co++;
                            save.setImageResource(R.drawable.save);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        rl = new rl4HR(AirBnBShowCase.this, list, name,"ShowCase");
        rl4HR1.setHasFixedSize(true);
        LinearLayoutManager li = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rl4HR1.setLayoutManager(li);
        rl4HR1.setAdapter(rl);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(AirBnBShowCase.this, R.style.BottomSheetDialogTheme);
                View bs = LayoutInflater.from(getApplicationContext()).inflate(R.layout.houserules, (LinearLayout) findViewById(R.id.houserules));
                bsd.setContentView(bs);
                bsd.show();
            }
        });

        amenities = (Button) findViewById(R.id.amenities);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        na.setText(name);
        amenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), ShowAmenties.class);
                in.putExtra("name", name);
                startActivity(in);
            }
        });
        DatabaseReference db34 = FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("rooms");


        db34.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Rooms roo = ds.getValue(Rooms.class);
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
                String email = d.child(name).child("email").getValue(String.class);
                String star = d.child(name).child("star").getValue(String.class);
                String des1 = d.child(name).child("des").getValue(String.class);
                String fullname = d.child(name).child("name").getValue(String.class);
                String phone2 = d.child(name).child("phone").getValue(String.class);
                String username = d.child(name).child("username").getValue(String.class);
                String ac = d.child(name).child("ac").getValue(String.class);
                String address = d.child(name).child("address").getValue(String.class);
                String url = d.child(name).child("url").getValue(String.class);
                String tv = d.child(name).child("tv").getValue(String.class);
                String fi = d.child(name).child("fi").getValue(String.class);
                String fia = d.child(name).child("fia").getValue(String.class);
                String wifi = d.child(name).child("wifi").getValue(String.class);
                String sp = d.child(name).child("sp").getValue(String.class);
                String plc = d.child(name).child("plc").getValue(String.class);
                String rs = d.child(name).child("rs").getValue(String.class);
                String rc = d.child(name).child("rc").getValue(String.class);
                String hd = d.child(name).child("hd").getValue(String.class);
                String sc = d.child(name).child("sc").getValue(String.class);
                ad.setText(address);
                des.setText(des1);
                lo.setText(address);
                //Picasso.with(HotelShowCase.this).load(url).fit().centerCrop().into(display);
                Picasso.get().load(url).fit().centerCrop().into(display);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if(isP==true) {
                    if(!ad.getText().toString().equals("")) {
                        String[] op = new String[1];
                        op[0] = na.getText().toString() + "," + ad.getText().toString();
                        startActivity(new Intent(getApplicationContext(), ActivityBeforeMap.class).putExtra("name", na.getText().toString() + ","+ad.getText().toString()).putExtra("op", op).putExtra("na","Hotel").putExtra("abid","1"));
                        //Toast.makeText(getApplicationContext(), ad.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Activity is getting updated. Please try again a few seconds later!!",Toast.LENGTH_LONG).show();

                }   else
                    Toast.makeText(getApplicationContext(),"Permission was not given",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_LONG).show();
                isP = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent in = new Intent();
                in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), " ");
                in.setData(uri);
                startActivity(in);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
}