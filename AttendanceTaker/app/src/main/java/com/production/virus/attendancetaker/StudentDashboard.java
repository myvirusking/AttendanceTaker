package com.production.virus.attendancetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class StudentDashboard extends AppCompatActivity {
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        Toolbar dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(dashboard_toolbar);
        SharedPreferences sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedpreferences.edit();

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
                Intent in = new Intent(StudentDashboard.this,MainActivity.class);
                startActivity(in);
                finish();
                return true;

            case R.id.action_settings:
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
