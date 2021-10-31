package com.example.ayudaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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

public class TestNewsActivity extends AppCompatActivity {
    private ArrayList<NewsItem> mExampleList;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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
        setContentView(R.layout.activity_test_news);

        mExampleList = new ArrayList<NewsItem>();
        helper = new DBAdapter(this);
        temp = helper.getNews();
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        createExampleList();
        buildRecyclerView();
        navigationObjects();
    }

    public void createExampleList() {
        if(temp.size()>0){
            for (int i = 0; i < temp.size(); i++) {
                mExampleList.add(new NewsItem(R.drawable.logonews, temp.get(i)[0], "Author Name: " + temp.get(i)[2] + "\n News Content: \n " + temp.get(i)[4] + "\n" + "Date Published:" + temp.get(i)[5]));
            }
        }else{
            mExampleList.add(new NewsItem(R.drawable.logonews, "No news availble", "No content available"));
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NewsAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + temp.get(position)[3]));
                startActivity(browserIntent);
            }
        });
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
                if (itemId == R.id.homepage) {
                    Intent i = new Intent(TestNewsActivity.this, FamilyMainActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.family_covid_case) {
                    Intent i = new Intent(TestNewsActivity.this, covid_case_activity.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                } else if (itemId == R.id.family_logout) {
                    Intent i = new Intent(TestNewsActivity.this, LogIn.class);
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