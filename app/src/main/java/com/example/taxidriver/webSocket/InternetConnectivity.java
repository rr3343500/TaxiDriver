// check internet connection
package com.example.taxidriver.webSocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.example.taxidriver.usersession.UserSession;


public class InternetConnectivity extends BroadcastReceiver {
    WebSocketConnection webSocketConnection = null;
    UserSession userSession;


    //set web socket objects permanent
    public void setConnect(WebSocketConnection webSocketConnection)
    {
        this.webSocketConnection = webSocketConnection;
    }
    //end of set web socket objects permanent


    //method check internet connection if true then send data
    @Override
    public void onReceive(Context context, Intent intent ) {
        userSession=new UserSession(context);
        String JSON_STRING= "{\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"area\":\""+userSession.getStreetname()+"\",\"type\":\"updateVechleLocation\"}";
        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);

        if(isConnected){
              webSocketConnection.SendData(JSON_STRING);

        }
        else{

        }
    }


}
