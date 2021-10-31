package com.example.ayudaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class admin_verify_ayuda extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String number = "";
    String message = "Thanks for using AyudApp! Your One Time Pin is:";
    DrawerLayout dLayout;
    String username;
    String password;
    DBAdapter helper;
    String OTP;
    String familyID;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_ayuda);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");
        OTP = extras.getString("OTP");
        familyID = extras.getString("familyID");

        final Activity activity = this;
        activity.setTitle("Distribute Ayuda");

        helper = new DBAdapter(this);
        ArrayList<String> creds = new ArrayList<>();
//        creds = helper.getAdminName(username, password);

        navigationObjects();
//        NavigationView navigationView = findViewById(R.id.navigation);
//        View headerView = navigationView.getHeaderView(0);
//        TextView navUsername = headerView.findViewById(R.id.nav_header_textView);
//        navUsername.setText("Hello " + creds.get(0) + "! Welcome back!");

        EditText print = findViewById(R.id.reference_print);
        print.setText(extras.getString("reference"));
        EditText OTP_input = findViewById(R.id.OTP_input);
        Button submit = findViewById(R.id.verify_otp_button);

        Toast.makeText(getApplicationContext(), OTP, Toast.LENGTH_LONG).show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(OTP_input.getText().toString(), OTP);
                diaBox.show();
            }
        });


    }

    private AlertDialog AskOption(String input_ID, String true_OTP) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Review your information")
                .setMessage("OTP input is:" + input_ID + ", continue?")
                .setIcon(R.drawable.warning)

                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (helper.OTPverifier(input_ID, true_OTP)) {
                            if (helper.checkDonatedStatus(familyID)) {
                                helper.updateFamilyDonationStatus(familyID);
                                helper.insertAyudaDonation("Ayuda distributed to Family No:" + familyID, familyID);
                                Toast.makeText(getApplicationContext(), "Donation Successful!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(admin_verify_ayuda.this, admin_family_checklist.class);
                                i.putExtra("username", username);
                                i.putExtra("password", password);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ayuda has already been claimed by the family, wait for next batch.", Toast.LENGTH_LONG + 3).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong OTP code! Try again", Toast.LENGTH_LONG + 3).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
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
                    Intent i = new Intent(admin_verify_ayuda.this, admin_historylog.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.news_announcements) {
                    Intent i = new Intent(admin_verify_ayuda.this, admin_news_announcements.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.covid_case) {
                    Intent i = new Intent(admin_verify_ayuda.this, admin_covid_case.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.logout) {
                    Intent i = new Intent(admin_verify_ayuda.this, LogIn.class);
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.family_checklist) {
                    Intent i = new Intent(admin_verify_ayuda.this, admin_family_checklist.class);
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