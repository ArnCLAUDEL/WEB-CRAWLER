package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;

import util.Cheat;

public abstract class AbstractNetworkHandler extends AbstractHandler implements NetworkHandler {
	private final Selector selector;
	private final int op;
	
	public AbstractNetworkHandler(SocketChannel channel, int op) throws IOException {
		super(channel);
		this.selector = Selector.open();
		this.op = op;
		channel.configureBlocking(false);
		channel.register(selector, op);
	}
	
	protected abstract boolean stop();
	
	private boolean checkOps(SelectionKey sk, int op) {
		return (sk.readyOps() & op & this.op) != 0;
	}
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, this + " starting.");
		Iterator<SelectionKey> itr;
		SelectionKey sk;
		while(!stop()) {
			try {
				selector.select();
				itr = selector.selectedKeys().iterator();
				while(itr.hasNext()) {
					sk = itr.next();
					
					if(checkOps(sk, SelectionKey.OP_ACCEPT))
						handleAcceptOperation(sk);
					
					else if(checkOps(sk, SelectionKey.OP_READ))
						handleReadOperation(sk);

					else if(checkOps(sk, SelectionKey.OP_WRITE))
						handleWriteOperation(sk);
					
					else if(checkOps(sk, SelectionKey.OP_CONNECT))
						handleConnectOperation(sk);
					
					itr.remove();
				}
			} catch (IOException e) {
				Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}
		Cheat.LOGGER.log(Level.INFO, this + " shutting down.");
	}
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.WARNING, this + " ignored Accept Operation");
	}

	@Override
	public void handleReadOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.WARNING, this + " ignored Read Operation");
	}

	@Override
	public void handleWriteOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.WARNING, this + " ignored Write Operation");
	}

	@Override
	public void handleConnectOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.WARNING, this + " ignored Connect Operation");
	}
	
	@Override
	public String toString() {
		return "Network Handler " + Thread.currentThread().getId();
	}
}
