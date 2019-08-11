package com.production.virus.attendancetaker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class MyAlertDialog {
    //protected static String urlPrefix = "http://192.168.1.106:8000/";
    protected static String urlPrefix = "http://myvirusking.pythonanywhere.com/";
    public static void showAlertDialog(Context ctx ,String title, String message, Boolean status){
        final String msg = title;
        final Context context = ctx;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);
        alertBuilder.setMessage(message)
                .setCancelable(status)
                .setTitle(title);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.show();
    }
}
