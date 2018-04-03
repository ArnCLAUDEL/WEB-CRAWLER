package server;

import java.io.IOException;
import java.util.logging.Level;

import util.Cheat;

public class SimpleServer extends AbstractTCPServer {
	
	public SimpleServer(int port) {
		super(port);
	}	

	public static void main(String[] args) throws IOException {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		Cheat.initPrintW();
		Server server = new SimpleServer(8080);
		Thread t1;

		t1 = new Thread(server);
		
		t1.start();
		
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
}