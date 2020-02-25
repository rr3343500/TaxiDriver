package com.iwish.taxidriver.Distance;

import android.util.Log;
import com.iwish.taxidriver.config.Constants;
import com.iwish.taxidriver.connection.ConnectionServer;
import com.iwish.taxidriver.connection.JsonHelper;

public class DistanceTimeCalculate {

    public DistanceTimeCalculate(String picuplat, String picuplong, String droplat, String droplong)
    {
//        setProgressDialog("Send OTP ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.DISTANNCETIME);
        connectionServer.buildParameter("PickLatitude", String.valueOf(picuplat));
        connectionServer.buildParameter("PickLogitude", String.valueOf(picuplong));
        connectionServer.buildParameter("destinationsLatitude", String.valueOf(droplat));
        connectionServer.buildParameter("destinationsLogitude", String.valueOf(droplong));

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
//
                    }

                }

            }
        });

    }



}
