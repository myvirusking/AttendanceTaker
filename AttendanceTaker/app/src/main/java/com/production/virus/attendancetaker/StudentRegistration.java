package com.production.virus.attendancetaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    EditText editUser,editFirstName,editLastName,editEmail,editContact,editPassword,editConPassword;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;
    Button btnRegistration;
    Spinner spinDept,spinClass;
    boolean validate;
    //String studentCreateUrl = "http://10.0.2.2:8000/attendance/api/student/create/";
    //String studentValidateUrl ="http://10.0.2.2:8000/attendance/api/student/user-validate/";

    String studentCreateUrl = "http://192.168.1.105:8000/attendance/api/student/create/";
    String studentValidateUrl ="http://192.168.1.105:8000/attendance/api/student/user-validate/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

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

        int selectedId = radioGenderGroup.getCheckedRadioButtonId();
        radioGenderButton = findViewById(selectedId);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate = allRequiredValidation();

                System.err.println("validate ======= " + validate);

                //Student Registration
                if(validate == true){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,studentCreateUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(StudentRegistration.this,"You Account Register Successfully",Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(StudentRegistration.this,StudentLogin.class);
                                    startActivity(in);
                                }
                            },
                                new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(StudentRegistration.this,error.toString(),Toast.LENGTH_LONG).show();
                                }
                        }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("user_id.username",editUser.getText().toString().toLowerCase());
                            params.put("user_id.first_name",editFirstName.getText().toString());
                            params.put("user_id.last_name",editLastName.getText().toString());
                            params.put("user_id.email",editEmail.getText().toString().toLowerCase());
                            params.put("user_id.password",editPassword.getText().toString());
                            params.put("clas",spinClass.getSelectedItem().toString());
                            params.put("dept",spinDept.getSelectedItem().toString());
                            params.put("gender",radioGenderButton.getText().toString());
                            params.put("contact",editContact.getText().toString());
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(StudentRegistration.this).addToRequestQueue(stringRequest);


                }
                // End Student Registration


            }
        });




    }


    protected boolean allRequiredValidation(){
        //Username And Email Validation
        Map<String,String> params = new HashMap<>();
        params.put("username",editUser.getText().toString());
        params.put("email",editEmail.getText().toString());
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, studentValidateUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean username_match = Boolean.parseBoolean(response.getString("username_match"));
                            boolean email_match = Boolean.parseBoolean(response.getString("email_match"));
                            if(username_match == true){
                                editUser.setError("Username Already Registered!");
                                validate = false;
                            }
                            else if(email_match == true){
                                editEmail.setError("Email Already Registered!");
                                validate = false;
                            }
                            else{
                                System.err.println("===================Else Called========================= ");
                                validate = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentRegistration.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(StudentRegistration.this).addToRequestQueue(jsonArrayRequest);
        //End Username And Email Validation

        if(validate == false){
            return false;
        }
        else {
            // All Essential + Required Fields Validator ========================================
            if (editUser.length() == 0) {
                editUser.setError("Username Cannot Be Empty!");
            } else if (!editUser.getText().toString().matches("[a-zA-Z0-9_]+")) {
                editUser.setError("Please Enter Valid Username!");
            } else if (editFirstName.length() == 0) {
                editFirstName.setError("FirstName Cannot Be Empty!");
            } else if (!editFirstName.getText().toString().matches("[a-zA-Z]+")) {
                editFirstName.setError("Please Enter Valid First Name!");
            } else if (editLastName.length() == 0) {
                editLastName.setError("LastName Cannot Be Empty!y");
            } else if (!editLastName.getText().toString().matches("[a-zA-Z]+")) {
                editLastName.setError("Please Enter Valid Last Name!");
            } else if (editEmail.length() == 0) {
                editEmail.setError("Email Cannot Be Empty!");
            } else if (!editEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                editEmail.setError("Please Enter Valid Email!");
            } else if (editContact.length() == 0) {
                editContact.setError("Contact Cannot Be Empty!");
            } else if (!editContact.getText().toString().matches("[0-9]{10}")) {
                editContact.setError("Please Enter Valid 10 Digit Contact Number");
            } else if (editPassword.length() < 8) {
                editPassword.setError("Password Must Contain At least 8 Characters!");
            } else if (editConPassword.length() == 0) {
                editConPassword.setError("Confirm Password Cannot Be Empty!");
            } else if (!editConPassword.getText().toString().equals(editPassword.getText().toString())) {
                editConPassword.setError("Password Cannot Matched!");
            } else {
                return true;
            }
            return false;
        }

        // End All Essential + Required Fields Validator ====================================
    }

}
