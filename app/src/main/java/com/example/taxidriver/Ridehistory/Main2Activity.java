package com.example.taxidriver.Ridehistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.taxidriver.R;
import com.example.taxidriver.config.Constants;
import com.example.taxidriver.connection.ConnectionServer;
import com.example.taxidriver.connection.JsonHelper;
import com.example.taxidriver.usersession.UserSession;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    RecyclerView  ridehistory;
    List<HistoryData> historyData;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        ridehistory= findViewById(R.id.recycle);
        userSession= new UserSession(Main2Activity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Main2Activity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ridehistory.setLayoutManager(linearLayoutManager);


        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.RIDEHISTORY);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("driver_id",userSession.getUserDetails().get("driverid"));

        historyData = new ArrayList<HistoryData>();
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {

                Log.e("output",output);

                JsonHelper jsonHelper = new JsonHelper(output);

                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");

                    if (response.equals("TRUE")) {
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");

                        Log.e("json", String.valueOf(jsonArray));

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            jsonHelper.setChildjsonObj(jsonArray,i);

                            historyData.add(new HistoryData(jsonHelper.GetResult("date_time"),jsonHelper.GetResult("amount"),jsonHelper.GetResult("pickup_city_name"),jsonHelper.GetResult("drop_city_name"),jsonHelper.GetResult("distance_km")));

                        }

                        RideHistoryAdaptor userListAdapter = new RideHistoryAdaptor(Main2Activity.this,historyData);

                        ridehistory.setAdapter(userListAdapter);

                    }
                }
            }
        });

    }



    }

