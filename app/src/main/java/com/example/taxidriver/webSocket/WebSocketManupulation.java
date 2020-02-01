package com.example.taxidriver.webSocket;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.taxidriver.Activity.Login;
import com.example.taxidriver.Activity.Login_check;
import com.example.taxidriver.Activity.Request;
import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.R;
import com.example.taxidriver.Request.RequestDialog;
import com.example.taxidriver.usersession.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.Socket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;

public class WebSocketManupulation extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    Context context;
    UserSession userSession;
    MainActivity mainActivity;
    WebSocket webSocket;
    BackgroundService backgroundService;
    Intent intent;
    Request  request;
    private WebSocket webSocketss;
    AlertDialog alertBuilder;

//    private onDataSend ondataSend;

    public WebSocketManupulation(Context context ) {
        this.context=context;
        userSession= new UserSession(context);
        mainActivity= new MainActivity();
        backgroundService= new BackgroundService(context);
        intent= new Intent(context,BackgroundService.class);



    }

//    public void setOnDataSend(onDataSend ondataSend)
//    {
//        this.ondataSend = ondataSend;
//    }


    public void setsocket(WebSocket webSocket){
        this.webSocket=webSocket;
    }

//    public WebSocket getScoket()
//    {
//        return this.webSocket;
//    }

    //set context in websocket class
    public void setContext(Context context)
    {
        this.context=context;
        userSession= new UserSession(context);
    }

    //set context in websocket class



    // send data to web socket
    public void SendData(String json  ,WebSocket webSocket)
    {
        if(userSession.getSocketConnection()){
          webSocket.send(json);
        }

    }
    //end of send data to web socket




    //destroy web socket connection
    public void closeConnection(WebSocket webSocket)
    {
        if(userSession.getSocketConnection())
        {
            userSession.setSocketConnection(false);
            final int NORMAL_CLOSURE_STATUS = 1000;
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
    }
    //end of destroy web socket connection




    @SuppressLint("WrongConstant")
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        String JSON_STRING = "{\"userID\":\""+userSession.getUserDetails().get("driverid")+"\",\"userType\":\"Driver\",\"type\":\"initiate\"}";
        webSocket.send(JSON_STRING);
        this.setsocket(webSocket);
//        ondataSend.dataSend(webSocket);
        userSession.setsocket(webSocket);
        userSession.setSocketConnection(true);
//        sendRequest(JSON_STRING,webSocket);


    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {

        Log.e("text",text);
//        requestDialog.createDialog(context);
        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(text);
            String requesttype=mainObject.getString("type");
            switch(requesttype){
                //Case statements
                case "vehicleRequest":

                    JSONObject clientdata = mainObject.getJSONObject("data");
                  String  clientid=clientdata.getString("userId");
                    final String JSON_STRING= "{\"userID\" :\""+clientid+"\",\"driverID\":\""+userSession.getUserDetails().get("driverid")+"\",\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"type\":\"acceptReq\"}";

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error").setMessage("vhgvgh").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });






                    //==================
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("Dialog created in a separate class!");
//                    builder.show();
//                    Intent intent=new Intent(context, Request.class);
//                    intent.putExtra("data", String.valueOf(webSocket));
//                    intent.putExtra("json",text);
//                    context.startActivity(intent);
//                    Animatoo.animateShrink(context);

                    //================



                    webSocket.send(JSON_STRING);
//                    sendRequest(text,webSocket);
                    break;
                case "dhioj":
                    System.out.println("20");
                    break;
                case "sdu": System.out.println("30");
                    break;
                //Default case statement
                default:

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {

    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        userSession.setSocketConnection(false);


    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        userSession.setSocketConnection(false);
//            SocketConnection=false;
//        mainActivity.onCreate();


    }


}

