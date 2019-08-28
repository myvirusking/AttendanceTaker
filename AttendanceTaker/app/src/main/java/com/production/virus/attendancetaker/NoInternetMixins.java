package com.production.virus.attendancetaker;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

public class NoInternetMixins {
    private Context context;
    Dialog dialog1;

    public NoInternetMixins(Context context) {
        this.context = context;
        dialog1 = new Dialog(context, R.style.df_dialog);
    }

    public void showNoInternetDialog() {
        dialog1.setContentView(R.layout.activity_no_internet);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getInternetStatus()){
                    dialog1.dismiss();
                }
                else{
                    Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    Toast.makeText(context, "No Connectivity!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog1.show();
    }

    public boolean getInternetStatus() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            showNoInternetDialog();
        }
        return isConnected;
    }


}