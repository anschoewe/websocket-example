package com.msci.iam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.EncodeException;

public class Broadcaster
{
	private Timer timer;
	private Set<ClientEndpoint> clientEndpoints;
	private DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	
	TimerTask task = new TimerTask() {
		
		@Override
		public void run()
		{
			Message m = new Message();
			m.setContent(formatter.format(LocalDateTime.now()));
			try {
				broadcast(m);
			} catch (Exception e) {
				System.err.println("problem sending scheduled message: " + e.getMessage());
			}
		}
	};
	
	public Broadcaster(Set<ClientEndpoint> clientEndpoints) {
		this.timer = new Timer(true);	
		this.clientEndpoints = clientEndpoints;
		start();
	}
	
	public void start() {
		long delay  = 1000L; //wait 1 sec before starting...
    long period = 7000L; //then repeat task every 7 seconds
		this.timer.schedule(task, delay, period);
	}
	
	public void stop() {
		this.timer.cancel();
	}
	
  private void broadcast(Message message) throws IOException, EncodeException {
    
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
