package com.lelei.airbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.Earning;
import com.lelei.airbnb.Models.ShowingData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AirBnBOverview extends AppCompatActivity implements OnChartValueSelectedListener {
    LineChart line;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    String name, phone;
    float da[] = new float[32];
    float value[] = new float[32];
    int va = 0;
    int po = 0;
    LineData d;
    String month = "";
    LineDataSet ar = new LineDataSet(null, null);
    ArrayList<ILineDataSet> abc = new ArrayList<>();
    LineData ld;
    HashMap<String,Integer> hm345=new HashMap<>();
    BarChart b;
    Dialog d1;
    int cmonth;
    float avg=0;
    TextView orders,head,earning,avgO,avgE;
    int cday,cy;
    float avg1;
    ImageView plusicon, ordPDF;
    long earning1,totalOrders;
    Earning ds;
    LinearLayout inte;
    String months[]={"january","february","march","april","may","june","july","august","september","october","november","december"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_airbnbs_overview);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with your logic
            setupPDFGeneration();
        }

        line = (LineChart) findViewById(R.id.line);
        //line.setOnChartGestureListener(this);
        line.setOnChartValueSelectedListener(this);
        line.setDragEnabled(true);
        line.setScaleEnabled(false);
        head=(TextView)findViewById(R.id.head);
        inte=(LinearLayout)findViewById(R.id.inte);
        if(!isWiConnected(AirBnBOverview.this))
        {
            head.setText("Connect To Internet To Access This App's Features!!!");
            inte.setVisibility(View.GONE);
            showCustomDialog();
        }
        else {
            b = (BarChart) findViewById(R.id.bar);
            plusicon=(ImageView)findViewById(R.id.plusicon);
            plusicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), AirBnBRooms.class));
                }
            });
            orders = (TextView) findViewById(R.id.orders);
            earning = (TextView) findViewById(R.id.earning);
            avgO = (TextView) findViewById(R.id.avgO);
            avgE = (TextView) findViewById(R.id.avgE);
            ordPDF = (ImageView) findViewById(R.id.ordPDF);

            ordPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generatePdf();
                }
            });


            SessionManagerAirBnBs sh = new SessionManagerAirBnBs(this, SessionManagerAirBnBs.USERSESSION);
            HashMap<String, String> hm = sh.returnData();
            name = hm.get(SessionManagerAirBnBs.FULLNAME);
            FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Earning").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ds = dataSnapshot.getValue(Earning.class);
                    totalOrders = ds.getOrders();
                    earning1 = ds.getEarning();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            head.setText(name);

            phone = hm.get(SessionManagerAirBnBs.PHONE);
            Calendar c23 = Calendar.getInstance();
            cmonth = c23.get(Calendar.MONTH);
            cy = c23.get(Calendar.YEAR);
            cday = c23.get(Calendar.DAY_OF_MONTH);

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
            FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("Opening").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long omon = (long) dataSnapshot.child("omon").getValue();
                    long oyear = (long) dataSnapshot.child("oyear").getValue();
                    if (omon > cmonth) {
                        avg = earning1 / (omon + cmonth);
                        avg1 = totalOrders / (omon + cmonth);
                    } else {
                        if (oyear == cy) {
                            if (cmonth == omon) {
                                avg = earning1;
                                avg1 = totalOrders;
                            } else {
                                avg = earning1 / (cmonth - omon);
                                avg1 = totalOrders / (cmonth - omon);
                            }
                        } else {
                            avg = earning1 / (cmonth - omon) * ((cy - oyear));
                            avg1 = totalOrders / (cmonth - omon) * ((cy - oyear));
                        }
                    }
                    avgO.setText(avg1 + "");
                    avgE.setText(avg + "");
                    orders.setText(totalOrders + "");
                    earning.setText(earning1 + "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("OrderMonths").addValueEventListener(new ValueEventListener() {

                ArrayList<BarEntry> bar = new ArrayList<>();
                ArrayList<String> label = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bar.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ShowingData shdt = ds.getValue(ShowingData.class);
                        hm345.put(shdt.getMonth(), shdt.getOrders());
                        //Toast.makeText(getApplicationContext(),shdt.getMonth()+" "+shdt.getOrders(),Toast.LENGTH_LONG).show();
                    }
                    for (int i = 0; i < cmonth + 1; i++) {
                        if (hm345.get(months[i]) != null) {
                            bar.add(new BarEntry(i, hm345.get(months[i])));
                            //Toast.makeText(getApplicationContext(), months[i] + " " + hm345.get(months[i]), Toast.LENGTH_LONG).show();


                        } else
                            bar.add(new BarEntry(i, 0));
                        label.add(months[i]);
                        Log.d(months[i], hm345.get(months[i]) + "");

                    }
                    showBarGraph(bar, label);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            for (int i = 0; i < 31; i++)
                da[i] = 0;
            month = month.toLowerCase();
            final String finalMonth = month;
            FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("DataSet").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Entry> data = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ShowingData sho = ds.getValue(ShowingData.class);
                        int tday = sho.getDay();
                        String tmonth = sho.getMonth().toLowerCase();
                        float ord = sho.getOrders();
                        if (tmonth.equals(finalMonth)) {

                            da[tday] = ord;
                        }

                    }
                    for (int i = 1; i <= cday; i++) {
                        if (da[i] == 0)
                            data.add(new Entry(i, 0));
                        else
                            data.add(new Entry(i, da[i]));
                    }
                    showChart(data);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void showBarGraph(ArrayList<BarEntry> bar, ArrayList<String> label) {
        BarDataSet barD=new BarDataSet(bar,"Monthly Orders");
        barD.setColors(ColorTemplate.COLORFUL_COLORS);
        Description des=new Description();
        des.setText("Year "+2024);
        b.setDescription(des);
        BarData ba=new BarData(barD);
        b.setData(ba);
        XAxis xa=b.getXAxis();
        xa.setValueFormatter(new IndexAxisValueFormatter(label));
        xa.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xa.setDrawAxisLine(false);
        xa.setDrawGridLines(false);
        xa.setGranularity(1f);
        xa.setLabelCount(cmonth+1);
        xa.setLabelRotationAngle(0);
        b.animateY(2000);
        b.getXAxis().setTextColor(Color.WHITE);
        b.getBarData().setValueTextColor(Color.WHITE);
        b.getBarData().setValueTextSize(10f);
        b.getLegend().setTextColor(Color.WHITE);

        b.getDescription().setTextColor(Color.WHITE);
        b.getDescription().setPosition(320,80);
        b.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String x = e.getX() + "";
                String x1 = "";
                String y1 = "";
                String y = e.getY() + "";
                for (int i = 0; i < x.length(); i++) {
                    if (x.charAt(i) == '.')
                        break;
                    x1 += x.charAt(i);
                }
                for (int i = 0; i < y.length(); i++) {
                    if (y.charAt(i) == '.')
                        break;
                    y1 += y.charAt(i);
                }
                View view = LayoutInflater.from(AirBnBOverview.this).inflate(R.layout.airbnbline, null, false);
                TextView date = (TextView) view.findViewById(R.id.date);
                TextView re = (TextView) view.findViewById(R.id.re);
                String s = date.getText().toString();
                String s1 = re.getText().toString();
                date.setText(s + x + " " + month);

                if (!y1.equals("0")) {

                    re.setText(s1 + y1 + "");
                } else {
                    re.setText("You have received no orders on the month of " + x);
                }

                AlertDialog.Builder al = new AlertDialog.Builder(AirBnBOverview.this);
                al.setView(view);
                al.setCancelable(true);
                AlertDialog alertDialog;
                alertDialog = al.create();
                al.show();


            }

            @Override
            public void onNothingSelected() {

            }
        });
        b.invalidate();
    }
    public void showChart(ArrayList<Entry> data) {
        ar.setValues(data);
        ar.setFillAlpha(110);
        ar.setLineWidth(5f);
        ar.setFormSize(20f);
        ar.setCircleColor(R.color.makeUplight);
        ar.setLabel("Current Month Order: ");
        abc.clear();
        abc.add(ar);
        Description des=new Description();

        des.setText("Report of "+month);

        line.setDescription(des);
        line.getDescription().setPosition(220,80);
        line.getDescription().setTextColor(Color.WHITE);
        line.getXAxis().setTextColor(Color.WHITE);
        line.getLegend().setTextColor(Color.WHITE);
        ld = new LineData(abc);
        YAxis rightAxis = line.getAxisRight();

        //Set the y-axis on the right of the chart to be disabled
        rightAxis.setEnabled(true);
        YAxis leftAxis = line.getAxisLeft();
        //Set the y-axis on the left of the chart to be disabled
        leftAxis.setEnabled(true);
        line.getAxisRight().setTextColor(Color.WHITE);
        line.getAxisLeft().setTextColor(Color.WHITE);
        //Set the x axis
        XAxis xAxis = line.getXAxis();
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//Whether to draw the axis
        xAxis.setDrawGridLines(false);//Set the line corresponding to each point on the x-axis
        xAxis.setDrawLabels(true);//Draw label refers to the corresponding value on the x axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Set the display position of the x axis
        xAxis.setGranularity(1f);//X-axis label redrawing is prohibited after zooming in
        line.clear();
        line.setData(ld);
        line.invalidate();

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String x = e.getX() + "";
        String x1 = "";
        String y1 = "";
        String y = e.getY() + "";
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '.')
                break;
            x1 += x.charAt(i);
        }
        for (int i = 0; i < y.length(); i++) {
            if (y.charAt(i) == '.')
                break;
            y1 += y.charAt(i);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.airbnbline, null, false);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView re = (TextView) view.findViewById(R.id.re);
        String s = date.getText().toString();
        String s1 = re.getText().toString();
        date.setText(s + x1 + " " + month);

        if (!y1.equals("0")) {

            re.setText(s1 + y1 + "");
        } else {
            re.setText("You have recieved no orders on " + date.getText().toString());
        }

        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setView(view);
        al.setCancelable(true);
        AlertDialog alertDialog;
        alertDialog = al.create();
        al.show();


    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                this);

