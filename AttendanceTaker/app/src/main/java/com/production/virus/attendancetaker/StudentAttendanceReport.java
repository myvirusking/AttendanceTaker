package com.production.virus.attendancetaker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class StudentAttendanceReport extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    EditText txtFromDate,txtToDate;
    TableLayout tblLayoutReport;
    Button btnGenerateReport;
    String from_date,to_date;
    ScrollView scrollView;
    ProgressDialog progressdialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String studentReportUrl = MyAlertMixins.urlPrefix+"attendance/api/student/attendance-report/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_report);

        // Checking For Internet Connection
        if(new NoInternetMixins(this).getInternetStatus()){
        }


        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        scrollView = findViewById(R.id.scrollView);

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

        tblLayoutReport = findViewById(R.id.tblLayoutReport);
        tblLayoutReport.setStretchAllColumns(true);
        sharedPreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        //----------------------------------Start Generate Report----------------------------------
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        StudentAttendanceReport.this, "Report Generating... ",
                        "Report Generating Please Wait..", true);
                progressdialog.setCancelable(false);

                Map<String,String> params = new HashMap<>();
                params.put("user_id",sharedPreferences.getString("user_id",""));
                params.put("from_date",from_date);
                params.put("to_date",to_date);
                final JSONObject jsonObject = new JSONObject(params);

                scrollView.setVisibility(View.VISIBLE);

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, studentReportUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    tblLayoutReport.removeAllViews();

                                    // For Header Entry On Table
                                    TableRow tblHeader = new TableRow(StudentAttendanceReport.this);
                                    TextView tvHeader1 = new TextView(StudentAttendanceReport.this);
                                    tvHeader1.setText("Date | Subject");
                                    tvHeader1.setTextColor(Color.BLACK);
                                    tvHeader1.setPadding(15,5,15,5);
                                    tvHeader1.setTextSize(18);
                                    tvHeader1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvHeader1.setGravity(Gravity.CENTER);
                                    tvHeader1.setBackground(getResources().getDrawable(R.drawable.cell_shapes_pink));
                                    tblHeader.addView(tvHeader1);

                                    JSONArray jsonArraySubjects = response.getJSONArray("subjects");
                                    for(int i=0;i<jsonArraySubjects.length();i++){
                                        TextView tvHeader2  = new TextView(StudentAttendanceReport.this);
                                        tvHeader2 .setText(jsonArraySubjects.getString(i));
                                        tvHeader2 .setTextColor(Color.BLACK);
                                        tvHeader2 .setPadding(15,5,15,5);
                                        tvHeader2 .setTextSize(18);
                                        tvHeader2 .setTypeface(Typeface.DEFAULT_BOLD);
                                        tvHeader2 .setGravity(Gravity.CENTER);
                                        tvHeader2 .setBackground(getResources().getDrawable(R.drawable.cell_shapes_pink));
                                        tblHeader.addView(tvHeader2);
                                    }
                                    tblLayoutReport.addView(tblHeader);


                                    // For Data Entry On Table
                                    JSONArray jsonArrayDates= response.getJSONArray("dates");
                                    JSONArray jsonArrayValues= response.getJSONArray("values");
                                    for(int i=0;i<jsonArrayDates.length();i++){
                                        TableRow tblData = new TableRow(StudentAttendanceReport.this);
                                        TextView tvData1 = new TextView(StudentAttendanceReport.this);
                                        tvData1.setText(jsonArrayDates.getString(i));
                                        tvData1.setTextColor(Color.BLACK);
                                        tvData1.setPadding(15,5,15,5);
                                        tvData1.setTextSize(18);
                                        tvData1.setTypeface(Typeface.DEFAULT_BOLD);
                                        tvData1.setGravity(Gravity.CENTER);
                                        if(i%2!=0){
                                            tvData1.setBackground(getResources().getDrawable(R.drawable.cell_shapes_blue));
                                        }
                                        else{
                                            tvData1.setBackground(getResources().getDrawable(R.drawable.cell_shapes_white));
                                        }
                                        tblData.addView(tvData1);

                                        JSONArray jsonArrayCuurentValues = jsonArrayValues.getJSONArray(i);
                                        for(int j=0;j<jsonArrayCuurentValues.length();j++){
                                            TextView tvData2 = new TextView(StudentAttendanceReport.this);
                                            tvData2.setText(jsonArrayCuurentValues.getString(j));
                                            tvData2.setTextColor(Color.BLACK);
                                            tvData2.setTextSize(18);
                                            if(i%2!=0){
                                                tvData2.setBackground(getResources().getDrawable(R.drawable.cell_shapes_blue));
                                            }
                                            else{
                                                tvData2.setBackground(getResources().getDrawable(R.drawable.cell_shapes_white));
                                            }
                                            tvData2.setPadding(15,5,15,5);
                                            tvData2.setGravity(Gravity.CENTER);
                                            tvData2.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                                            tblData.addView(tvData2);
                                        }
                                        tblLayoutReport.addView(tblData);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressdialog.dismiss();
                            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressdialog.dismiss();
                    Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    assert v != null;
                    v.vibrate(1000);
                    MyAlertMixins.showAlertDialog(StudentAttendanceReport.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header = new HashMap<>();
                        header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                        return header;
                    }
                };
                VolleySingleton.getInstance(StudentAttendanceReport.this).addToRequestQueue(jsonArrayRequest);}

        });
        //----------------------------------End Generate Report----------------------------------
    }



    //----------------------------------Start Select Date Range----------------------------------
    public void fromDate(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentAttendanceReport.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                txtFromDate.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(monthOfYear).append("/").append(year));
                from_date = String.valueOf(dayOfMonth) + "/" +
                        monthOfYear + "/" + year;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void toDate(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentAttendanceReport.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                txtToDate.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(monthOfYear).append("/").append(year));
                to_date = String.valueOf(dayOfMonth) + "/" +
                        monthOfYear + "/" + year;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    //----------------------------------End Select Date Range----------------------------------




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
                Toast.makeText(StudentAttendanceReport.this,"LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(StudentAttendanceReport.this,MainActivity.class);
                startActivity(in);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
