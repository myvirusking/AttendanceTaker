package com.production.virus.attendancetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class LecturalDashboard extends AppCompatActivity {
    SharedPreferences.Editor editor;
    CardView cardSmartAttendance,cardManualAttendance,cardReport,cardMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectural_dashboard);

        // Checking For Internet Connection
        if(new NoInternetMixins(this).getInternetStatus()){
        }


        cardSmartAttendance = findViewById(R.id.cardSmartAttendance);
        cardManualAttendance = findViewById(R.id.cardManualAttendance);
        cardReport = findViewById(R.id.cardReport);
        cardMyAccount = findViewById(R.id.cardMyAccount);

        //Navigation Code
        Toolbar dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(dashboard_toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("  E-Aptitude");
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setIcon(R.mipmap.ic_launcher_round);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //End Navigation Code

        SharedPreferences sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        //----------------------------------Start Smart Attendance Code----------------------------------
        cardSmartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking For Internet Connection
                if(new NoInternetMixins(LecturalDashboard.this).getInternetStatus()){
                }
                Intent in = new Intent(LecturalDashboard.this,SmartAttendance.class);
                startActivity(in);
            }
        });
        //----------------------------------End Smart Attendance Code----------------------------------

        //----------------------------------Start Manual Attendance Code----------------------------------
        cardManualAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vv = (Vibrator) getSystemService(LecturalDashboard.this.VIBRATOR_SERVICE);
                vv.vibrate(1000);
                MyAlertMixins.showAlertDialog(LecturalDashboard.this,"Future Update...","This Update Implement In Future!",false);
            }
        });
        //----------------------------------End Manual Attendance Code----------------------------------

        //----------------------------------Start Student Report Code----------------------------------
        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LecturalDashboard.this,AllStudentAttendanceReport.class);
                startActivity(in);
            }
        });
        //----------------------------------End Student Report Code----------------------------------

        //----------------------------------Start My Account Code----------------------------------
        cardMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LecturalDashboard.this,LecturerAccountInfo.class);
                startActivity(in);
            }
        });
        //----------------------------------End My Account Code----------------------------------

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
                Toast.makeText(LecturalDashboard.this,"LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(LecturalDashboard.this,MainActivity.class);
                startActivity(in);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


       @Override
    public void onBackPressed() {
        Toast.makeText(LecturalDashboard.this,"APP EXIT SUCCESSFULLY", Toast.LENGTH_LONG).show();
        finishAffinity();
        super.onBackPressed();

    }
}
