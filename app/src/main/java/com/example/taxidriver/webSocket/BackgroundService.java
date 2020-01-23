//keep background service alive
package com.example.taxidriver.webSocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    WebSocketConnection webSocketConnection;
    Context context;
    protected Handler handler;

    //constructer to set context
    public BackgroundService(Context context)
    {
        this.context=context;

    }
    // end of constructer to set context

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

     //start background service
    public int onStartCommand(final Intent intent, int flags, int startId ) {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.setContext(context);
        final InternetConnectivity internetConnectivity= new InternetConnectivity();
        webSocketConnection.onCreate();
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                internetConnectivity.setConnect(webSocketConnection);
                internetConnectivity.onReceive(context,intent);
            }
        }, 0, 10000);

            }
        });
        return START_STICKY;
    }
    //end of start background service

    //stopping the  background service
    @Override
    public void onDestroy() {
        super.onDestroy();

        webSocketConnection.closeConnection();
    }
    //end of stopping the  background service
}
