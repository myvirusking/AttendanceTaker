package com.production.virus.attendancetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
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


public class AllStudentAttendanceReport extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText editYear;
    Spinner spinMonth, spinClass;
    Button btnGenerateReport;
    ProgressDialog progressdialog;
    TableLayout tblLayoutReport;
    ScrollView scrollView;
    String mYear,mMonth;
    String allStudentReportUrl = MyAlertMixins.urlPrefix+"attendance/api/student/attendance-report/all/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student_attendance_report);
        editYear = findViewById(R.id.editYear);
        spinMonth = findViewById(R.id.spinMonth);
        spinClass = findViewById(R.id.spinClass);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        scrollView = findViewById(R.id.scrollView);
        tblLayoutReport = findViewById(R.id.tblLayoutReport);
        tblLayoutReport.setStretchAllColumns(true);

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

        final Calendar c = Calendar.getInstance();
        mYear = ""+c.get(Calendar.YEAR);
        editYear.setText(mYear);
        sharedPreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //----------------------------------Start Generate Report----------------------------------
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***Progress Dialog Box***
                progressdialog = ProgressDialog.show(
                        AllStudentAttendanceReport.this, "Report Generating... ",
                        "Report Generating Please Wait..", true);
                progressdialog.setCancelable(false);

                String selectedMonth = spinMonth.getSelectedItem().toString();
                if(selectedMonth.equals("January")){ mMonth = "1"; }
                else if(selectedMonth.equals("March")){ mMonth = "3"; }
                else if(selectedMonth.equals("April")){ mMonth = "4"; }
                else if(selectedMonth.equals("May")){ mMonth = "5"; }
                else if(selectedMonth.equals("June")){ mMonth = "6"; }
                else if(selectedMonth.equals("July")){ mMonth = "7"; }
                else if(selectedMonth.equals("August")){ mMonth = "8"; }
                else if(selectedMonth.equals("September")){ mMonth = "9"; }
                else if(selectedMonth.equals("October")){ mMonth = "10"; }
                else if(selectedMonth.equals("November")){ mMonth = "11"; }
                else{ mMonth = "12"; }

                Map<String,String> params = new HashMap<>();
                params.put("user_id",sharedPreferences.getString("user_id",""));
                params.put("month",mMonth);
                params.put("year",mYear.trim());
                params.put("clas", spinClass.getSelectedItem().toString());
                final JSONObject jsonObject = new JSONObject(params);

                scrollView.setVisibility(View.VISIBLE);

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, allStudentReportUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    tblLayoutReport.removeAllViews();

                                    // For Header Entry On Table
                                    TableRow tblHeader = new TableRow(AllStudentAttendanceReport.this);
                                    TextView tvHeader1 = new TextView(AllStudentAttendanceReport.this);
                                    tvHeader1.setText("Name | Subject");
                                    tvHeader1.setTextColor(Color.BLACK);
                                    tvHeader1.setPadding(15,5,15,5);
                                    tvHeader1.setTextSize(18);
                                    tvHeader1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvHeader1.setGravity(Gravity.CENTER);
                                    tvHeader1.setBackground(getResources().getDrawable(R.drawable.cell_shapes_pink));
                                    tblHeader.addView(tvHeader1);

                                    JSONArray jsonArraySubjects = response.getJSONArray("subjects");
                                    for(int i=0;i<jsonArraySubjects.length();i++){
                                        TextView tvHeader2  = new TextView(AllStudentAttendanceReport.this);
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
                                    JSONArray jsonArrayDates= response.getJSONArray("st_names");
                                    JSONArray jsonArrayValues= response.getJSONArray("values");
                                    for(int i=0;i<jsonArrayDates.length();i++){
                                        TableRow tblData = new TableRow(AllStudentAttendanceReport.this);
                                        TextView tvData1 = new TextView(AllStudentAttendanceReport.this);
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
                                            TextView tvData2 = new TextView(AllStudentAttendanceReport.this);
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
                        tblLayoutReport.removeAllViews();
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        assert v != null;
                        v.vibrate(1000);
                        MyAlertMixins.showAlertDialog(AllStudentAttendanceReport.this,"✘ API Not Responding","Please Contact With Admin!...",false);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header = new HashMap<>();
                        header.put("Authorization","Token "+sharedPreferences.getString("token",""));
                        return header;
                    }
                };
                VolleySingleton.getInstance(AllStudentAttendanceReport.this).addToRequestQueue(jsonArrayRequest);}

        });
        //----------------------------------End Generate Report----------------------------------

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
                Toast.makeText(AllStudentAttendanceReport.this, "LOG OUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                Intent in = new Intent(AllStudentAttendanceReport.this, MainActivity.class);
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