// Setting Dialog Title
        alertDialog2.setTitle("Exit");

// Setting Dialog Message
        alertDialog2.setMessage("Are You Sure Want To Exit??");
        alertDialog2.setIcon(R.drawable.apptitle);
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

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
    public boolean isWiConnected(AirBnBOverview l) {
        ConnectivityManager c = (ConnectivityManager) l.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wi = c.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mi = c.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wi != null && wi.isConnected()) || (mi != null && mi.isConnected())) {
            return true;
        } else
            return false;

    }

    private void showCustomDialog() {
        String x="LogIn_Or_SignUp";
        d1 = new Dialog(AirBnBOverview.this);
        d1.setContentView(R.layout.custom);
        d1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.customdraw));
        d1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d1.setCancelable(false);
        d1.getWindow().getAttributes().windowAnimations = R.style.animate;
        d1.show();
        Button b1 = d1.findViewById(R.id.cancel);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
            }

        });

        Button b2 = d1.findViewById(R.id.connect);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with PDF generation setup
                setupPDFGeneration();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupPDFGeneration() {
//        ordPDF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                generatePdf();
//            }
//        });
    }

    private void generatePdf() {
        SessionManagerAirBnBs sh = new SessionManagerAirBnBs(this, SessionManagerAirBnBs.USERSESSION);
        String hotelName = sh.returnData().get(SessionManagerAirBnBs.FULLNAME);

        FirebaseDatabase.getInstance().getReference("Hotels").child(hotelName).child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map<String, Object>> orders = new ArrayList<>();
                float totalAmount = 0.0f;

                // Extract data into a list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                    if (map != null) {
                        orders.add(map);

                        // Calculate total amount
                        String priceStr = (String) map.get("price");
                        if (priceStr != null && priceStr.endsWith("Ksh.")) {
                            try {
                                float price = Float.parseFloat(priceStr.replace("Ksh.", "").trim());
                                totalAmount += price;
                            } catch (NumberFormatException e) {
                                e.printStackTrace(); // Handle the case where the price is not a valid float
                            }
                        }
                    }
                }

                // Log total amount for debugging
                Log.d("PDFGeneration", "Total Amount: " + totalAmount);

                // Sort the list by "issueDate"
                Collections.sort(orders, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        String issueDate1 = (String) o1.get("issueDate");
                        String issueDate2 = (String) o2.get("issueDate");

                        // Convert issue dates to Date objects for comparison
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
                        try {
                            Date date1 = dateFormat.parse(issueDate1);
                            Date date2 = dateFormat.parse(issueDate2);
                            return date1.compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0; // Default to no sorting if parsing fails
                        }
                    }
                });

                // Create PDF file
                try {
                    File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    String filePath = downloadFolder + "/AirBnBOrders.pdf";
                    PdfWriter writer = new PdfWriter(filePath);
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document document = new Document(pdfDoc, PageSize.A4);

                    // Define fonts
                    PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                    PdfFont subHeaderFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                    // Title
                    Paragraph title = new Paragraph("AirBnB Name: " + hotelName)
                            .setFont(headerFont)
                            .setFontSize(24)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom(10);

                    // Subheading
                    Paragraph subheading = new Paragraph("AirBnb orders Report as of " + getCurrentDateTime())
                            .setFont(subHeaderFont)
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom(20);

                    // Add title and subheading to document
                    document.add(title);
                    document.add(subheading);

                    // Table
                    Table table = new Table(new float[]{1, 2, 2, 1, 1});
                    table.addHeaderCell(new Paragraph("Customer Details").setFont(headerFont).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Paragraph("In Date").setFont(headerFont).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Paragraph("Out Date").setFont(headerFont).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Paragraph("Rooms Count").setFont(headerFont).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Paragraph("Price").setFont(headerFont).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                    // Add sorted data to the table
                    for (Map<String, Object> map : orders) {
                        table.addCell(new Paragraph(map.get("cname") != null ? map.get("cname").toString() : "N/A").setFont(subHeaderFont));
                        table.addCell(new Paragraph(map.get("issueDate") != null ? map.get("issueDate").toString() : "N/A").setFont(subHeaderFont));
                        table.addCell(new Paragraph(map.get("endDate") != null ? map.get("endDate").toString() : "N/A").setFont(subHeaderFont));
                        table.addCell(new Paragraph(map.get("roomscount") != null ? map.get("roomscount").toString() : "N/A").setFont(subHeaderFont));
                        table.addCell(new Paragraph(map.get("price") != null ? map.get("price").toString() : "N/A").setFont(subHeaderFont));
                    }

                    // Add the total amount row in a separate table
                    Table totalTable = new Table(new float[]{1, 1});
                    totalTable.addCell(new Cell().add(new Paragraph("Total Amount:"))
                            .setFont(headerFont)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT));
                    totalTable.addCell(new Cell().add(new Paragraph(String.format("%.2f Ksh.", totalAmount))
                            .setFont(headerFont)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.RIGHT)));

                    // Add all tables to document
                    document.add(table);
                    document.add(totalTable);

                    document.close();

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "PDF generated successfully", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Open", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Open the PDF file
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri pdfUri = FileProvider.getUriForFile(AirBnBOverview.this, getPackageName() + ".provider", new File(filePath));
                            intent.setDataAndType(pdfUri, "application/pdf");
                            startActivity(intent);
                        }
                    });
                    snackbar.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Failed to create PDF: ",e.getMessage());
                    Toast.makeText(AirBnBOverview.this, "Failed to create PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AirBnBOverview.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
