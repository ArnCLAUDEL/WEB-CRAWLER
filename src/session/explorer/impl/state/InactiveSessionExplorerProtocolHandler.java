package session.explorer.impl.state;

import java.net.SocketAddress;

import certification.CertificationRetriever;
import protocol.NetworkWriter;
import session.SessionManager;
import session.explorer.SessionExplorer;
import session.message.SessionStart;

public class InactiveSessionExplorerProtocolHandler extends AbstractSessionExplorerProtocolHandler {
	
	public InactiveSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager) {
		super(writer, explorer, retriever, manager);
	}
	
	@Override
	public void sendSessionStart(SocketAddress to, SessionStart start) {
		explorer.setProtocolHandler(new InactiveStartSentSessionExplorerProtocolHandler(networkWriter.get(), explorer, retriever.get(), manager.get(), start, to));
		send(to, start);
	}
	
}