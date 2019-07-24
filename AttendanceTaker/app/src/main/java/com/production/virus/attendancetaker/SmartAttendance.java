package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class SmartAttendance extends AppCompatActivity {
    Spinner spinSub;
    Button btnTakeAttendance;
    SharedPreferences sharedPreferences;
    ProgressDialog progressdialog;
    TextView textDeptValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_attendance);
        String dataFetchURL ="http://192.168.1.105:8000/attendance/api/lectural/fetch-data/";
        //String dataFetchURL ="http://192.168.43.219:8000/attendance/api/lectural/fetch-data/";


        spinSub = findViewById(R.id.spinSub);
        textDeptValue = findViewById(R.id.textDeptValue);
        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        sharedPreferences = getSharedPreferences("login",this.MODE_PRIVATE);
        Map<String,String> params = new HashMap<>();
        params.put("user_id",sharedPreferences.getString("user_id",""));
        JSONObject jsonObject = new JSONObject(params);


        //***Progress Dialog Box***
        progressdialog = ProgressDialog.show(
                SmartAttendance.this, "Data Fetching... ",
                "Data Fetching Please Wait..", true);
        progressdialog.setCancelable(false);

        //----------------------------------Start Lectural Fectch Data----------------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dataFetchURL,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressdialog.dismiss();
                        try{
                            textDeptValue.setText("- "+response.getString("dept"));

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
                Toast.makeText(SmartAttendance.this,error.toString(),Toast.LENGTH_LONG).show();
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
        //----------------------------------End Lectural Fectch Data----------------------------------





        //----------------------------------Smart Attendance Logic Start----------------------------------
        btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SmartAttendance.this,"Hello",Toast.LENGTH_LONG).show();
            }
        });
        //----------------------------------Smart Attendance Logic End----------------------------------

    }
}
