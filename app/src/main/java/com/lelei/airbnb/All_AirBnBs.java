package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.lelei.airbnb.Models.AirBnBShow;
import com.lelei.airbnb.Models.Users;

import java.util.ArrayList;
import java.util.List;

public class All_AirBnBs extends AppCompatActivity {
    private RecyclerView rl;
    List<String> li = new ArrayList<>();
    List<String> image = new ArrayList<>();
    Rl3Adapter rl3;
    SearchView s;
    ImageView backB;
    Users it;
    int count;
    TextView nameOfDiv,empty;
    List<Users> listI = new ArrayList<>();
    ProgressDialog pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        pr=new ProgressDialog(this);
        pr.show();
        pr.setContentView(R.layout.progress);
        pr.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_all_airbnbs);
        rl = (RecyclerView) findViewById(R.id.rl3);
        nameOfDiv = (TextView) findViewById(R.id.nameOfDiv);
        empty = (TextView) findViewById(R.id.empty);
        backB = (ImageView) findViewById(R.id.backButton);
        s = (SearchView) findViewById(R.id.serachView);
        if(getIntent().getStringExtra("com")!=null&&!getIntent().getStringExtra("com").equals("no")){
            nameOfDiv.setText("Your searched results: ");
        }

        EditText txtSearch = ((EditText)s.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.first));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.BLACK);
        rl.setLayoutManager(new LinearLayoutManager(this));
        rl3 = new Rl3Adapter(this, listI);
        rl.setAdapter(rl3);

        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Hotels").child("Hotel Names");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                li.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    AirBnBShow hr=ds.getValue(AirBnBShow.class);
                    li.add(hr.getName());
                    count++;
                    if(getIntent().getStringExtra("com").equals("no"))
                    {
                        Users it1 = new Users(hr.getUrl(), hr.getName(), "Expand All");
                        listI.add(it1);
                    }
                    else{
                        String COM=hr.getName().toLowerCase();
                        String COM1=getIntent().getStringExtra("com").toLowerCase();
                        if(COM.contains(COM1)){
                            Users it1 = new Users(hr.getUrl(), hr.getName(), "Expand All");
                            listI.add(it1);
                        }
                    }



                }

                rl3.notifyDataSetChanged();
                if(listI.size()==0)
                {
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("No Results Found");
                }
                pr.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        search();
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    private void search() {
        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //  rl3.getFilter().filter(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                rl3.getFilter().filter(s.toString());
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        All_AirBnBs.super.onBackPressed();
    }
}