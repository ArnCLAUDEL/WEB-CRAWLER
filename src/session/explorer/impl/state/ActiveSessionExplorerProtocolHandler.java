package session.explorer.impl.state;

import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.Certificate;
import certification.CertificationRetriever;
import protocol.NetworkWriter;
import session.SessionInfo;
import session.SessionManager;
import session.explorer.SessionExplorer;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionStop;
import util.Cheat;

public class ActiveSessionExplorerProtocolHandler extends AbstractSessionExplorerProtocolHandler {

	public ActiveSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager) {
		super(writer, explorer, retriever, manager);
	}	

	@Override
	public void sendSessionStop(SocketAddress to, SessionStop stop) {
		send(to, stop);
		explorer.setProtocolHandler(new InactiveSessionExplorerProtocolHandler(networkWriter.get(), explorer, retriever.get(), manager.get()));
	}

	@Override
	public void handleSessionInit(SocketAddress from, SessionInit init) {
		if(init.getCertificateIdentifier().getType() != Certificate.AGENT)
			return;
			
		Future<Certificate> futureCertificate = retriever.get().getCertificate(init.getCertificateIdentifier());
		try {
			Certificate certificate = futureCertificate.get();
			if(certificate.getType() != Certificate.AGENT || certificate.getId() != init.getCertificateIdentifier().getId())
				return;
			
			SessionInfo info = new SessionInfo(certificate.getId(), certificate);
			if(manager.get().createSession(info)) {
				sendSessionAck(from, new SessionAck(init.getId(), info));
				Cheat.LOGGER.log(Level.INFO, "Session created.");
			} else {
				Cheat.LOGGER.log(Level.WARNING, "Session could not be created.");
			}
		} catch (NoSuchElementException e) {
			Cheat.LOGGER.log(Level.WARNING, "Certificate not found, SessionStart ignored.");
		} catch (InterruptedException | ExecutionException e1) {
			Cheat.LOGGER.log(Level.WARNING, "Error while retrieving the certificate", e1);
		}		
	}
	
	@Override
	public void sendSessionAck(SocketAddress to, SessionAck ack) {
		send(to, ack);
	}

}
