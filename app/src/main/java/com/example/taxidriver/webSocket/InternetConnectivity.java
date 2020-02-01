// check internet connection
package com.example.taxidriver.webSocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.example.taxidriver.usersession.UserSession;

import okhttp3.WebSocket;


public class InternetConnectivity extends BroadcastReceiver {
    WebSocketManupulation webSocketManupulation = null;
    UserSession userSession;
    private WebSocket websocket;


    //set web socket objects permanent
    public void setConnect(WebSocketManupulation webSocketManupulationm ,WebSocket webSocket)
    {
        this.webSocketManupulation = webSocketManupulationm;
         this.websocket=webSocket;
    }
    //end of set web socket objects permanent


    //method check internet connection if true then send data
    @Override
    public void onReceive(Context context, Intent intent) {
        userSession=new UserSession(context);
//        userSession.setsocket(websocket);
        final String JSON_STRING= "{\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"area\":\""+userSession.getStreetname()+"\",\"type\":\"updateVehicleLocation\"}";
        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);

        if(isConnected){
              webSocketManupulation.SendData(JSON_STRING,websocket);
        }
        else{

        }
    }




}
