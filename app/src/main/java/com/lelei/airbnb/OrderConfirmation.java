package com.lelei.airbnb;

import static com.lelei.airbnb.Mpesa.Constants.BUSINESS_SHORT_CODE;
import static com.lelei.airbnb.Mpesa.Constants.CALLBACKURL;
import static com.lelei.airbnb.Mpesa.Constants.PARTYB;
import static com.lelei.airbnb.Mpesa.Constants.PASSKEY;
import static com.lelei.airbnb.Mpesa.Constants.TRANSACTION_TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.Models.Config;
import com.lelei.airbnb.Models.Earning;
import com.lelei.airbnb.Models.OrderShow;
import com.lelei.airbnb.Models.ShowingData;
import com.lelei.airbnb.Mpesa.AccessToken;
import com.lelei.airbnb.Mpesa.DarajaApiClient;
import com.lelei.airbnb.Mpesa.STKPush;
import com.lelei.airbnb.Mpesa.Utils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmation extends AppCompatActivity {
    public boolean isAuthTokenReceived = false;
    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    TextView cdate,start,end,rname,qua,total,total1,hdes,cdes;
    EditText edtMpesa;
    String cdes1,start1,end1,rname1,qua1,total2,total3,hdes1,name;
    long tot;
    ImageView back;
    Button confirm;
    String diff;
    long di;
    String n1;
    int q;
    String phone,mpesaNo;
    Calendar cal=Calendar.getInstance();
    String rn12,HNAME=" ";
    String email;
    String fullname;
    String price;
    static int totalOrders;
    long jk;
    long ear=0,oRd=0;

    static long earning=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_order_confirmation);
        SessionManager sh=new SessionManager(this,SessionManager.USERSESSION);

        HashMap<String,String> hm=sh.returnData();
        final String n=hm.get(SessionManager.FULLNAME);
        phone=hm.get(SessionManager.PHONE);
        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        cdate=(TextView)findViewById(R.id.cdate);
        confirm=(Button)findViewById(R.id.confirm);

        rn12=getIntent().getStringExtra("rn12");
        Query c = FirebaseDatabase.getInstance().getReference("Hotels").orderByChild("name").equalTo(rn12);
        c.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot d) {
                fullname = d.child(rn12).child("name").getValue(String.class);

                email = d.child(rn12).child("email").getValue(String.class);

                String address=d.child(rn12).child("address").getValue(String.class);
                hdes.setText(rn12+"\n"+address);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        start=(TextView)findViewById(R.id.start);
        end=(TextView)findViewById(R.id.end);
        rname=(TextView)findViewById(R.id.rname);
        qua=(TextView)findViewById(R.id.qua);
        total=(TextView)findViewById(R.id.total);
        total1=(TextView)findViewById(R.id.total1);
        hdes=(TextView)findViewById(R.id.hdes);
        cdes=(TextView)findViewById(R.id.cdes);
        cdes.setText(n+"\n"+"Phone: "+phone);
        back=(ImageView)findViewById(R.id.back);
        edtMpesa = findViewById(R.id.mpesaNo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        qua1=getIntent().getStringExtra("Qua");
        start1=getIntent().getStringExtra("Start");
        end1=getIntent().getStringExtra("End");
        start.setText(start1);
        end.setText(end1);
        name=getIntent().getStringExtra("Name");
        q=Integer.parseInt(qua1);
        qua.setText(qua1);
        price=getIntent().getStringExtra("Price");
        tot=Integer.parseInt(price);

        rname.setText(name);
        total.setText(price+"Ksh.");
        total1.setText("Total: "+price+"Ksh.");
        cdate.setText(getIntent().getStringExtra("Cdate"));



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpesaNo = edtMpesa.getText().toString().trim();
                if (mpesaNo.isEmpty()) {
                    edtMpesa.setError("Paying number is required");
                }
                else if (mpesaNo.startsWith("07") || mpesaNo.startsWith("01")) {
                    // Check for Safaricom numbers (starts with "07" or "01")
                    if (mpesaNo.length() != 10) {
                        edtMpesa.setError("Enter valid phone number (10 digits starting with '07' or '01')");
                    } else {
                        getAccessToken(); // Valid Safaricom number, proceed with payment
                    }
                }
                else if (mpesaNo.startsWith("+254")) {
                    // Check for numbers starting with "+254"
                    if (mpesaNo.length() != 13) {
                        edtMpesa.setError("Enter valid phone number (+254 followed by 9 digits)");
                    } else {
                        getAccessToken(); // Valid number starting with "+254", proceed with payment
                    }
                }
                else if (mpesaNo.startsWith("254")) {
                    // Check for numbers starting with "254"
                    if (mpesaNo.length() != 12) {
                        edtMpesa.setError("Enter valid phone number (254 followed by 9 digits)");
                    } else {
                        getAccessToken(); // Valid number starting with "254", proceed with payment
                    }
                }
                else {
                    edtMpesa.setError("Enter valid phone number starting with '07', '01', '+254' or '254'");
                }
            }
        });

    }
    public void performSTKPush(String mpesaNo,String amount) {
        mProgressDialog.setMessage("Processing Payment....");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(mpesaNo),
                PARTYB,
                Utils.sanitizePhoneNumber(mpesaNo),
                CALLBACKURL,
                "AirBnB", //Account reference
                "We prioritize our customers"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                System.out.println("post submitted to API. %s "+response.body());
                                try {

                                    Random r12n=new Random();
                                    long yui=r12n.nextInt(10000000);
                                    FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("Earning").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Earning ds=dataSnapshot.getValue(Earning.class);
                                            oRd=ds.getOrders();
                                            ear=ds.getEarning();
                                            ear+=tot;
                                            oRd+=1;
                                            HashMap uio=new HashMap();
                                            uio.put("earning",ear);
                                            uio.put("orders",oRd);
                                            FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("Earning").updateChildren(uio);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    //String phoneWithPlus = "+" + phone;
                                    OrderShow or=new OrderShow(cdes.getText().toString(),hdes.getText().toString(),tot+"Ksh.",q+"",start1+"",end1+"",rname.getText().toString(),cdate.getText().toString(),"foxriver2471@gmail.com");
                                    DatabaseReference d=FirebaseDatabase.getInstance().getReference("Users").child(phone).child("Order");
                                    or.setCname(cdes.getText().toString());
                                    or.setHname(hdes.getText().toString());
                                    or.setRname(rname.getText().toString());
                                    //Toast.makeText(getApplicationContext(),fullname,Toast.LENGTH_LONG).show();
                                    DatabaseReference ho=FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("Order");
                                    //Toast.makeText(getApplicationContext(),fullname,Toast.LENGTH_LONG).show();
                                    ho.child("order"+yui).setValue(or);
                                    d.child("order"+yui).setValue(or);


                                    final int cmonth=cal.get(Calendar.MONTH);
                                    String month="";
                                    if (cmonth == 1 - 1)
                                        month = "January";
                                    else if (cmonth == 2 - 1)
                                        month = "February";
                                    else if (cmonth == 3 - 1)
                                        month = "March";
                                    else if (cmonth == 4 - 1)
                                        month = "April";
                                    else if (cmonth == 5 - 1)
                                        month = "May";
                                    else if (cmonth == 6 - 1)
                                        month = "June";
                                    else if (cmonth == 7 - 1)
                                        month = "July";
                                    else if (cmonth == 8 - 1)
                                        month = "August";
                                    else if (cmonth == 9 - 1)
                                        month = "September";
                                    else if (cmonth == 10 - 1)
                                        month = "October";
                                    else if (cmonth == 11 - 1)
                                        month = "November";
                                    else month = "December";
                                    month=month.toLowerCase();
                                    Query orM=FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("OrderMonths").orderByChild("month").equalTo(month);
                                    final String finalMonth = month;
                                    orM.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                long orders= (long) dataSnapshot.child(finalMonth).child("orders").getValue();
                                                orders++;

                                                HashMap o=new HashMap();
                                                o.put("orders",orders);
                                                FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("OrderMonths").child(finalMonth).updateChildren(o);
                                            }
                                            else
                                            {
                                                ShowingData shp=new ShowingData(finalMonth,1);
                                                FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("OrderMonths").child(finalMonth).setValue(shp);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    Query c = FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("DataSet").orderByChild("date").equalTo(cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.MONTH)+" "+cal.get(Calendar.YEAR));
                                    c.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                long orders= (long) dataSnapshot.child(cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.MONTH)+" "+cal.get(Calendar.YEAR)).child("orders").getValue();
                                                orders++;

                                                HashMap o=new HashMap();
                                                o.put("orders",orders);
                                                FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("DataSet").child(cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.MONTH)+" "+cal.get(Calendar.YEAR)).updateChildren(o);
                                            }
                                            else
                                            {


                                                ShowingData sho=new ShowingData(cdate.getText().toString(),1, cal.get(Calendar.DAY_OF_MONTH),finalMonth);
                                                FirebaseDatabase.getInstance().getReference("Hotels").child(fullname).child("DataSet").child(cdate.getText().toString()).setValue(sho);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    FirebaseDatabase.getInstance().getReference("Users").child(phone).child("Payment").child(q+start1+end1+getIntent().getStringExtra("Price")+""+getIntent().getStringExtra("Cdate")).removeValue();
                                    Intent intent = new Intent(OrderConfirmation.this, CheckoutActivityJava.class);
                                            intent.putExtra("paymentDetails",mpesaNo);
                                            intent.putExtra("paymentAmount",tot+" ");
                                            intent.putExtra("name",fullname);
                                            intent.putExtra("rname",name).putExtra("rCount",q+" ").putExtra("User",phone).putExtra("Email",email);
                                            startActivity(intent);
                                            finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                System.out.println("Response %s "+response.errorBody().string());
                                edtMpesa.setError("Invalid Paying Number");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },5000);

            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                    isAuthTokenReceived = true;

                    // Get the total amount from the price field
                    int totalAmount = Integer.parseInt(price);

                    // Calculate 10% of the total amount
                    int percentageAmount = (int) Math.round(totalAmount * 0.10);

                    // Prepare the phone number
                    String mPesa = edtMpesa.getText().toString().trim();
                    if (mPesa.startsWith("+")) {
                        mPesa = mPesa.substring(1); // Remove the leading '+'
                    }

                    // Perform the STK Push with the calculated amount
                    performSTKPush(mPesa, String.valueOf(percentageAmount));
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                // Handle token retrieval failure
            }
        });
    }


}
