package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class StudentDashboard extends AppCompatActivity {
    SharedPreferences.Editor editor;
    Spinner spinSub;
    Button btnCheckStatus,btnPresent;
    SharedPreferences sharedPreferences;
    ProgressDialog progressdialog;
    TextView textDeptValue,textClassValue;
    final String studenrtDataFetchURL = MyAlertDialog.urlPrefix+"attendance/api/student/fetch-data/";
    final String attendanceCheckURL = MyAlertDialog.urlPrefix+"attendance/api/student/attendance-status/check/";
    final String smartAttendanceURL = MyAlertDialog.urlPrefix+"attendance/api/student/smart-attendance/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        spinSub = findViewById(R.id.spinSub);
        btnCheckStatus = findViewById(R.id.btnCheckStatus);
        btnPresent = findViewById(R.id.btnPresent);
        textDeptValue = findViewById(R.id.textDeptValue);
        textClassValue = findViewById(R.id.textClassValue);
        Toolbar dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(dashboard_toolbar);
        sharedPreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //----------------------------------Start Student Fetch Data----------------------------------
        //***Progress Dialog Box***
        progressdialog = ProgressDialog.show(
                StudentDashboard.this, "Data Fetching... ",
                "Data Fetching Please Wait..", true);
        progressdialog.setCancelable(false);


        Map<String,String> params = new HashMap<>();
        params.put("user_id",sharedPreferences.getString("user_id",""));
        final JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, studenrtDataFetchURL,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressdialog.dismiss();
                        try{
                            textDeptValue.setText(response.getString("dept"));
                            textClassValue.setText(response.getString("clas"));

                            int total_len = response.getJSONArray("subject").length();
                            String[] arraySpinnerClas = new String[total_len];
                            for(int i=0;i<total_len;i++){
                                arraySpinnerClas[i] = response.getJSONArray("subject").getString(i);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StudentDashboard.this,
                                    android.R.layout.simple_spinner_item, arraySpinnerClas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinSub.setAdapter(adapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressdialog.dismiss();
                Vibrator v = (Vibrator) getSystemService(StudentDashboard.this.VIBRATOR_SERVICE);
                v.vibrate(1000);
                //MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ Error Data Not Fetched","Invalid Credentials...",false);
                MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ API Not Responding",error.toString(),false);
                //MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ API Not Responding","Please Contact With Admin!...",false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                return header;
            }
        };
        VolleySingleton.getInstance(StudentDashboard.this).addToRequestQueue(jsonObjectRequest);
        //----------------------------------End Student Fetch Data------------------------------------




        //----------------------------------Attendance Active Status Check Start----------------------------------
        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        StudentDashboard.this, "Data Fetching... ",
                        "Data Fetching Please Wait..", true);
                progressdialog.setCancelable(false);

                Map<String,String> params = new HashMap<>();
                params.put("user_id",sharedPreferences.getString("user_id",""));
                params.put("sub",spinSub.getSelectedItem().toString());
                final JSONObject jsonObject = new JSONObject(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,attendanceCheckURL,jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressdialog.dismiss();
                                try{

                                    if(Boolean.parseBoolean(response.getString("active"))== true){
                                        btnPresent.setEnabled(true);
                                    }
                                    else{
                                        progressdialog.dismiss();
                                        Vibrator v = (Vibrator) getSystemService(StudentDashboard.this.VIBRATOR_SERVICE);
                                        v.vibrate(1000);
                                        MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ Attendance Not Allowed","Attendance Not Active Now, Please Contact With Lecturer...",false);
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Vibrator v = (Vibrator) getSystemService(StudentDashboard.this.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ API Not Responding",error.toString(),false);
                        //MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header = new HashMap<>();
                        header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                        return header;
                    }
                };
                VolleySingleton.getInstance(StudentDashboard.this).addToRequestQueue(jsonObjectRequest);

            }
        });
        //----------------------------------Attendance Active Status Check Emd------------------------------------



        //On Subject Change Present Button Disabled Code
        spinSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnPresent.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //----------------------------------Actual Attendance Logic Start----------------------------------
        btnPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",sharedPreferences.getString("user_id",""));
                params.put("sub",spinSub.getSelectedItem().toString());
                JSONObject jsonObject = new JSONObject(params);

                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        StudentDashboard.this, "Attendance On Processing... ",
                        "Attendance On Processing Please Wait..", true);
                progressdialog.setCancelable(false);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, smartAttendanceURL,jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressdialog.dismiss();
                                try{
                                    String responseSuccess = response.getString("Success");
                                    Toast.makeText(StudentDashboard.this,responseSuccess,Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(StudentDashboard.this,StudentDashboard.class);
                                    startActivity(in);

                                }
                                catch (JSONException e) {
                                    progressdialog.dismiss();
                                    Vibrator v = (Vibrator) getSystemService(StudentDashboard.this.VIBRATOR_SERVICE);
                                    v.vibrate(1000);
                                    MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ Duplicate Attendance","Your Attendance Already Done Today!...",false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Vibrator v = (Vibrator) getSystemService(StudentDashboard.this.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        MyAlertDialog.showAlertDialog(StudentDashboard.this,"✘ API Not Responding",error.toString(),false);
                        //MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ API Not Responding","Please Contact With Admin!...",false);

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header = new HashMap<>();
                        header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                        return header;
                    }
                };
                VolleySingleton.getInstance(StudentDashboard.this).addToRequestQueue(jsonObjectRequest);

            }
        });

        //----------------------------------Actual Attendance Logic End------------------------------------



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
