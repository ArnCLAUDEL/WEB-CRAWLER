package session.client.impl.state;

import java.net.SocketAddress;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.Certificate;
import session.SessionInfo;
import session.client.SessionClient;
import session.message.SessionInit;
import session.message.SessionRequest;
import util.Cheat;

public class NotConnectedSessionClientProtocolHandler extends AbstractSessionClientProtocolHandler {

	public NotConnectedSessionClientProtocolHandler(SessionClient client) {
		super(client);
	}
	
	@Override
	public Future<SessionInfo> sendSessionInit(SocketAddress to, SessionInit init) {
		Cheat.LOGGER.log(Level.FINEST, init + " ignored.");
		return getNotYetConnectedFuture();
	}


	@Override
	public Future<Certificate> sendSessionRequest(SocketAddress to, SessionRequest request) {
		Cheat.LOGGER.log(Level.FINEST, request + " ignored.");
		return getNotYetConnectedFuture();
	}

}
