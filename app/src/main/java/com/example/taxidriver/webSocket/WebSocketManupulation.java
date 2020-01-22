package com.example.taxidriver.webSocket;

import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.tubesock.WebSocket;
import com.google.firebase.database.tubesock.WebSocketEventHandler;

import java.net.URI;



public class WebSocketManupulation extends WebSocket {

    @Override
    public void blockClose() throws InterruptedException {
        super.blockClose();
    }

    public WebSocketManupulation(ConnectionContext context, URI url) {
        super(context, url);
    }

    @Override
    public synchronized void connect() {
        super.connect();
    }

    @Override
    public synchronized void send(String data) {
        super.send(data);
    }

    @Override
    public void setEventHandler(WebSocketEventHandler eventHandler) {
        super.setEventHandler(eventHandler);
    }
}