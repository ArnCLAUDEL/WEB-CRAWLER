package session.explorer.impl.state;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.logging.Level;

import certification.CertificationRetriever;
import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import session.SessionManager;
import session.explorer.SessionExplorer;
import session.explorer.SessionExplorerProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionStart;
import session.message.SessionStop;
import util.Cheat;

public abstract class AbstractSessionExplorerProtocolHandler extends AbstractProtocolHandler implements SessionExplorerProtocolHandler {

	protected final SessionExplorer explorer;
	
	protected Optional<SessionManager> manager;
	protected Optional<CertificationRetriever> retriever;
	
	protected AbstractSessionExplorerProtocolHandler(SessionExplorer explorer) {
		super();
		this.explorer = explorer;
		this.retriever = Optional.empty();
		this.manager = Optional.empty();
	}
	
	protected AbstractSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager) {
		super(writer);
		this.explorer = explorer;
		this.retriever = Optional.of(retriever);
		this.manager = Optional.of(manager);
	}

	@Override
	public void sendSessionStart(SocketAddress to, SessionStart start) {
		Cheat.LOGGER.log(Level.FINEST, start + " ignored.");
	}

	@Override
	public void sendSessionStop(SocketAddress to, SessionStop stop) {
		Cheat.LOGGER.log(Level.FINEST, stop + " ignored.");
	}

	@Override
	public void handleSessionInit(SocketAddress from, SessionInit init) {
		Cheat.LOGGER.log(Level.FINEST, init + " ignored.");
	}

	@Override
	public void sendSessionAck(SocketAddress to, SessionAck ack) {
		Cheat.LOGGER.log(Level.FINEST, ack + " ignored.");
	}

	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		Cheat.LOGGER.log(Level.FINEST, ack + " ignored.");
	}
	
}
