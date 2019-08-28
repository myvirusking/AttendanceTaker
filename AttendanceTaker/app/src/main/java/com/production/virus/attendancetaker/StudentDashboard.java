package com.production.virus.attendancetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class StudentDashboard extends AppCompatActivity {
    SharedPreferences.Editor editor;
    CardView cardGiveAttendance,cardMyReport,cardMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Checking For Internet Connection
        if(new NoInternetMixins(this).getInternetStatus()){
        }


        cardGiveAttendance = findViewById(R.id.cardGiveAttendance);
        cardMyReport = findViewById(R.id.cardMyReport);
        cardMyAccount = findViewById(R.id.cardMyAccount);
        //Navigation Code
        Toolbar dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(dashboard_toolbar);
        //End Navigation Code
        SharedPreferences sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        //----------------------------------Start Smart Attendance Code----------------------------------
        cardGiveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StudentDashboard.this,StudentGiveAttendance.class);
                startActivity(in);
            }
        });
        //----------------------------------End Smart Attendance Code----------------------------------

        //----------------------------------Start Manual Attendance Code----------------------------------
        cardMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StudentDashboard.this,StudentAttendanceReport.class);
                startActivity(in);
            }
        });
        //----------------------------------End Manual Attendance Code----------------------------------

        //----------------------------------Start Student Report Code----------------------------------
        cardMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StudentDashboard.this,StudentAccountInfo.class);
                startActivity(in);
            }
        });
        //----------------------------------End Student Report Code----------------------------------


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                editor.clear();
                editor.commit();
                Toast.makeText(StudentDashboard.this,"LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(StudentDashboard.this,MainActivity.class);
                startActivity(in);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(StudentDashboard.this,"APP EXIT SUCCESSFULLY", Toast.LENGTH_LONG).show();
        finishAffinity();
        super.onBackPressed();

    }
}
