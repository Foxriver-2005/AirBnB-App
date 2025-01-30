package com.lelei.airbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lelei.airbnb.Helpers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CheckoutActivityJava extends AppCompatActivity {
    TextView card, postal, amount, des;
    String email, aemail, fbn;
    Button pa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stripe);

        card = findViewById(R.id.card);
        postal = findViewById(R.id.postal);
        pa = findViewById(R.id.pa);
        amount = findViewById(R.id.amount);
        des = findViewById(R.id.des);

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserPrfofilew.class));
                finish();
            }
        });

        Intent intent = getIntent();
        aemail = intent.getStringExtra("Email");

        // Get the payment details from intent extras
       // String paymentSuccess = intent.getBooleanExtra("paymentDetails");
        String phone = intent.getStringExtra("paymentDetails");
        //String paymentSuccess = intent.getStringExtra("paymentDetails");
        String paymentAmount = intent.getStringExtra("paymentAmount");
        String fullname = intent.getStringExtra("name");
        String roomName = intent.getStringExtra("rname");
        String roomCount = intent.getStringExtra("rCount");

        // Display details based on payment success
        if (phone != null) {
            des.setText("Your booking for \"" + fullname + "\" AirBnB for the room named " + roomName +
                    " (rooms count: " + roomCount + ") was successful.");

            // Assuming response is successful, populate other views
            card.setText("Payment number " + phone);
            amount.setText(paymentAmount + "Ksh.");
            postal.setText("Approved");

            // Send emails
            sendConfirmationEmails(fullname, roomName, roomCount);
        } else {
            // Handle payment failure scenario
            des.setText("Payment for booking \"" + fullname + "\" AirBnB failed.");
        }
    }

    private void sendConfirmationEmails(String hname, String rname, String q) {
        SessionManager sh = new SessionManager(this, SessionManager.USERSESSION);

        HashMap<String, String> hm = sh.returnData();
        email = hm.get(SessionManager.EMAIL);
        fbn = hm.get(SessionManager.FULLNAME);
        String phone = hm.get(SessionManager.PHONE);

        String subject = "Your Booking for AirBnB " + hname + " was successful" + "\n\n" +
                "Transaction Mpesa Number: "+ phone + "\nAmount: " + amount.getText().toString();

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
            // Send email to user
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Successful AirBnB Booking");
            message.setText(subject);

            new SendEmail().execute(message);

            // Send email to admin
            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress(Email));
            message1.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aemail));
            String s = "AirBnB " + hname + " has new orders." + "\nRoom Name: " + rname +
                    "\nRooms Count: " + q + "\n Customer Details: \nName: " +
                    fbn + "\nEmail: " + email + "\nPhone No: " + phone;
            message1.setSubject("New Order");
            message1.setText(s);

            new SendEmail().execute(message1);

        } catch (MessagingException e) {
            e.printStackTrace();
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