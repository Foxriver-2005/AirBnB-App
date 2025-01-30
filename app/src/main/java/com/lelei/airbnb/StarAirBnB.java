package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Adapters.Rl3Adapter;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StarAirBnB extends AppCompatActivity {
    private RecyclerView rl;
    private List<String> li = new ArrayList<>();
    private List<String> image = new ArrayList<>();
    private Rl3Adapter rl3;
    private SearchView s;
    private ImageView backB;
    private TextView Danger, ht;
    private String star;
    private List<Users> listI = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_star_airbnbs);

        rl = findViewById(R.id.rl3);
        backB = findViewById(R.id.backButton);
        s = findViewById(R.id.serachView);
        Danger = findViewById(R.id.Danger);
        ht = findViewById(R.id.ht);

        star = getIntent().getStringExtra("Star");
        ht.setText("All " + star + " Hotels");
        //Toast.makeText(getApplicationContext(), star, Toast.LENGTH_LONG).show();

        EditText txtSearch = findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHint(getResources().getString(R.string.first));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.BLACK);

        rl.setLayoutManager(new LinearLayoutManager(this));
        rl3 = new Rl3Adapter(this, listI);
        rl.setAdapter(rl3);

        SessionManager sh = new SessionManager(StarAirBnB.this, SessionManagerAirBnBs.USERSESSION);
        HashMap<String, String> hm = sh.returnData();
        final String userName = hm.get(SessionManager.FULLNAME);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Hotels").child("Star").child(star);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                li.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Ensure the value is not null before calling toString
                    String hotelName = ds.getValue(String.class);
                    if (hotelName != null) {
                        li.add(hotelName);
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Hotels").child(hotelName);
                        db1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                String url = (String) dataSnapshot1.child("url").getValue();
                                if (url != null) {
                                    image.add(url);
                                    Users user = new Users(url, hotelName, "Expand All");
                                    listI.add(user);
                                    rl3.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle possible errors
                            }
                        });
                    }
                }
                if (!li.isEmpty()) {
                    Danger.setText(" ");
                } else {
                    Danger.setText("There are no " + star + " AirBnB's in our app!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rl3.getFilter().filter(newText);
                return false;
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}