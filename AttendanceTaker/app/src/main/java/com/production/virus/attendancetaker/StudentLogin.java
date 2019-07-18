package com.production.virus.attendancetaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLogin extends AppCompatActivity {
    TextView txtCreateAc;
    EditText editUser,editPass;
    Button btnLogin;
    String authenticateTokenUrl ="http://10.0.2.2:8000/attendance/api/student/authentication-token/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        editPass = findViewById(R.id.editPass);
        editUser = findViewById(R.id.editUser);
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAc = findViewById(R.id.txtCreateAc);

        txtCreateAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StudentLogin.this,StudentRegistration.class);
                startActivity(in);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Using Authentication Get Token
                Map<String,String> params = new HashMap<>();
                params.put("username",editUser.getText().toString().toLowerCase());
                params.put("password",editPass.getText().toString());
                JSONObject jsonObject = new JSONObject(params);

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, authenticateTokenUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(StudentLogin.this,response.toString(),Toast.LENGTH_LONG).show();
                                    Log.e("suceess",response.getString("token"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentLogin.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                VolleySingleton.getInstance(StudentLogin.this).addToRequestQueue(jsonArrayRequest);
                //Using Authentication Get Token
            }
        });


    }
}
