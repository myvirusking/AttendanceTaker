package com.production.virus.attendancetaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LecturalDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectural_dashboard);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(LecturalDashboard.this,"APP EXIT SUCCESSFULLY", Toast.LENGTH_LONG).show();
        finishAffinity();
        super.onBackPressed();

    }
}
