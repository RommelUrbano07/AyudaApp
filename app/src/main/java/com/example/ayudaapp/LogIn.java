package com.example.ayudaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submit;
    private CheckBox checkbox;
    DBAdapter helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        submit = findViewById(R.id.login_trigger);

        helper = new DBAdapter(this);

        if (helper.checkDatabaseContent() == false) {
            helper.initialAdminAccount();
            helper.loadData();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().split("_").length > 1) {
                    ArrayList<String[]> temp = helper.adminlogin(username.getText().toString(), password.getText().toString());
                    if (temp.size() <= 0) {
                        Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(LogIn.this, admin_family_checklist.class);
                        i.putExtra("username", username.getText().toString());
                        i.putExtra("password", password.getText().toString());
                        startActivity(i);
                        finish();
                    }
                } else {
                    String temp = helper.getFamilyReferenceID(username.getText().toString(), password.getText().toString());
                    if (temp == null) {
                        Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(LogIn.this, FamilyMainActivity.class);
                        i.putExtra("username", username.getText().toString());
                        i.putExtra("password", password.getText().toString());
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }
}
