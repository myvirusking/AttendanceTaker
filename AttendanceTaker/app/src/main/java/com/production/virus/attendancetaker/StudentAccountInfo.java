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
import android.widget.Button;
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

public class StudentAccountInfo extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btnLogOut;
    ProgressDialog progressdialog;
    String dataFetchURL = MyAlertMixins.urlPrefix+"attendance/api/student/account-info/";
    TextView textUsernameValue,textEmailValue,textFirstNameValue,textLastNameValue,textContactValue,textGenderValue,textDeptValue,textClassValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account_info);


        btnLogOut = findViewById(R.id.btnLogOut);
        textUsernameValue = findViewById(R.id.textUsernameValue);
        textEmailValue = findViewById(R.id.textEmailValue);
        textFirstNameValue = findViewById(R.id.textFirstNameValue);
        textLastNameValue = findViewById(R.id.textLastNameValue);
        textContactValue = findViewById(R.id.textContactValue);
        textGenderValue = findViewById(R.id.textGenderValue);
        textDeptValue = findViewById(R.id.textDeptValue);
        textClassValue = findViewById(R.id.textClassValue);
        sharedPreferences = getSharedPreferences("login",this.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Navigation Code
        Toolbar dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(dashboard_toolbar);
        dashboard_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //End Navigation Code



        //----------------------------------Start Data Fetching Code--------------------------------
        //***Progress Dialog Box***
        progressdialog = ProgressDialog.show(
                StudentAccountInfo.this, "Data Fetching... ",
                "Data Fetching Please Wait..", true);
        progressdialog.setCancelable(false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, dataFetchURL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressdialog.dismiss();
                        try{
                            textUsernameValue.setText(response.getString("username"));
                            textEmailValue.setText(response.getString("email"));
                            textFirstNameValue.setText(response.getString("first_name"));
                            textLastNameValue.setText(response.getString("last_name"));
                            textContactValue.setText(response.getString("contact"));
                            textGenderValue.setText(response.getString("gender"));
                            textDeptValue.setText(response.getString("dept"));
                            textClassValue.setText(response.getString("clas"));
                        }
                        catch (JSONException e) {
                            progressdialog.dismiss();
                            Vibrator v = (Vibrator) getSystemService(StudentAccountInfo.this.VIBRATOR_SERVICE);
                            v.vibrate(1000);
                            MyAlertMixins.showAlertDialog(StudentAccountInfo.this,"✘ Authentication Failed","Invalid Token!...",false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressdialog.dismiss();
                Vibrator v = (Vibrator) getSystemService(StudentAccountInfo.this.VIBRATOR_SERVICE);
                v.vibrate(1000);
                MyAlertMixins.showAlertDialog(StudentAccountInfo.this,"✘ API Not Responding","Please Contact With Admin!...",false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                return header;
            }
        };
        VolleySingleton.getInstance(StudentAccountInfo.this).addToRequestQueue(jsonObjectRequest);
        //----------------------------------End Data Fetching Code---------------------------------



        //----------------------------------Start Logout Code---------------------------------
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Toast.makeText(StudentAccountInfo.this,"LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(StudentAccountInfo.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        });
        //----------------------------------End Logout Code-----------------------------------


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
                Toast.makeText(StudentAccountInfo.this,"LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(StudentAccountInfo.this,MainActivity.class);
                startActivity(in);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
