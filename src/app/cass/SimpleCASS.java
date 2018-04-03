package app.cass;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.Certificate;
import certification.CertificateIdentifier;
import certification.CertificationStorer;
import certification.impl.SimpleCertificationStorer;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import certification.server.CertificationProvider;
import certification.server.CertificationServerProtocolHandler;
import certification.server.impl.SimpleCertificationProvider;
import certification.server.impl.state.ConnectedCertificationServerProtocolHandler;
import certification.server.impl.state.NotConnectedCertificationServerProtocolHandler;
import io.AbstractIOEntity;
import session.SessionManager;
import session.impl.SimpleSessionManager;
import session.message.SessionAck;
import session.message.SessionReply;
import session.message.SessionRequest;
import session.message.SessionStart;
import session.message.SessionStop;
import session.server.SessionServerProtocolHandler;
import session.server.impl.state.ConnectedSessionServerProtocolHandler;
import session.server.impl.state.NotConnectedSessionServerProtocolHandler;
import util.Cheat;

public class SimpleCASS extends AbstractIOEntity implements CASS {

	private final SocketAddress local;
	private final SessionManager sessionManager;
	private final CertificationProvider certificationProvider;
	private final CertificationStorer certificationStorer;
	
	private CertificationServerProtocolHandler certificationProtocolHandler;
	private SessionServerProtocolHandler sessionServerProtocolHandler;
	private CASSUDPNetworkHandler networkHandler;
	
	private boolean active;
	
	public SimpleCASS(String name, SocketAddress local) {
		super(name);
		this.local = local;
		this.sessionManager = new SimpleSessionManager();
		this.certificationProvider = new SimpleCertificationProvider();
		this.certificationStorer = new SimpleCertificationStorer();
		this.certificationProtocolHandler = new NotConnectedCertificationServerProtocolHandler();
		this.sessionServerProtocolHandler = new NotConnectedSessionServerProtocolHandler();
		this.active = false;
	}
	
	@Override
	public Future<Certificate> getCertificate(CertificateIdentifier identifier) {
		try {
			Certificate certificate = certificationStorer.get(identifier);
			return CompletableFuture.completedFuture(certificate);
		} catch (NoSuchElementException e) {
			CompletableFuture<Certificate> error = new CompletableFuture<>();
			error.completeExceptionally(new NoSuchElementException("Certificate not found."));
			return error;
		}
	}
	
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	protected void start() throws IOException {
		active = true;
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(local);
		networkHandler = new CASSUDPNetworkHandler(channel, this);
		certificationProtocolHandler = new ConnectedCertificationServerProtocolHandler(networkHandler, certificationStorer, certificationProvider);
		sessionServerProtocolHandler = new ConnectedSessionServerProtocolHandler(networkHandler, sessionManager, this);
		addHandler(networkHandler);
	}

	public void handleCertificationRequest(SocketAddress from, CertificationRequest request) {
		certificationProtocolHandler.handleCertificationRequest(from, request);
	}

	public void handleCertificationGet(SocketAddress from, CertificationGet get) {
		certificationProtocolHandler.handleCertificationGet(from, get);
	}

	public void sendCertificationReply(SocketAddress to, CertificationReply reply) {
		certificationProtocolHandler.sendCertificationReply(to, reply);
	}

	public void handleSessionRequest(SocketAddress from, SessionRequest request) {
		sessionServerProtocolHandler.handleSessionRequest(from, request);
	}

	public void sendSessionReply(SocketAddress to, SessionReply reply) {
		sessionServerProtocolHandler.sendSessionReply(to, reply);
	}

	public void handleSessionStart(SocketAddress from, SessionStart start) {
		sessionServerProtocolHandler.handleSessionStart(from, start);
	}

	public void handleSessionStop(SocketAddress from, SessionStop stop) {
		sessionServerProtocolHandler.handleSessionStop(from, stop);
	}

	public void sendSessionAck(SocketAddress to, SessionAck ack) {
		sessionServerProtocolHandler.sendSessionAck(to, ack);
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		new Thread(new SimpleCASS("CASS", new InetSocketAddress(9090))).start();
	}

}
