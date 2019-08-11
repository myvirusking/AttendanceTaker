package com.production.virus.attendancetaker;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class SmartAttendanceStop extends AppCompatActivity {
    Button btnStopAttendance;
    ProgressDialog progressdialog;
    SharedPreferences sharedPreferences;
    //String attendanceTriggerStopURL ="http://192.168.1.105:8000/attendance/api/lectural/attendance-trigger/stop/";
    String attendanceTriggerStopURL = MyAlertDialog.urlPrefix+"attendance/api/lectural/attendance-trigger/stop/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_attendance_stop);

        sharedPreferences = getSharedPreferences("login",this.MODE_PRIVATE);
        btnStopAttendance = findViewById(R.id.btnStopAttendance);

        //----------------------------------Stop Smart Attendance Start----------------------------------
        btnStopAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent in = getIntent();

                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        SmartAttendanceStop.this, "Stopping Attendance... ",
                        "Stopping Attendance Please Wait..", true);
                progressdialog.setCancelable(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, attendanceTriggerStopURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressdialog.dismiss();
                                Toast.makeText(SmartAttendanceStop.this,"Attendance Stopped Successfully!",Toast.LENGTH_LONG).show();
                                Intent in = new Intent(SmartAttendanceStop.this,LecturalDashboard.class);
                                startActivity(in);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        MyAlertDialog.showAlertDialog(SmartAttendanceStop.this,"✘ API Not Responding",error.toString(),false);
                        //MyAlertDialog.showAlertDialog(SmartAttendanceStop.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("user_id",sharedPreferences.getString("user_id",""));
                        params.put("ATM_obj_id",in.getStringExtra("ATM_obj_id"));
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header = new HashMap<>();
                        header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                        return header;
                    }
                };
                VolleySingleton.getInstance(SmartAttendanceStop.this).addToRequestQueue(stringRequest);


            }
        });
        //----------------------------------Stop Smart Attendance End----------------------------------
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(SmartAttendanceStop.this,"Attendance Stopped Successfully!", Toast.LENGTH_LONG).show();
        btnStopAttendance.performClick();
        super.onBackPressed();

    }


    @Override
    protected void onStop() {
        Toast.makeText(SmartAttendanceStop.this,"Attendance Stopped Successfully!", Toast.LENGTH_LONG).show();
        btnStopAttendance.performClick();
        super.onStop();
    }

}
