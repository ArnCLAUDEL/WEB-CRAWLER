package app.service.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
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
import io.AbstractKeyboardHandler;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import service.server.Explorer;
import service.server.ServiceServerProtocolHandler;
import service.server.impl.state.ActiveServiceServerProtocolHandler;
import service.server.impl.state.InactiveServiceServerProtocolHandler;
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
import util.SerializerBuffer;

public class SimpleExplorerServer extends AbstractIOEntity implements ExplorerServer {

	private final InetSocketAddress local;
	private final InetSocketAddress serverAddress;
	private final SessionManager sessionManager;
	
	private Explorer explorer;
	private CertificationClientProtocolHandler certificationProtocolHandler;
	private SessionExplorerProtocolHandler sessionProtocolHandler;
	private ServiceServerProtocolHandler serviceProtocolHandler;
	private ExplorerServerUDPNetworkHandler udpNetworkHandler;
	private ExplorerServerTCPNetworkHandler tcpNetworkHandler;
	private boolean active;

	public SimpleExplorerServer(String name, InetSocketAddress local, InetSocketAddress serverAddress) {
		super(name);
		this.local = local;
		this.serverAddress = serverAddress;
		this.sessionManager = new SimpleSessionManager();
		this.certificationProtocolHandler = new NotConnectedCertificationClientProtocolHandler();
		this.sessionProtocolHandler = new NotConnectedSessionExplorerProtocolHandler(this);
		this.serviceProtocolHandler = new InactiveServiceServerProtocolHandler(this);
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
		udpNetworkHandler = new ExplorerServerUDPNetworkHandler(channel, this);
		addHandler(udpNetworkHandler);
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(local);
		tcpNetworkHandler = new ExplorerServerTCPNetworkHandler(serverSocketChannel, this);
		addHandler(tcpNetworkHandler);
		
		this.certificationProtocolHandler = new ConnectedCertificationClientProtocolHandler(udpNetworkHandler);
		this.sessionProtocolHandler = new InactiveSessionExplorerProtocolHandler(udpNetworkHandler, this, this, this);
		
		this.serviceProtocolHandler = new ActiveServiceServerProtocolHandler(tcpNetworkHandler, this);
		
		final ExplorerServer server = this;
		
		new Thread(() -> {
			try {Thread.sleep(0);}
			catch (InterruptedException e) {}
			server.scan(Cheat.ONISEP_URL);
		}).start();;
		
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

	public SessionInfo getRandomAgent() throws NoSuchElementException {
		return sessionManager.getRandomAgent();
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
	
	public void handleServiceStart(SessionInfo info, ServiceStart startService) {
		serviceProtocolHandler.handleServiceStart(info, startService);
	}

	public void handleServiceStop(SessionInfo info, ServiceStop stopService) {
		serviceProtocolHandler.handleServiceStop(info, stopService);
	}

	public void handleServiceReply(SessionInfo info, ServiceReply reply) {
		serviceProtocolHandler.handleServiceReply(info, reply);
	}

	public void handleServiceDecline(SessionInfo info, ServiceDecline decline) {
		serviceProtocolHandler.handleServiceDecline(info, decline);
	}

	public void sendServiceOk(SessionInfo info, ServiceOk ok) {
		serviceProtocolHandler.sendServiceOk(info, ok);
	}

	public void sendServiceRequest(SessionInfo info, ServiceRequest request) {
		serviceProtocolHandler.sendServiceRequest(info, request);
	}

	@Override
	public void setProtocolHandler(ServiceServerProtocolHandler protocolHandler) {
		this.serviceProtocolHandler = protocolHandler;
	}

	@Override
	public void setClientActivity(SessionInfo info, boolean active) {
		
	}

	@Override
	public SessionInfo getSession(InetSocketAddress address) throws NoSuchElementException {
		return sessionManager.getSession(address);
	}
	
	
	
	public SessionInfo getRandomExplorer() throws NoSuchElementException {
		return sessionManager.getRandomExplorer();
	}

	@Override
	public void scan(String hostname) {
		hostname=hostname.replaceAll("\n", "");
		explorer = new Explorer(this, hostname);
		explorer.sendRequests();
	}

	@Override
	public boolean sendRequest(ServiceRequest request) {
		try {
			serviceProtocolHandler.sendServiceRequest(getRandomAgent(), request);
			return true;
		} catch (NoSuchElementException e) {
			Cheat.LOGGER.log(Level.WARNING, "No client found.", e);
			return false;
		}
	}

	@Override
	public void processReply(ServiceReply reply) {
		explorer.processReply(reply);
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		for(int i = 1; i <= 20; i++)
			new Thread(new SimpleExplorerServer("Explorer " +i, new InetSocketAddress("127.0.0.1", 9100+i), new InetSocketAddress("127.0.0.1", 9090))).start();
	}

	
}
