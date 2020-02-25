package com.iwish.taxidriver.Request;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.iwish.taxidriver.Activity.Login_check;
import com.iwish.taxidriver.Map.MainActivity;
import com.iwish.taxidriver.R;
import com.iwish.taxidriver.config.Constants;
import com.iwish.taxidriver.connection.ConnectionServer;
import com.iwish.taxidriver.connection.JsonHelper;
import com.iwish.taxidriver.extended.ButtonFonts;
import com.iwish.taxidriver.extended.TexiFonts;
import com.iwish.taxidriver.usersession.UserSession;
import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.List;


public class EndtripDialog extends Dialog implements android.view.View.OnClickListener {
    ButtonFonts end;
    UserSession userSession;
    TexiFonts distance, duration, amount;
    Context context;
    double distancecalculated;
    LatLng latLngA, latLngB;
    Geocoder geocoder;
    List<Address> address = null;
    String clientaddress, amt, dis;
    KProgressHUD kProgressHUD;

    public EndtripDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        kProgressHUD = new KProgressHUD(context);
        userSession = new UserSession(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.endtrip_dialog);
//        setProgressDialog("Calculating ...");
        duration = findViewById(R.id.timeduration);
        distance = findViewById(R.id.distance);
        amount = findViewById(R.id.amount);
        end = findViewById(R.id.end);
        userSession = new UserSession(context);
        final String bookingtype = userSession.getBookingtype();


        if ("daily".equals(bookingtype)) {
            latLngA = new LatLng(Double.parseDouble(userSession.getpiclat()), Double.parseDouble(userSession.getpiclong()));
            latLngB = new LatLng(userSession.getLatitute(), userSession.getLogitute());
            try {
                Geocoder geocoder = new Geocoder(context);
                address = geocoder.getFromLocation(userSession.getLatitute(), userSession.getLogitute(), 1);
                clientaddress = address.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Location locationA = new Location("point A");
            locationA.setLatitude(latLngA.latitude);
            locationA.setLongitude(latLngA.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latLngB.latitude);
            locationB.setLongitude(latLngB.longitude);
            distancecalculated = Double.parseDouble(String.format("%.2f", locationA.distanceTo(locationB) / 1000));

            distance(String.valueOf(distancecalculated), userSession.getUserDetails().get("driverid"));
//            distance(userSession.getpiclat(),userSession.getpiclong(),userSession.getLatitute(),userSession.getLogitute());
        } else {
            if ("rental".equals(bookingtype)) {
                latLngA = new LatLng(Double.parseDouble(userSession.getRentaldetail().get("rentalpiclat")), Double.parseDouble(userSession.getRentaldetail().get("rentalpiclong")));
                latLngB = new LatLng(userSession.getLatitute(), userSession.getLogitute());
                try {
                    Geocoder geocoder = new Geocoder(context);
                    address = geocoder.getFromLocation(userSession.getLatitute(), userSession.getLogitute(), 1);
                    clientaddress = address.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Location locationA = new Location("point A");
                locationA.setLatitude(latLngA.latitude);
                locationA.setLongitude(latLngA.longitude);
                Location locationB = new Location("point B");
                locationB.setLatitude(latLngB.latitude);
                locationB.setLongitude(latLngB.longitude);
                distancecalculated = Double.parseDouble(String.format("%.2f", locationA.distanceTo(locationB) / 1000));

                distance(String.valueOf(distancecalculated), userSession.getUserDetails().get("driverid"));

            } else {
                latLngA = new LatLng(Double.parseDouble(userSession.getpiclat()), Double.parseDouble(userSession.getpiclong()));
                latLngB = new LatLng(userSession.getLatitute(), userSession.getLogitute());
                try {
                    Geocoder geocoder = new Geocoder(context);
                    address = geocoder.getFromLocation(userSession.getLatitute(), userSession.getLogitute(), 1);
                    clientaddress = address.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Location locationA = new Location("point A");
                locationA.setLatitude(latLngA.latitude);
                locationA.setLongitude(latLngA.longitude);
                Location locationB = new Location("point B");
                locationB.setLatitude(latLngB.latitude);
                locationB.setLongitude(latLngB.longitude);
                distancecalculated = Double.parseDouble(String.format("%.2f", locationA.distanceTo(locationB) / 1000));

                distance(String.valueOf(distancecalculated), userSession.getUserDetails().get("driverid"));

            }

        }


//       clientid= userSession.getClientID();

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.Booking("false");
                userSession.set_requestdata("null");
                userSession.setLocationStatus(false);
                onBackPressed();
                Animatoo.animateSlideUp(context);

            }
        });

    }


    @Override
    public void onClick(View view) {


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void distance(String distance1, String driverid) {
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.DISTANNCETIME);
        connectionServer.buildParameter("distance", distance1);
        connectionServer.buildParameter("driverid", driverid);
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
//                        String aa = jsonHelper.GetResult("data");
//                        if (aa != null) {
                            amount.setText("₹" + jsonHelper.GetResult("data"));
                            amt = jsonHelper.GetResult("data");
                            distance.setText(String.valueOf(distancecalculated) + " KM");
                            final String JSON_STRING = "{\"picaddress\":\"" + userSession.getClientaddress() + "\",\"dropaddress\":\"" + clientaddress + "\",\"amount\":\"" + jsonHelper.GetResult("data") + "\",\"diatance\":\"" + distancecalculated + "\",\"type\":\"RideConfirm\",\"driverID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"clientID\":\"" + userSession.getClientID() + "\"}";
                            userSession.settripdata(JSON_STRING);
                            userSession.setendTripstatus(true);
//                          remove_progress_Dialog();

//                        }
                    }
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        if ("OutStation".equals(userSession.getBookingtype())) {
            Geocoder geocoder = new Geocoder(context);
            try {
                address = geocoder.getFromLocation(Double.parseDouble(userSession.getRentaldetail().get("rentalpiclat")), Double.parseDouble(userSession.getRentaldetail().get("rentalpiclong")), 1);
                String drop = address.get(0).getAddressLine(0);
                savedata(userSession.getUserDetails().get("driverid"), userSession.getRentaldetail().get("rentalclientid"), drop, clientaddress, String.valueOf(distancecalculated), amt);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if ("daily".equals(userSession.getBookingtype())) {
                savedata(userSession.getUserDetails().get("driverid"), userSession.getClientID(), userSession.getClientaddress(), clientaddress, String.valueOf(distancecalculated), amt);

            } else {
                savedata(userSession.getUserDetails().get("driverid"), userSession.getRentaldetail().get("rentalclientid"), userSession.getClientaddress(), clientaddress, String.valueOf(distancecalculated), amt);

            }
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Animatoo.animateSlideUp(context);
    }

    private void savedata(String driverid, String clientID, String clientaddress, String clientaddress1, String distancecalculated, String amt) {

        userSession.settotalbooking(1, Integer.parseInt(amt), 0);
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.SAVERIDEHISTORY);
        connectionServer.buildParameter("driver_id", driverid);
        connectionServer.buildParameter("client_id", clientID);
        connectionServer.buildParameter("pic", clientaddress);
        connectionServer.buildParameter("drop", clientaddress1);
        connectionServer.buildParameter("amt", amt);
        connectionServer.buildParameter("dis", distancecalculated);
        connectionServer.buildParameter("trackid", userSession.gettrackid());
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {


                    }
                }

            }
        });

    }


    public void setProgressDialog(String msg) {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    public void remove_progress_Dialog() {
        kProgressHUD.dismiss();
    }
}
