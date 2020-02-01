package com.example.taxidriver.Request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.taxidriver.R;
import com.example.taxidriver.extended.ButtonFonts;
import com.example.taxidriver.extended.TexiFonts;

import java.util.Timer;
import java.util.TimerTask;

public class RequestDialog    {
    Dialog dialog;
    Context context;
    TexiFonts streetname, citynamr;
    ButtonFonts confirm;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Handler handler;


    public RequestDialog(Context context) {
//        super(context);
        this.context=context;




    }



    public void createDialog(Context context)
    {
//
//        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        builder = new AlertDialog.Builder(context);
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.request_dialog, viewGroup, false);
//        streetname=dialogView.findViewById(R.id.Streetname);
//        citynamr=dialogView.findViewById(R.id.cityname);
//        confirm=dialogView.findViewById(R.id.confirm);
//        builder.setView(dialogView);
//        alertDialog = builder.create();
//        alertDialog.show();

                builder.setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .create();


//        Toast.makeText(context, "hi i am fdnj", Toast.LENGTH_SHORT).show();


//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ConfirmBooking();
//
//            }
//        });
    }

    public void ConfirmBooking(){

//         alertDialog.cancel();
    }
}
