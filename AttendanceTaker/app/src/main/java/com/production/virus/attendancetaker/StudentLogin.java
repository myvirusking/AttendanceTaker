package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    //String authenticateTokenUrl ="http://10.0.2.2:8000/attendance/api/student/authentication-token/";
    String authenticateTokenUrl ="http://192.168.1.105:8000/attendance/api/student/authentication-token/";
    SharedPreferences sharedpreferences;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        editPass = findViewById(R.id.editPass);
        editUser = findViewById(R.id.editUser);
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAc = findViewById(R.id.txtCreateAc);
        sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        String token = sharedpreferences.getString("token","");
        String member = sharedpreferences.getString("member","");

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        if(user != null){
            System.err.print("=================="+user+"===================");
            Toast.makeText(StudentLogin.this,user,Toast.LENGTH_LONG).show();
            editUser.setText(user);
        }
        else{
            System.err.print("==================NULL===================");
            Toast.makeText(StudentLogin.this,"Null",Toast.LENGTH_LONG).show();
        }


        //----------------------------------Start Login Code----------------------------------
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
                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        StudentLogin.this, "Authenticating... ",
                        "Authenticating Please Wait..", true);
                progressdialog.setCancelable(false);

                //***Using Authentication Get Token***
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
                                    if(response.getString("member").equals("student")){
                                        Toast.makeText(StudentLogin.this,"You Are Student",Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("token",response.getString("token"));
                                        editor.putString("user_id",response.getString("user_id"));
                                        editor.putString("member","student");
                                        editor.commit();
                                        progressdialog.dismiss();
                                        Intent in = new Intent(StudentLogin.this,StudentDashboard.class);
                                        startActivity(in);
                                    }
                                    else{
                                        Toast.makeText(StudentLogin.this,"Not A Student",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Vibrator v = (Vibrator) getSystemService(StudentLogin.this.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        MyAlertDialog.showAlertDialog(StudentLogin.this,"✘ Authentication Failed","Incorrect Username And Password...",false);
                        editPass.setText("");
                    }
                });

                VolleySingleton.getInstance(StudentLogin.this).addToRequestQueue(jsonArrayRequest);

            }
        });
        //----------------------------------End Login Code----------------------------------




    }
}
