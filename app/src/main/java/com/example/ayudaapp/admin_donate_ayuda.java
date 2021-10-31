package com.example.ayudaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class admin_donate_ayuda extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String number = "09293199016";
    String message = "Thank you for using AyudApp. OTP is: ";
    DrawerLayout dLayout;
    String username;
    String password;
    String OTP;
    DBAdapter helper;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_ayuda);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");


        final Activity activity = this;
        activity.setTitle("Distribute Ayuda");

        helper = new DBAdapter(this);

        ArrayList<String> creds = new ArrayList<>();
//        creds = helper.getAdminName(username, password);
        OTP = helper.generateID();

        navigationObjects();

        Button verify_button = findViewById(R.id.verify_button);
        EditText ID = findViewById(R.id.familyid_personid);
        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID.getText().toString().split("-").length < 2 || ID.getText().toString().split("-").length > 2) {
                    ID.setError("Invalid Input: Format must me xxxxxxxx-xx i.e: 12345678-01");
                } else {
                    AlertDialog diaBox = AskOption(ID.getText().toString());
                    diaBox.show();
                }
            }
        });

    }

    private AlertDialog AskOption(String ID) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Review your information")
                .setMessage("The ID you entered is: " + ID + " is this correct?")
                .setIcon(R.drawable.warning)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String temp[] = ID.split("-");
                        Intent i = new Intent(getApplicationContext(), admin_verify_ayuda.class);
                        number = helper.returnContactNo(temp[1]);
                        message = message + OTP;
                        i.putExtra("reference", ID);
                        i.putExtra("OTP", OTP);
                        i.putExtra("familyID", temp[0]);
                        i.putExtra("username", username);
                        i.putExtra("password", password);

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "One Time Pin has been sent to the client, re-enter for verification", Toast.LENGTH_LONG).show();


                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(number, null, message, pi, null);

//                        sendToOtherPage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void sendToOtherPage() {


    }

    private void navigationObjects() {
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.historylogs) {
                    Intent i = new Intent(admin_donate_ayuda.this, admin_historylog.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.news_announcements) {
                    Intent i = new Intent(admin_donate_ayuda.this, admin_news_announcements.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.covid_case) {
                    Intent i = new Intent(admin_donate_ayuda.this, admin_covid_case.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.logout) {
                    Intent i = new Intent(admin_donate_ayuda.this, LogIn.class);
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.family_checklist) {
                    Intent i = new Intent(admin_donate_ayuda.this, admin_family_checklist.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                }
                return false;
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
