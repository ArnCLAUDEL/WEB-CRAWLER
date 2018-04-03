package session.server.impl.state;

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
import session.message.SessionAck;
import session.message.SessionReply;
import session.message.SessionRequest;
import session.message.SessionStart;
import session.message.SessionStop;
import util.Cheat;

public class ConnectedSessionServerProtocolHandler extends AbstractSessionServerProtocolHandler {
	
	private final SessionManager manager;
	private final CertificationRetriever retriever;
	
	public ConnectedSessionServerProtocolHandler(NetworkWriter writer, SessionManager manager, CertificationRetriever retriever) {
		super(writer);
		this.manager = manager;
		this.retriever = retriever;
	}
	
	@Override
	public void handleSessionRequest(SocketAddress from, SessionRequest request) {
		
		SessionReply reply;
		try {
			Future<Certificate> futureCertificate = retriever.getCertificate(request.getCertificateIdentifier());
			Certificate clientCertificate = futureCertificate.get();
			Certificate explorerCertificate = manager.getSession().getCertificate();
			reply = new SessionReply(request.getId(), explorerCertificate);
		} catch (NoSuchElementException e) {
			reply = new SessionReply(request.getId(), e.getMessage());
		} catch (InterruptedException e1) {
			Cheat.LOGGER.log(Level.WARNING, "Error while retrieving the certificate", e1);
			return;
		} catch (ExecutionException e2) {
			if(e2.getCause() instanceof NoSuchElementException)
				reply = new SessionReply(request.getId(), e2.getMessage());
			else
				return;
		}
		sendSessionReply(from, reply);
	}

	@Override
	public void sendSessionReply(SocketAddress to, SessionReply reply) {
		send(to, reply);
	}

	@Override
	public void handleSessionStart(SocketAddress from, SessionStart start) {
		Future<Certificate> futureCertificate = retriever.getCertificate(start.getCertificateIdentifier());
		try {
			Certificate certificate = futureCertificate.get();
			SessionInfo info = manager.generate(certificate);
			if(manager.createSession(info)) {
				sendSessionAck(from, new SessionAck(start.getId(), info));
				Cheat.LOGGER.log(Level.INFO, "Session created.");
			} else {
				Cheat.LOGGER.log(Level.WARNING, "Session could not be created.");
			}
		} catch (NoSuchElementException e) {
			Cheat.LOGGER.log(Level.WARNING, "Certificate not found, SessionStart ignored.");
		} catch (InterruptedException | ExecutionException e1) {
			Cheat.LOGGER.log(Level.WARNING, "Error while retrieving the certificate", e1);
			return;
		}
	}

	@Override
	public void handleSessionStop(SocketAddress from, SessionStop stop) {
		manager.deleteSession(stop.getSessionIdentifier());
	}

	@Override
	public void sendSessionAck(SocketAddress to, SessionAck ack) {
		send(to, ack);
	}

}
