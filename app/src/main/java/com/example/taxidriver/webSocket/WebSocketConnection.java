//web socket connection class  easy to manuplulate
package com.example.taxidriver.webSocket;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxidriver.usersession.UserSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class WebSocketConnection extends AppCompatActivity {

    private OkHttpClient client;
    WebSocket ws;
    Context context;






    // initialize web socket connection
    public void onCreate()
    {
        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService();
    }
    //end  of initialize web socket connection



    //set context in websocket class
   public void setContext(Context context)
   {
   this.context=context;
   }

    //set context in websocket class



    // send data to web socket
    public void SendData(String json)
    {
        ws.send(json);
    }
    //end of send data to web socket



    //destroy web socket connection
    public void closeConnection()
    {
        final int NORMAL_CLOSURE_STATUS = 1000;
        ws.close(NORMAL_CLOSURE_STATUS, null);
    }
    //end of destroy web socket connection


    //receive output from web socket
    void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
    //end of receive output from web socket





    //initialize web socket listener
    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        UserSession userSession= new UserSession(context);
        String JSON_STRING = "{\"userID\":\""+userSession.getUserDetails().get("driverid")+"\",\"userType\":\"Driver\",\"type\":\"initiate\"}";

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(JSON_STRING);

        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
            onCreate();

        }


    }
    //initialize web socket listener



}
