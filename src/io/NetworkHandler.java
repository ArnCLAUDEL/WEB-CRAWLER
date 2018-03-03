package io;

import java.nio.channels.SelectionKey;

public interface NetworkHandler {
	public void handleAcceptOperation(SelectionKey sk);
	public void handleReadOperation(SelectionKey sk);
	public void handleWriteOperation(SelectionKey sk);
	public void handleConnectOperation(SelectionKey sk);
}
