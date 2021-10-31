package com.example.ayudaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class admin_family_checklist extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String number = "09993323894";
    String message = "CovidDUBIDAPDAP Hello Jv";
    DrawerLayout dLayout;
    TableLayout family_records;
    String username;
    String password;
    DBAdapter helper;
    Button criteria_submit;
    EditText criteria_text;
    Spinner criteria_drop;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_checklist);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");
        family_records = findViewById(R.id.item_view);
        criteria_submit = findViewById(R.id.criteria_submit);
        criteria_drop = findViewById(R.id.criteria);
        criteria_text = findViewById(R.id.criteria_search);


        final Activity activity = this;
        activity.setTitle("Family Checklist");

        Spinner dropdown = findViewById(R.id.criteria);
        String[] items = new String[]{"Region", "City", "Barangay"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        helper = new DBAdapter(this);
        ArrayList<String> creds = new ArrayList<>();
        creds = helper.getAdminName(username, password);
        Toolbar temp = findViewById(R.id.main_toolbar);
        temp.setTitle("Hello "+creds.get(0)+"! Welcome Back!");

        navigationObjects();

        Button ayuda = findViewById(R.id.distribute_ayuda);
        Button resetBatch = findViewById(R.id.reset_batch);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.resetFamilyDonationStatus();
                family_records.removeAllViews();
                ArrayList<String[]> load = helper.returnFamilyChecklistAllCriteria();
                generateFamilyItemsCriteria(load);
                Toast.makeText(getApplicationContext(), "Ayuda Batch is now reset. Employ new batch.", Toast.LENGTH_LONG + 1).show();
            }
        });

        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(admin_family_checklist.this, admin_donate_ayuda.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish();
            }
        });

        ArrayList<String[]> load = helper.returnFamilyChecklistAllCriteria();

        generateFamilyItemsCriteria(load);

        criteria_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String[]> data = new ArrayList<>();
                family_records.removeAllViews();
                if (criteria_drop.getSelectedItem().toString().equals("Region")) {
                    data = helper.returnFamilyChecklistRegion(criteria_text.getText().toString());
                } else if (criteria_drop.getSelectedItem().toString().equals("City")) {
                    data = helper.returnFamilyChecklistCity(criteria_text.getText().toString());
                } else if (criteria_drop.getSelectedItem().toString().equals("Barangay")) {
                    data = helper.returnFamilyChecklistBarangay(criteria_text.getText().toString());
                } else if (criteria_text.getText().toString() == null) {
                    data = helper.returnFamilyChecklistAllCriteria();
                }
                generateFamilyItemsCriteria(data);
                Toast.makeText(getApplicationContext(), "Here 2", Toast.LENGTH_LONG);
            }
        });
    }

    private void generateFamilyItemsCriteria(ArrayList<String[]> data) {
        Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_LONG);
        for (int i = 0; i < data.size(); i++) {
            TableRow temp = new TableRow(this);
            temp.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            temp.setBackgroundResource(R.drawable.border);
            temp.setWeightSum(1);
            temp.setGravity(Gravity.FILL);

            TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            layout.setMargins(5, 5, 5, 5);

            TextView ItemName = new TextView(this);
            ItemName.setLayoutParams(layout);
            ItemName.setGravity(Gravity.CENTER);
            TextView ItemQuantity = new TextView(this);
            ItemQuantity.setLayoutParams(layout);
            ItemQuantity.setGravity(Gravity.CENTER);

            String FamilyID = data.get(i)[0];
            String FamilyName = data.get(i)[1];
            String HouseNo = data.get(i)[2];
            String Subdivision = data.get(i)[3];
            String StreetName = data.get(i)[4];

            ItemName.setText(FamilyID + "-\nFamily Name:" + FamilyName);
            ItemQuantity.setText("House No: " + HouseNo + "\nSubdivision: " + Subdivision + "\nStreet Name: " + StreetName);

            temp.addView(ItemName);
            temp.addView(ItemQuantity);
            family_records.addView(temp);
        }
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
                if(itemId == R.id.historylogs){
                    Intent i = new Intent(admin_family_checklist.this, admin_historylog.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();
                }else if(itemId==R.id.news_announcements){
                    Intent i = new Intent(admin_family_checklist.this, admin_news_announcements.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();
                }else if(itemId==R.id.covid_case){
                    Intent i = new Intent(admin_family_checklist.this, admin_covid_case.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();
                }else if(itemId == R.id.logout){
                    Intent i = new Intent(admin_family_checklist.this, LogIn.class);
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
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
