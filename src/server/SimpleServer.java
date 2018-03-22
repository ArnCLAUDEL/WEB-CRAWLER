package server;

import java.util.logging.Level;

import util.Cheat;

public class SimpleServer extends AbstractUDPServer {
	
	public SimpleServer(int port) {
		super(port);
	}	

	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		
		Server server = new SimpleServer(8080);
		Thread t1;
		
		t1 = new Thread(server);
		
		t1.start();
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
}