package app.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.Certificate;
import certification.CertificateIdentifier;
import certification.CertificateRequest;
import certification.client.CertificationClientProtocolHandler;
import certification.client.impl.state.ConnectedCertificationClientProtocolHandler;
import certification.client.impl.state.NotConnectedCertificationClientProtocolHandler;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import io.AbstractIOEntity;
import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Init;
import service.message.Ok;
import service.message.Reply;
import service.message.Request;
import service.message.StartService;
import service.message.StopService;
import service.server.ClientIdentifier;
import session.SessionIdentifier;
import session.SessionInfo;
import session.SessionManager;
import session.explorer.SessionExplorerProtocolHandler;
import session.explorer.impl.state.InactiveSessionExplorerProtocolHandler;
import session.explorer.impl.state.NotConnectedSessionExplorerProtocolHandler;
import session.impl.SimpleSessionManager;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionStart;
import session.message.SessionStop;
import util.Cheat;

public class SimpleExplorerServer extends AbstractIOEntity implements ExplorerServer {

	private final InetSocketAddress local;
	private final InetSocketAddress serverAddress;
	private final SessionManager sessionManager;
	
	private CertificationClientProtocolHandler certificationProtocolHandler;
	private SessionExplorerProtocolHandler sessionProtocolHandler;
	private ExplorerServerUDPNetworkHandler networkHandler;
	private boolean active;

	public SimpleExplorerServer(String name, InetSocketAddress local, InetSocketAddress serverAddress) {
		super(name);
		this.local = local;
		this.serverAddress = serverAddress;
		this.sessionManager = new SimpleSessionManager();
		this.certificationProtocolHandler = new NotConnectedCertificationClientProtocolHandler();
		this.sessionProtocolHandler = new NotConnectedSessionExplorerProtocolHandler(this);
		this.active = false;
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
		networkHandler = new ExplorerServerUDPNetworkHandler(channel, this);
		addHandler(networkHandler);
		
		this.certificationProtocolHandler = new ConnectedCertificationClientProtocolHandler(networkHandler);
		this.sessionProtocolHandler = new InactiveSessionExplorerProtocolHandler(networkHandler, this, this, this);
		
		CompletableFuture<Void> init = CompletableFuture.supplyAsync(() -> {
										try { return requestCertification(new CertificateRequest(getName(), Certificate.EXPLORER, local)).get(); }
										catch (InterruptedException | ExecutionException e) { throw new NoSuchElementException("Certification request failed."); }
									})
									.thenApply(Certificate::getIdentifier)
									.thenApply((CertificateIdentifier id) -> new SessionStart(Cheat.getId(), id))
									.thenAccept((SessionStart start) -> sessionProtocolHandler.sendSessionStart(serverAddress, start));
		try {
			init.get();
		} catch (InterruptedException | ExecutionException e) {
			Cheat.LOGGER.log(Level.WARNING, "Initialization failed", e);
		}
	}
	
	@Override
	public void setProtocolHandler(SessionExplorerProtocolHandler handler) {
		sessionProtocolHandler = handler;
	}

	@Override
	public Future<Certificate> requestCertification(CertificateRequest request) {
		return certificationProtocolHandler.sendCertertificationRequest(serverAddress, new CertificationRequest(Cheat.getId(), request));
	}

	@Override
	public Future<Certificate> getCertificate(CertificateIdentifier identifier) {
		return certificationProtocolHandler.sendCertificationGet(serverAddress, new CertificationGet(Cheat.getId(), identifier));
	}

	public SessionInfo generate(Certificate certificate) {
		return sessionManager.generate(certificate);
	}

	public boolean createSession(SessionInfo info) {
		return sessionManager.createSession(info);
	}

	public void deleteSession(SessionIdentifier identifier) {
		sessionManager.deleteSession(identifier);
	}

	public SessionInfo getSession(SessionIdentifier identifier) throws NoSuchElementException {
		return sessionManager.getSession(identifier);
	}

	public SessionInfo getSession() throws NoSuchElementException {
		return sessionManager.getSession();
	}

	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		return certificationProtocolHandler.sendCertertificationRequest(to, request);
	}

	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		return certificationProtocolHandler.sendCertificationGet(to, request);
	}

	public void handleCertificationReply(SocketAddress from, CertificationReply reply) {
		certificationProtocolHandler.handleCertificationReply(from, reply);
	}

	public void sendSessionStart(SocketAddress to, SessionStart start) {
		sessionProtocolHandler.sendSessionStart(to, start);
	}

	public void sendSessionStop(SocketAddress to, SessionStop stop) {
		sessionProtocolHandler.sendSessionStop(to, stop);
	}

	public void handleSessionInit(SocketAddress from, SessionInit init) {
		sessionProtocolHandler.handleSessionInit(from, init);
	}

	public void sendSessionAck(SocketAddress to, SessionAck ack) {
		sessionProtocolHandler.sendSessionAck(to, ack);
	}

	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		sessionProtocolHandler.handleSessionAck(from, ack);
	}

	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		for(int i = 1; i <= 15; i++)
			new Thread(new SimpleExplorerServer("Explorer " +i, new InetSocketAddress(9100+i), new InetSocketAddress(9090))).start();
	}
	
}
