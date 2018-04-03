package server;

import java.io.IOException;
import java.util.logging.Level;

import client.SimpleClient;
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
		if(args.length>0) {
			SimpleClient.main(args);
			server.scan(args[0]);
			}
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
}