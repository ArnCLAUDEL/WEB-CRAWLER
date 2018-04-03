package session.client.impl.state;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.CertificationRetriever;
import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import session.SessionInfo;
import session.client.SessionClient;
import session.client.SessionClientProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;
import util.Cheat;

public abstract class AbstractSessionClientProtocolHandler extends AbstractProtocolHandler implements SessionClientProtocolHandler {
	
	protected final SessionClient client;
	
	protected Optional<CertificationRetriever> retriever;
	
	protected AbstractSessionClientProtocolHandler(SessionClient client) {
		super();
		this.client = client;
		this.retriever = Optional.empty();
	}
	
	protected AbstractSessionClientProtocolHandler(NetworkWriter writer, SessionClient client, CertificationRetriever retriever) {
		super(writer);
		this.client = client;
		this.retriever = Optional.of(retriever);
	}

	@Override
	public Future<SessionInfo> sendSessionRequest(SocketAddress to, SessionRequest request) {
		Cheat.LOGGER.log(Level.FINEST, request + " ignored.");
		return getNotYetConnectedFuture();
	}

	@Override
	public void handleSessionReply(SocketAddress from, SessionReply reply) {
		Cheat.LOGGER.log(Level.FINEST, reply + " ignored.");
	}

	@Override
	public void sendSessionInit(SocketAddress to, SessionInit init) {
		Cheat.LOGGER.log(Level.FINEST, init + " ignored.");
	}

	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		Cheat.LOGGER.log(Level.FINEST, ack + " ignored.");
	}
	
}
