package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LecturalLogin extends AppCompatActivity {
    TextView txtCreateAc;
    EditText editUser,editPass;
    Button btnLogin;
    //String authenticateTokenUrl ="http://192.168.43.219:8000/attendance/api/student/authentication-token/";
    String authenticateTokenUrl = MyAlertDialog.urlPrefix+"attendance/api/student/authentication-token/";
    SharedPreferences sharedpreferences;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectural_login);

        editPass = findViewById(R.id.editPass);
        editUser = findViewById(R.id.editUser);
        btnLogin = findViewById(R.id.btnLogin);
        sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        String token = sharedpreferences.getString("token","");
        String member = sharedpreferences.getString("member","");

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        if(user != null){
            editUser.setText(user);
        }


        //----------------------------------Start Login Code----------------------------------
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        LecturalLogin.this, "Authenticating... ",
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
                                    if(response.getString("member").equals("lectural")){
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("token",response.getString("token"));
                                        editor.putString("user_id",response.getString("user_id"));
                                        editor.putString("member","lectural");
                                        editor.commit();
                                        progressdialog.dismiss();
                                        Intent in = new Intent(LecturalLogin.this,LecturalDashboard.class);
                                        startActivity(in);
                                    }
                                    else{
                                        progressdialog.dismiss();
                                        Vibrator v = (Vibrator) getSystemService(LecturalLogin.this.VIBRATOR_SERVICE);
                                        v.vibrate(1000);
                                        Toast.makeText(LecturalLogin.this,"✘ Invalid Lectural Credentials, Please Try Via Student Login...",Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(LecturalLogin.this,StudentLogin.class);
                                        in.putExtra("user",editUser.getText().toString().toLowerCase());
                                        startActivity(in);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Vibrator v = (Vibrator) getSystemService(LecturalLogin.this.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        MyAlertDialog.showAlertDialog(LecturalLogin.this,"✘ API Not Responding",error.toString(),false);
                        //MyAlertDialog.showAlertDialog(LecturalLogin.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                        editPass.setText("");
                    }
                });

                VolleySingleton.getInstance(LecturalLogin.this).addToRequestQueue(jsonArrayRequest);
            }
        });
        //----------------------------------End Login Code----------------------------------


        
    }
}
