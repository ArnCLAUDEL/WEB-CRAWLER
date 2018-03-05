package io;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import util.Cheat;

public abstract class AbstractNetworkHandler extends AbstractHandler implements NetworkHandler {
	private final Map<SelectableChannel, Integer> channelOps = new HashMap<>();
	private final Selector selector;
	private final IOEntity ioEntity;
	
	private boolean stop;
	
	public AbstractNetworkHandler(SelectableChannel channel, int op, IOEntity ioEntity) throws IOException {
		super();
		this.selector = Selector.open();
		this.ioEntity = ioEntity;
		addChannel(channel, op);
		this.stop = false;
	}
	
	protected void addChannel(SelectableChannel channel, int op) throws IOException {
		channel.configureBlocking(false);
		channel.register(selector, op);
		channelOps.put(channel, op);
	}
	
	@Override
	protected boolean stop() {
		return !ioEntity.isActive() || stop;
	}
	
	@Override
	public void shutdown() {
		stop = true;
	}
	
	private boolean checkOps(SelectionKey sk, int op) {
		return (sk.readyOps() & op & channelOps.get(sk.channel())) != 0;
	}
	
	@Override
	public void handle() {
		try {
			Iterator<SelectionKey> itr;
			SelectionKey sk;
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
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleReadOperation(SelectionKey sk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleWriteOperation(SelectionKey sk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleConnectOperation(SelectionKey sk) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "Network Handler " + Thread.currentThread().getId();
	}
}
