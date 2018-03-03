package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import util.Cheat;

public class ClientNetworkHandler extends AbstractNetworkHandler {
	
	public ClientNetworkHandler(SocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk) {
		buffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(sc.read(buffer) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
				return;
			}
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		buffer.flip();
		String message = Cheat.CHARSET.decode(buffer).toString();
		System.out.println(message);
	}	

}
