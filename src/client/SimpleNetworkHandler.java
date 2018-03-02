package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import util.Cheat;

public class SimpleNetworkHandler extends AbstractNetworkHandler {
	
	private final Client client;
	
	private boolean stop;
	
	public SimpleNetworkHandler(SocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ);
		this.client = client;
		this.stop = false;
	}

	@Override
	protected boolean stop() {
		return !client.isConnected() || stop;
	}
	
	@Override
	public void shutdown() {
		stop = true;
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk) {
		buffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(sc.read(buffer) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
				return;
			}
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		buffer.flip();
		String message = Cheat.CHARSET.decode(buffer).toString();
		System.err.println(message);
	}	

}
