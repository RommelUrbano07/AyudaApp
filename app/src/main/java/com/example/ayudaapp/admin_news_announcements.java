package com.example.ayudaapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class admin_news_announcements extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String number = "09993323894";
    String message = "CovidDUBIDAPDAP Hello Jv";
    String username;
    String password;
    DBAdapter helper;
    EditText fname, mname, lname, sname, content;
    Button submit;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_news_announcement);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        helper = new DBAdapter(this);
        ArrayList<String> creds = new ArrayList<>();
        creds = helper.getAdminName(username, password);

        navigationObjects();
//        NavigationView navigationView = findViewById(R.id.navigation);
//        View headerView = navigationView.getHeaderView(0);
//        TextView navUsername = headerView.findViewById(R.id.nav_header_textView);
//        navUsername.setText("Hello " + creds.get(0) + "! Welcome back!");

        fname = findViewById(R.id.author_f);
        mname = findViewById(R.id.author_m);
        lname = findViewById(R.id.author_l);
        sname = findViewById(R.id.author_s);
        content = findViewById(R.id.news_announcement_content);
        submit = findViewById(R.id.submit_news_announcement);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.insertNewsAnnouncement(content.getText().toString(), fname.getText().toString(),
                        mname.getText().toString(), lname.getText().toString(), sname.getText().toString()) >= 1) {
                    Toast.makeText(admin_news_announcements.this, "News/Announcement Submitted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(admin_news_announcements.this, "News/Announcement Failed", Toast.LENGTH_SHORT).show();
                }
                fname.setText("");
                mname.setText("");
                lname.setText("");
                sname.setText("");
                content.setText("");
            }
        });

    }

    protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void navigationObjects(){
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
                if (itemId == R.id.family_checklist) {
                    Intent i = new Intent(admin_news_announcements.this, admin_family_checklist.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.historylogs) {
                    Intent i = new Intent(admin_news_announcements.this, admin_historylog.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.covid_case) {
                    Intent i = new Intent(admin_news_announcements.this, admin_covid_case.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.logout) {
                    Intent i = new Intent(admin_news_announcements.this, LogIn.class);
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
                // display a toast message with menu item's title
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
