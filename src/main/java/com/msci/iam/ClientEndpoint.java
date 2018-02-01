package com.msci.iam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/realtime/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ClientEndpoint {
  
    protected Session session;
    private static Set<ClientEndpoint> clientEndpoints = new CopyOnWriteArraySet<ClientEndpoint>();
    private static HashMap<String, String> users = new HashMap<String, String>();
    private static Broadcaster ticker = new Broadcaster(clientEndpoints);
 
    @OnOpen
    public void onOpen(
    		Session session, 
    		@PathParam("username") String username) throws IOException, EncodeException {
  
        this.session = session;
        clientEndpoints.add(this);
        users.put(session.getId(), username);
 
        Message message = new Message();
        message.setContent("Welcome " + username + "!");
        broadcast(message);
    }
 
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        broadcast(message);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
  
        clientEndpoints.remove(this);
        Message message = new Message();
        message.setContent("Disconnected!");
        broadcast(message);
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
 
    private static void broadcast(Message message) throws IOException, EncodeException {
  
        clientEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
