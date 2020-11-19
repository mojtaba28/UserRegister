package com.example.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    TextView tv_email,tv_password;
    FloatingActionButton fab;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        tv_email.setText(sessionManager.getUserEmail());
        tv_password.setText(sessionManager.getUserPassword());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLoggedIn(false);
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }

    public void init(){
        sessionManager=new SessionManager(getApplicationContext());
        tv_email=findViewById(R.id.tv_email);
        tv_password=findViewById(R.id.tv_password);
        fab=findViewById(R.id.fab);

    }
}