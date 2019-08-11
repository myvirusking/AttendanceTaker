package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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


public class SmartAttendance extends AppCompatActivity {
    Spinner spinSub,spinClass;
    Button btnTakeAttendance;
    SharedPreferences sharedPreferences;
    ProgressDialog progressdialog;
    TextView textDeptValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_attendance);
        //String dataFetchURL ="http://192.168.1.105:8000/attendance/api/lectural/fetch-data/";
        //String attendanceTriggerActiveURL ="http://192.168.1.105:8000/attendance/api/lectural/attendance-trigger/active/";
        final String dataFetchURL = MyAlertDialog.urlPrefix+"attendance/api/lectural/fetch-data/";
        final String attendanceTriggerActiveURL = MyAlertDialog.urlPrefix+"attendance/api/lectural/attendance-trigger/active/";


        spinSub = findViewById(R.id.spinSub);
        spinClass = findViewById(R.id.spinClass);
        textDeptValue = findViewById(R.id.textDeptValue);
        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        sharedPreferences = getSharedPreferences("login",this.MODE_PRIVATE);
        Map<String,String> params = new HashMap<>();
        params.put("user_id",sharedPreferences.getString("user_id",""));
        final JSONObject jsonObject = new JSONObject(params);


        //***Progress Dialog Box***
        progressdialog = ProgressDialog.show(
                SmartAttendance.this, "Data Fetching... ",
                "Data Fetching Please Wait..", true);
        progressdialog.setCancelable(false);

        //----------------------------------Start Lectural Fetch Data----------------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dataFetchURL,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressdialog.dismiss();
                        try{
                            textDeptValue.setText(response.getString("dept"));

                            int total_len = response.getJSONArray("subject").length();
                            String[] arraySpinnerClas = new String[total_len];
                            for(int i=0;i<total_len;i++){
                                arraySpinnerClas[i] = response.getJSONArray("subject").getString(i);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmartAttendance.this,
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
                Vibrator v = (Vibrator) getSystemService(SmartAttendance.this.VIBRATOR_SERVICE);
                v.vibrate(1000);
                //MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ Error Data Not Fetched","Invalid Credentials...",false);
                MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ API Not Responding",error.toString(),false);
                //MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                onBackPressed();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                return header;
            }
        };
        VolleySingleton.getInstance(SmartAttendance.this).addToRequestQueue(jsonObjectRequest);
        //----------------------------------End Lectural Fetch Data----------------------------------





        //----------------------------------Smart Attendance Logic Start----------------------------------
        btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> params = new HashMap<>();
                params.put("user_id",sharedPreferences.getString("user_id",""));
                params.put("clas",spinClass.getSelectedItem().toString());
                params.put("sub",spinSub.getSelectedItem().toString());
                params.put("dept",textDeptValue.getText().toString());
                JSONObject jsonObject = new JSONObject(params);

                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        SmartAttendance.this, "Activating Attendance... ",
                        "Activating Attendance Please Wait..", true);
                progressdialog.setCancelable(false);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, attendanceTriggerActiveURL,jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressdialog.dismiss();
                                try{
                                    Intent in = new Intent(SmartAttendance.this,SmartAttendanceStop.class);
                                    in.putExtra("ATM_obj_id",response.getString("ATM_obj_id"));
                                    startActivity(in);

                                }
                                catch (JSONException e) {
                                    progressdialog.dismiss();
                                    Vibrator v = (Vibrator) getSystemService(SmartAttendance.this.VIBRATOR_SERVICE);
                                    v.vibrate(1000);
                                    MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ Invalid Subject & Class","Please Select Valid Subject And Class...",false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Vibrator v = (Vibrator) getSystemService(SmartAttendance.this.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        MyAlertDialog.showAlertDialog(SmartAttendance.this,"✘ API Not Responding",error.toString(),false);
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
                VolleySingleton.getInstance(SmartAttendance.this).addToRequestQueue(jsonObjectRequest);

            }
        });
        //----------------------------------Smart Attendance Logic End----------------------------------

    }
}
