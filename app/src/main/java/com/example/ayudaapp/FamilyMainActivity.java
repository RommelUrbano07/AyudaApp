package com.example.ayudaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FamilyMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String username;
    String password;
    DBAdapter helper;
    private CardView cardView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");
        helper=new DBAdapter(this);
        String creds =  helper.getFamilyReferenceID(username,password);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_main);
        navigationObjects();

        TextView familyID = findViewById(R.id.family_id_text_view);
        familyID.setText(creds);
        String [] temp = creds.split("-");
        String familyname = helper.getFamilySurname(temp[0]);
        TextView family_surname = findViewById(R.id.family_name_text_view);
        family_surname.setText(familyname);

        TextView Daily = findViewById(R.id.daily_main);
        TextView Monthly = findViewById(R.id.monthly_main);
        TextView Yearly = findViewById(R.id.yearly_main);
        cardView = findViewById(R.id.home_news_card_view);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ" ));
                startActivity(browserIntent);
            }
        });

        if(helper.getYearlyCovid() == 0){
            Daily.setText(0+"");
            Monthly.setText(0+"");
            Yearly.setText(0+"");
        }else{
            Daily.setText(helper.getDailyCovid()+"");
            Monthly.setText(helper.getMonthlyCovidCase()+"");
            Yearly.setText(helper.getYearlyCovid()+"");
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
                if(itemId==R.id.family_covid_case){
                    Intent i = new Intent(FamilyMainActivity.this, covid_case_activity.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();

                }else if(itemId==R.id.newspage){
                    Intent i = new Intent(FamilyMainActivity.this, TestNewsActivity.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();
                }else if(itemId == R.id.family_logout){
                    Intent i = new Intent(FamilyMainActivity.this, LogIn.class);
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}