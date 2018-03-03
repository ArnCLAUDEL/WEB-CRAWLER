package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import util.Cheat;

public class ServerNetworkHandler extends AbstractNetworkHandler {

	public ServerNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
	}
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		try {
			SocketChannel socket = ((ServerSocketChannel) sk.channel()).accept();
			addChannel(socket, SelectionKey.OP_READ);
			Cheat.LOGGER.log(Level.INFO,"Connection from " + socket.getRemoteAddress() + " accepted");
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.FINER, "Message received.");
		buffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(sc.read(buffer) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
				return;
			}
			buffer.flip();
			sc.write(buffer.slice());
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		String message = Cheat.CHARSET.decode(buffer).toString();
		System.out.println(message);
	}

}
