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


public class MainActivity extends AppCompatActivity {
    TextView txtCreateAc,txtForgotPass;
    EditText editUser,editPass;
    Button btnLogin;
    String authenticateTokenUrl = MyAlertMixins.urlPrefix+"attendance/api/student/authentication-token/";
    //String authenticateTokenUrl ="http://192.168.1.104:8000/attendance/api/student/authentication-token/";
    SharedPreferences sharedpreferences;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking For Internet Connection
        if(new NoInternetMixins(this).getInternetStatus()){
        }


        //----------------------------------If Session Start----------------------------------
        sharedpreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        String token = sharedpreferences.getString("token","");
        String category = sharedpreferences.getString("category","");

        if(category.equals("student")) {
            Intent in = new Intent(MainActivity.this, StudentDashboard.class);
            startActivity(in);
            finish();
        }else if(category.equals("faculty")) {
            Intent in = new Intent(MainActivity.this,LecturalDashboard.class);
            startActivity(in);
            finish();
        }

        //----------------------------------If Session End ----------------------------------

        //----------------------------------If Not Session Start----------------------------------
        else{
            setContentView(R.layout.activity_main);

            editPass = findViewById(R.id.editPass);
            editUser = findViewById(R.id.editUser);
            btnLogin = findViewById(R.id.btnLogin);
            txtCreateAc = findViewById(R.id.txtCreateAc);
            txtForgotPass = findViewById(R.id.txtForgotPass);

            Intent intent = getIntent();
            String user = intent.getStringExtra("user");
            if(user != null){
                editUser.setText(user);
            }



            txtCreateAc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this,StudentRegistration.class);
                    startActivity(in);
                }
            });

            txtForgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vv = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                    vv.vibrate(1000);
                    MyAlertMixins.showAlertDialog(MainActivity.this,"Future Update...","This Update Implement In Future!",false);
                }
            });


            //----------------------------------Start Login Code----------------------------------
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //***Progress Dialog Box***
                    progressdialog = ProgressDialog.show(
                            MainActivity.this, "Authenticating... ",
                            "Authenticating Please Wait..", true);
                    progressdialog.setCancelable(false);

                    //***Using Authentication Get Token***
                    Map<String,String> params = new HashMap<>();
                    params.put("username",editUser.getText().toString().toLowerCase().trim());
                    params.put("password",editPass.getText().toString());
                    JSONObject jsonObject = new JSONObject(params);

                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, authenticateTokenUrl, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getString("category").equals("student")){
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("token",response.getString("token"));
                                            editor.putString("user_id",response.getString("user_id"));
                                            editor.putString("category",response.getString("category"));
                                            editor.commit();
                                            progressdialog.dismiss();
                                            Intent in = new Intent(MainActivity.this, StudentDashboard.class);
                                            startActivity(in);
                                        }

                                        else if(response.getString("category").equals("faculty")){
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("token",response.getString("token"));
                                            editor.putString("user_id",response.getString("user_id"));
                                            editor.putString("category",response.getString("category"));
                                            editor.commit();
                                            progressdialog.dismiss();
                                            Intent in = new Intent(MainActivity.this,LecturalDashboard.class);
                                            startActivity(in);
                                        }
                                        else{
                                            progressdialog.dismiss();
                                            Vibrator v = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                                            v.vibrate(1000);
                                            MyAlertMixins.showAlertDialog(MainActivity.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                                            Intent in = new Intent(MainActivity.this,MainActivity.class);
                                            startActivity(in);
                                        }
                                    } catch (JSONException e) {
                                        progressdialog.dismiss();
                                        Vibrator v = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                                        v.vibrate(1000);
                                        MyAlertMixins.showAlertDialog(MainActivity.this,"✘ Authentication Failed","Invalid Credentials...",false);
                                        editPass.setText("");
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressdialog.dismiss();
                            Vibrator v = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                            v.vibrate(1000);
                            MyAlertMixins.showAlertDialog(MainActivity.this,"✘ Authentication Failed","Invalid Credentials...",false);
                            editPass.setText("");
                        }
                    });

                    VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);

                }
            });
            //----------------------------------End Login Code----------------------------------
            
            
        }
        //----------------------------------If Not Session Start----------------------------------

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this,"APP EXIT SUCCESSFULLY", Toast.LENGTH_LONG).show();
        finishAffinity();
        super.onBackPressed();

    }
}