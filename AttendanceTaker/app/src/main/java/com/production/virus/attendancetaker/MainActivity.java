package com.production.virus.attendancetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class MainActivity extends AppCompatActivity {
    Button sdnt_login,lec_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----------------------------------Start Session Checking----------------------------------
        SharedPreferences sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        String token = sharedpreferences.getString("token","");
        String member = sharedpreferences.getString("member","");

        if(member.equals("student")) {
            //Toast.makeText(MainActivity.this, "Success -" + token + "\nmember-" + member, Toast.LENGTH_LONG).show();
            Intent in = new Intent(MainActivity.this,StudentDashboard.class);
            startActivity(in);
            finish();
        }else if(member.equals("lectural")) {
            //Toast.makeText(MainActivity.this, "Success -" + token + "\nmember-" + member, Toast.LENGTH_LONG).show();
            Intent in = new Intent(MainActivity.this,LecturalDashboard.class);
            startActivity(in);
            finish();
        }
        else{
            Toast.makeText(MainActivity.this,"Error Credentials",Toast.LENGTH_LONG).show();

            setContentView(R.layout.activity_main);
            sdnt_login = findViewById(R.id.button);
            lec_login = findViewById(R.id.button2);

            sdnt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this,StudentLogin.class);
                    startActivity(in);
                }
            });

            lec_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this,LecturalLogin.class);
                    startActivity(in);
                }
            });
        }
        //----------------------------------End Session Checking----------------------------------


    }


}
