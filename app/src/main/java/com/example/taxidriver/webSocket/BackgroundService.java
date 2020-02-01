//keep background SocketService alive
package com.example.taxidriver.webSocket;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.example.taxidriver.Activity.Login;
import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.usersession.UserSession;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.app.Service.START_NOT_STICKY;

@SuppressLint("NewApi")
public class BackgroundService  extends Service{
    WebSocket ws;
    Context context;
    UserSession userSession;
    protected Handler handler;
   WebSocketManupulation webSocketManupulation;

    //constructer to set context
    public BackgroundService(Context context)
    {
//        super("BackgroundService");
        this.context=context;
        userSession= new UserSession(context);


    }
    // end of constructer to set context

public WebSocket getwebsocket()
{
    return ws;
}


//    start background SocketService
    public int onStartCommand(final Intent intent, int flags, int startId ) {
        webSocketManupulation= new WebSocketManupulation(context);

        //final WebSocket webs = webSocketManupulation.getScoket();

        final InternetConnectivity internetConnectivity= new InternetConnectivity();

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(userSession.getSocketConnection()) {
                    internetConnectivity.setConnect(webSocketManupulation ,ws);
                    internetConnectivity.onReceive(context, intent);
                }
            }
        }, 30, 5000);

            }
        });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    end of start background SocketService

    //stopping the  background SocketService
    @Override
    public void onDestroy() {
        webSocketManupulation.closeConnection(ws);
        super.onDestroy();
    }
    //end of stopping the  background SocketService



    // initialize web socket connection
    public void onCreate()
    {
        final OkHttpClient client;
        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
        WebSocketManupulation listener = new WebSocketManupulation(context);
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService();
    }
    //end  of initialize web socket connection


}

