package com.lelei.airbnb.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lelei.airbnb.ChangeRoomInfo;
import com.lelei.airbnb.Helpers.SessionManager;
import com.lelei.airbnb.LogIn_Or_SignUp;
import com.lelei.airbnb.Models.Rooms;
import com.lelei.airbnb.OrderReady;
import com.lelei.airbnb.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class rl4HR extends RecyclerView.Adapter<rl4HR.RoomsAd> {

    List<Rooms> list;
    Context c;
    String name1, price, name, n, what;
    public static String[] dele = new String[100];
    public static int d = 0;
    Dialog d1;

    public rl4HR(Context c, List<Rooms> list, String n, String what) {
        this.c = c;
        this.list = list;
        this.n = n;
        this.what = what;
    }

    @NonNull
    @Override
    public rl4HR.RoomsAd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roomsshowcase, parent, false);
        return new RoomsAd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomsAd holder, final int position) {
        if (!"Update".equals(what)) {
            holder.update.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
        holder.price.setText("Ksh" + list.get(position).getPrice());
        holder.service.setText(list.get(position).getServices());
        holder.rn.setText(list.get(position).getRoomname());
        //Picasso.with(c).load(list.get(position).getUrl()).fit().centerCrop().into(holder.Ab);
        Picasso.get().load(list.get(position).getUrl()).fit().centerCrop().into(holder.Ab);
        name1 = list.get(position).getRoomname();
        price = list.get(position).getPrice();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    dele[d] = list.get(currentPosition).getRoomname();
                    d++;
                    list.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Intent in = new Intent(c, ChangeRoomInfo.class);
                    in.putExtra("price", list.get(currentPosition).getPrice());
                    in.putExtra("roomname", list.get(currentPosition).getRoomname());
                    in.putExtra("services", list.get(currentPosition).getServices());
                    in.putExtra("hname", n);
                    c.startActivity(in);
                }
            }
        });

        if ("ShowCase".equals(what)) {
            holder.ca1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        SessionManager sh = new SessionManager(c, SessionManager.USERSESSION);
                        HashMap<String, String> hm = sh.returnData();
                        final String phone2 = hm.get(SessionManager.PHONE);
                        if (phone2 != null) {
                            Intent in = new Intent(c, OrderReady.class);
                            in.putExtra("Name", list.get(currentPosition).getRoomname());
                            in.putExtra("Price", list.get(currentPosition).getPrice());
                            in.putExtra("Rn", n);
                            c.startActivity(in);
                        } else {
                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(c);
                            alertDialog2.setTitle("Log In.");
                            alertDialog2.setMessage("Log In To Book Hotels");
                            alertDialog2.setIcon(R.drawable.apptitle);
                            alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    c.startActivity(new Intent(c, LogIn_Or_SignUp.class));
                                }
                            });
                            alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog2.show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class RoomsAd extends RecyclerView.ViewHolder {

        TextView price, service, rn;
        Button update, delete;
        CardView ca1;
        ImageView Ab;

        public RoomsAd(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            service = itemView.findViewById(R.id.service);
            rn = itemView.findViewById(R.id.rn);
            ca1 = itemView.findViewById(R.id.ca1);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            Ab = itemView.findViewById(R.id.Ab);
        }
    }
}