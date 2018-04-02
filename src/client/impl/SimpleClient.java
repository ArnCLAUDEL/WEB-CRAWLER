package client.impl;

import java.io.IOException;
import java.util.logging.Level;

import client.Client;
import util.Cheat;

public class SimpleClient extends AbstractTCPClient {

	public SimpleClient(String hostname, int port, long previousId) {
		super(hostname, port, previousId);
	}
	
	public SimpleClient(String hostname, int port) {
		this(hostname, port, Cheat.DEFAULT_ID);
	}
	
	public static void main(String[] args) throws IOException {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		
		Client client = new SimpleClient("localhost", 8080);
		Thread t1;
		
		t1 = new Thread(client);
		
		t1.start();
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
}
