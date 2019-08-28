package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;

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


public class StudentRegistration extends AppCompatActivity {
    EditText editUser, editFirstName, editLastName, editEmail, editContact, editPassword, editConPassword;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;
    Button btnRegistration;
    Spinner spinDept, spinClass;
    ProgressDialog progressdialog;
    boolean validate = false;
    int selectedId;
    //String studentCreateUrl = "http://10.0.2.2:8000/attendance/api/student/create/";
    //String studentValidateUrl ="http://10.0.2.2:8000/attendance/api/student/user-validate/";
    String studentCreateUrl = MyAlertMixins.urlPrefix + "attendance/api/student/create/";
    String studentValidateUrl = MyAlertMixins.urlPrefix + "attendance/api/student/user-validate/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        // Checking For Internet Connection
        if (new NoInternetMixins(this).getInternetStatus()) {
        }


        editUser = findViewById(R.id.editUser);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        spinDept = findViewById(R.id.spinDept);
        spinClass = findViewById(R.id.spinClass);
        editContact = findViewById(R.id.editContact);
        radioGenderGroup = findViewById(R.id.radioGender);
        editPassword = findViewById(R.id.editPassword);
        editConPassword = findViewById(R.id.editConPassword);
        btnRegistration = findViewById(R.id.btnRegistration);
        selectedId = radioGenderGroup.getCheckedRadioButtonId();
        radioGenderButton = findViewById(selectedId);


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


        //----------------------------------Start Student Registration Code----------------------------------
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate = allRequiredValidation();
                if (validate == true) {
                    //***Progress Dialog Box***
                    progressdialog = ProgressDialog.show(
                            StudentRegistration.this, "Registering... ",
                            "Registering Please Wait..", true);
                    progressdialog.setCancelable(false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, studentCreateUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressdialog.dismiss();
                                    Toast.makeText(StudentRegistration.this, "✔ Your Account Register Successfully", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(StudentRegistration.this, MainActivity.class);
                                    in.putExtra("user", editUser.getText().toString().toLowerCase());
                                    startActivity(in);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressdialog.dismiss();
                                    Vibrator v = (Vibrator) getSystemService(StudentRegistration.this.VIBRATOR_SERVICE);
                                    v.vibrate(1000);
                                    MyAlertMixins.showAlertDialog(StudentRegistration.this, "✘ API Not Responding", "Please Contact With Admin!...", false);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id.username", editUser.getText().toString().toLowerCase().trim());
                            params.put("user_id.first_name", editFirstName.getText().toString());
                            params.put("user_id.last_name", editLastName.getText().toString());
                            params.put("user_id.email", editEmail.getText().toString().toLowerCase().trim());
                            params.put("user_id.password", editPassword.getText().toString());
                            params.put("clas", spinClass.getSelectedItem().toString());
                            params.put("dept", spinDept.getSelectedItem().toString());
                            params.put("gender", radioGenderButton.getText().toString());
                            params.put("contact", editContact.getText().toString());
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(StudentRegistration.this).addToRequestQueue(stringRequest);

                }
            }
        });
        //----------------------------------End Student Registration Code----------------------------------

    }


    //----------------------------------Start All Essential + Required Fields Validation----------------------------------
    protected boolean allRequiredValidation() {

        //***Required Fields Validation***
        if (editFirstName.length() == 0) {
            editFirstName.setError("FirstName Cannot Be Empty!");
            validate = false;
        } else if (!editFirstName.getText().toString().matches("[a-zA-Z]+")) {
            editFirstName.setError("Please Enter Valid First Name!");
            validate = false;
        } else if (editLastName.length() == 0) {
            editLastName.setError("LastName Cannot Be Empty!y");
            validate = false;
        } else if (!editLastName.getText().toString().matches("[a-zA-Z]+")) {
            editLastName.setError("Please Enter Valid Last Name!");
            validate = false;
        } else if (editEmail.length() == 0) {
            editEmail.setError("Email Cannot Be Empty!");
            validate = false;
        } else if (!editEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
            editEmail.setError("Please Enter Valid Email!");
            validate = false;
        } else if (editUser.length() == 0) {
            editUser.setError("Username Cannot Be Empty!");
            validate = false;
        } else if (!editUser.getText().toString().matches("[a-zA-Z0-9_]+")) {
            editUser.setError("Please Enter Valid Username!");
            validate = false;
        } else if (editContact.length() == 0) {
            editContact.setError("Contact Cannot Be Empty!");
            validate = false;
        } else if (!editContact.getText().toString().matches("[0-9]{10}")) {
            editContact.setError("Please Enter Valid 10 Digit Contact Number");
            validate = false;
        } else if (editPassword.length() < 8) {
            editPassword.setError("Password Must Contain At least 8 Characters!");
            validate = false;
        } else if (editConPassword.length() == 0) {
            editConPassword.setError("Confirm Password Cannot Be Empty!");
            validate = false;
        } else if (!editConPassword.getText().toString().equals(editPassword.getText().toString())) {
            editConPassword.setError("Password Cannot Matched!");
            validate = false;
        } else {
            //***Progress Dialog Box***
            progressdialog = ProgressDialog.show(
                    StudentRegistration.this, "Validating... ",
                    "Validating Please Wait..", true);
            progressdialog.setCancelable(false);

            Map<String, String> params = new HashMap<>();
            params.put("username", editUser.getText().toString());
            params.put("email", editEmail.getText().toString());
            JSONObject jsonObject = new JSONObject(params);

            //***Username And Email Validation***
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, studentValidateUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressdialog.dismiss();
                                boolean username_match = Boolean.parseBoolean(response.getString("username_match"));
                                boolean email_match = Boolean.parseBoolean(response.getString("email_match"));
                                if (username_match == true) {
                                    editUser.setError("Username Already Registered!");
                                    validate = false;
                                } else if (email_match == true) {
                                    editEmail.setError("Email Already Registered!");
                                    validate = false;
                                } else {
                                    validate = true;
                                }
                            } catch (JSONException e) {
                                progressdialog.dismiss();
                                Vibrator v = (Vibrator) getSystemService(StudentRegistration.this.VIBRATOR_SERVICE);
                                v.vibrate(1000);
                                MyAlertMixins.showAlertDialog(StudentRegistration.this, "✘ API Not Responding", "Please Contact With Admin!...", false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressdialog.dismiss();
                    Vibrator v = (Vibrator) getSystemService(StudentRegistration.this.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    MyAlertMixins.showAlertDialog(StudentRegistration.this, "✘ API Not Responding", "Please Contact With Admin!...", false);
                }
            });

            VolleySingleton.getInstance(StudentRegistration.this).addToRequestQueue(jsonArrayRequest);

        }

        return validate;
    }
    //----------------------------------Start All Essential + Required Fields Validation----------------------------------

}
