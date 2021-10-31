package com.example.ayudaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class covid_case_activity extends AppCompatActivity {

    private ArrayList<CaseItem> mExampleList1;
    private ArrayList<CaseItem> mExampleList2;
    private ArrayList<CaseItem> mExampleList3;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private RecyclerView.LayoutManager mLayoutManager1;
    private RecyclerView.LayoutManager mLayoutManager2;
    private RecyclerView.LayoutManager mLayoutManager3;
    private RecyclerView.Adapter mAdapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    String username;
    String password;
    DBAdapter helper;
    ArrayList<String[]> temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_case_activity);
        mExampleList1 = new ArrayList<CaseItem>();
        mExampleList2 = new ArrayList<CaseItem>();
        mExampleList3 = new ArrayList<CaseItem>();
        helper = new DBAdapter(this);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        navigationObjects();
        createExampleList();
        buildRecyclerView1();
        buildRecyclerView2();
        buildRecyclerView3();
    }

    public void createExampleList() {
        temp = helper.covidYearly();
        if(temp.size()==0){
            mExampleList1.add(new CaseItem("No daily reports", "N/a"));
            mExampleList2.add(new CaseItem("No monthly reports", "N/a"));
            mExampleList3.add(new CaseItem("No yearly reports", "N/a"));
        }else{
            for(String[] x: temp){
                mExampleList3.add(new CaseItem(x[0], x[1]));
            }
            temp= helper.covidDaily();
            for(String[] x: temp){
                mExampleList1.add(new CaseItem(x[0]+" "+x[1]+" "+x[2], x[3]));
            }
            temp = helper.covidMonthly();
            for(String[] x: temp){
                mExampleList2.add(new CaseItem(x[0]+" "+x[1], x[2]));
            }

        }
    }

    public void buildRecyclerView1() {
        mRecyclerView1 = findViewById(R.id.daily_case_recycler_view);
        mLayoutManager1 = new LinearLayoutManager(this, mRecyclerView1.HORIZONTAL,false);
        mRecyclerView1.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        mAdapter = new CaseAdapter(mExampleList1);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setAdapter(mAdapter);
    }

    public void buildRecyclerView2() {
        mRecyclerView2 = findViewById(R.id.monthly_case_recycler_view);
        mLayoutManager2 = new LinearLayoutManager(this, mRecyclerView1.HORIZONTAL,false);
        mAdapter = new CaseAdapter(mExampleList2);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setAdapter(mAdapter);
    }

    public void buildRecyclerView3() {
        mRecyclerView3 = findViewById(R.id.yearly_case_recycler_view);
        mLayoutManager3 = new LinearLayoutManager(this, mRecyclerView1.HORIZONTAL,false);
        mAdapter = new CaseAdapter(mExampleList3);
        mRecyclerView3.setLayoutManager(mLayoutManager3);
        mRecyclerView3.setHasFixedSize(true);
        mRecyclerView3.setAdapter(mAdapter);
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
                if(itemId==R.id.homepage){
                    Intent i = new Intent(covid_case_activity.this, FamilyMainActivity.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();

                }else if(itemId==R.id.newspage){
                    Intent i = new Intent(covid_case_activity.this, TestNewsActivity.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish();
                }else if(itemId == R.id.family_logout){
                    Intent i = new Intent(covid_case_activity.this, LogIn.class);
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

    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}