package com.lelei.airbnb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lelei.airbnb.Helpers.FcmNotificationsSender;
import com.lelei.airbnb.Helpers.SessionManagerAirBnBs;
import com.lelei.airbnb.Models.OrderShow;
import com.lelei.airbnb.R;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.NEED> {
    List<OrderShow> list;
    Context c;
    static String dele[] = new String[100];
    static int d = 0;
    int len = 0;
    String cname, hname, start1, end1, idate1, price, qua, rn;

    public ApprovalAdapter(Context c, List<OrderShow> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public NEED onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderadapter, parent, false);
        return new ApprovalAdapter.NEED(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NEED holder, final int i) {
        if (i + 1 == 1)
            holder.count.setText("1st Order Approval\nSeeMore...");
        else if (i + 1 == 2)
            holder.count.setText("2nd Order Approval\nSeeMore...");
        else if (i + 1 == 3)
            holder.count.setText("3rd Order Approval\nSeeMore...");
        else
            holder.count.setText(i + 1 + "th Order Approval\nSeeMore...");

        holder.cdate.setText(list.get(i).getIdate());
        holder.end.setText(list.get(i).getEndDate());
        holder.start.setText(list.get(i).getIssueDate());
        holder.cdes.setText(list.get(i).getCname());
        len = list.get(i).getCname().length();
        cname = list.get(i).getCname();
        holder.hdes.setText(list.get(i).getHname());
        holder.total.setText(list.get(i).getPrice());
        end1 = list.get(i).getEndDate();
        hname = list.get(i).getHname();
        price = list.get(i).getPrice();
        qua = list.get(i).getRoomscount();
        start1 = list.get(i).getIssueDate();
        idate1 = list.get(i).getIdate();
        rn = list.get(i).getRname();
        holder.rname.setText(list.get(i).getRname());
        holder.qua.setText(list.get(i).getRoomscount());

        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.visi.setVisibility(View.VISIBLE);
                holder.count.setVisibility(View.GONE);
                holder.approve.setVisibility(View.VISIBLE);
                holder.disApprove.setVisibility(View.VISIBLE);

                holder.approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SessionManagerAirBnBs sh = new SessionManagerAirBnBs(c, SessionManagerAirBnBs.USERSESSION);
                        HashMap<String, String> hm = sh.returnData();
                        String name = hm.get(SessionManagerAirBnBs.FULLNAME);
                        String emai = hm.get(SessionManagerAirBnBs.EMAIL);

                        String subject = "Your Last Order Has Been Approved. Now go to the Pay Now section of your profile to complete the booking";
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
                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(list.get(i).getEmail()));
                            message.setSubject("Successful");
                            message.setText(subject);

                            new ApprovalAdapter.SendEmail().execute(message);

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }

                        FirebaseDatabase.getInstance().getReference("Hotels").child(name).child("needApp").child(start1 + end1 + price + rn + idate1).removeValue();
                        list.remove(i);
                        notifyItemRemoved(i);

                        int k = 0;
                        String userNum = "";
                        for (int j = len - 1; ; j--) {
                            if (k == 9)
                                break;
                            userNum += cname.charAt(j);
                            k++;
                        }
                        StringBuilder input1 = new StringBuilder();
                        input1.append(userNum);
                        input1.reverse();
                        userNum = input1.toString();
                        userNum = "+254" + userNum;

                        OrderShow or = new OrderShow(cname, hname, price, qua, start1, end1, rn, idate1, emai);
                        FirebaseDatabase.getInstance().getReference("Users").child(userNum)
                                .child("Payment").child(qua + start1 + end1 + price + idate1).setValue(or);

                        FirebaseDatabase.getInstance().getReference("Users").child(userNum).child("Token").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && dataSnapshot.child("token").exists()) {
                                    String token = dataSnapshot.child("token").getValue(String.class);
                                    if (token != null) {
                                        FcmNotificationsSender fcm = new FcmNotificationsSender(token, "Successful", "Your last order has been approved.", c, (Activity) c);
                                        fcm.SendNotifications();
                                    } else {
                                        Toast.makeText(c, "Token is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(c, "Token not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error
                                Toast.makeText(c, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                holder.disApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dele[d] = list.get(i).getIssueDate() + list.get(i).getEndDate();
                        d++;
                        list.remove(i);
                        notifyItemRemoved(i);

                        int k = 0;
                        String userNum = "";
                        for (int j = len - 1; ; j--) {
                            if (k == 11)
                                break;
                            userNum += cname.charAt(j);
                            k++;
                        }
                        StringBuilder input1 = new StringBuilder();
                        input1.append(userNum);
                        input1.reverse();
                        userNum = input1.toString();
                        userNum = "+254" + userNum;

                        FirebaseDatabase.getInstance().getReference("Users").child(userNum).child("Token").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && dataSnapshot.child("token").exists()) {
                                    String token = dataSnapshot.child("token").getValue(String.class);
                                    if (token != null) {
                                        FcmNotificationsSender fcm = new FcmNotificationsSender(token, "Successful", "Your last order has been disapproved.", c, (Activity) c);
                                        fcm.SendNotifications();
                                    } else {
                                        Toast.makeText(c, "Token is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(c, "Token not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error
                                Toast.makeText(c, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                holder.off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.visi.setVisibility(View.GONE);
                        holder.count.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class NEED extends RecyclerView.ViewHolder {
        TextView cdate, start, end, hdes, cdes, rname, qua, total, count, off;
        CardView ca1;
        LinearLayout visi;
        Button approve, disApprove;

        public NEED(@NonNull View itemView) {
            super(itemView);
            cdate = itemView.findViewById(R.id.cdate);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            hdes = itemView.findViewById(R.id.hdes);
            cdes = itemView.findViewById(R.id.cdes);
            rname = itemView.findViewById(R.id.rname);
            qua = itemView.findViewById(R.id.qua);
            total = itemView.findViewById(R.id.total);
            visi = itemView.findViewById(R.id.visi);
            count = itemView.findViewById(R.id.count);
            off = itemView.findViewById(R.id.off);
            approve = itemView.findViewById(R.id.approve);
            disApprove = itemView.findViewById(R.id.disApprove);
        }
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