package app.service.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.NoSuchElementException;
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
import process.ProcessExecutor;
import service.client.ServiceClientProtocolHandler;
import service.client.impl.state.InactiveServiceClientProtocolHandler;
import service.client.impl.state.NotConnectedServiceClientProtocolHandler;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import session.SessionInfo;
import session.client.SessionClientProtocolHandler;
import session.client.impl.state.ConnectedSessionClientProtocolHandler;
import session.client.impl.state.NotConnectedSessionClientProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;
import util.Cheat;

public class SimpleClient extends AbstractIOEntity implements Client {

	private final InetSocketAddress local;
	private final InetSocketAddress serverAddress;
	private final ProcessExecutor executor;
	
	private CertificationClientProtocolHandler certificationProtocolHandler;
	private SessionClientProtocolHandler sessionClientProtocolHandler;
	private ServiceClientProtocolHandler serviceClientProtocolHandler;
	private ClientUDPNetworkHandler udpNetworkHandler;
	private ClientTCPNetworkHandler tcpNetworkHandler;
	private boolean active;
	
	public SimpleClient(String name, InetSocketAddress local, InetSocketAddress serverAddress) {
		super(name);
		this.local = local;
		this.serverAddress = serverAddress;
		this.executor = new ProcessExecutor();
		this.certificationProtocolHandler = new NotConnectedCertificationClientProtocolHandler();
		this.sessionClientProtocolHandler = new NotConnectedSessionClientProtocolHandler(this);
		this.serviceClientProtocolHandler = new NotConnectedServiceClientProtocolHandler(this);
		this.active = false;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	protected void start() throws IOException {
		active = true;
		DatagramChannel datagramChannel = DatagramChannel.open();
		datagramChannel.bind(local);
		udpNetworkHandler = new ClientUDPNetworkHandler(datagramChannel, this);
		addHandler(udpNetworkHandler);
		
		certificationProtocolHandler = new ConnectedCertificationClientProtocolHandler(udpNetworkHandler);
		sessionClientProtocolHandler = new ConnectedSessionClientProtocolHandler(udpNetworkHandler, this, this);
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(local);
		tcpNetworkHandler = new ClientTCPNetworkHandler(serverSocketChannel, this);
		addHandler(tcpNetworkHandler);
		
		serviceClientProtocolHandler = new InactiveServiceClientProtocolHandler(tcpNetworkHandler, this);
		
		try {
			Future<Certificate> futureCertificate = requestCertification(new CertificateRequest(getName(), Certificate.AGENT, local));
			
			Certificate myCertificate = futureCertificate.get();
			
			Future<Certificate> futureExplorerCertificate = sendSessionRequest(serverAddress, new SessionRequest(Cheat.getId(), myCertificate.getIdentifier()));
			
			Certificate explorerCertificate = futureExplorerCertificate.get();
			
			Future<SessionInfo> futureInfo = sendSessionInit(explorerCertificate.getAddress(), new SessionInit(Cheat.getId(), getName(), 100, 4, myCertificate.getIdentifier()));
			
			SessionInfo info = futureInfo.get();
			
			tcpNetworkHandler.connect(explorerCertificate.getAddress());
			serviceClientProtocolHandler.sendServiceStart(explorerCertificate.getAddress(), new ServiceStart());
		} catch (NoSuchElementException e) {
			Cheat.LOGGER.log(Level.WARNING, "Session request failed", e);
		} catch (InterruptedException | ExecutionException e1) {
			Cheat.LOGGER.log(Level.WARNING, e1.getMessage(), e1);
		}
	}
	
	@Override
	public void setProtocolHandler(SessionClientProtocolHandler handler) {
		sessionClientProtocolHandler = handler;
	}

	@Override
	public Future<Certificate> requestCertification(CertificateRequest request) {
		return certificationProtocolHandler.sendCertertificationRequest(serverAddress, new CertificationRequest(Cheat.getId(), request));
	}

	@Override
	public Future<Certificate> getCertificate(CertificateIdentifier identifier) {
		return certificationProtocolHandler.sendCertificationGet(serverAddress, new CertificationGet(Cheat.getId(), identifier));
	}

	@Override
	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		return certificationProtocolHandler.sendCertertificationRequest(to, request);
	}

	@Override
	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		return certificationProtocolHandler.sendCertificationGet(to, request);
	}

	@Override
	public void handleCertificationReply(SocketAddress from, CertificationReply reply) {
		certificationProtocolHandler.handleCertificationReply(from, reply);
	}

	@Override
	public Future<Certificate> sendSessionRequest(SocketAddress to, SessionRequest request) {
		return sessionClientProtocolHandler.sendSessionRequest(to, request);
	}

	@Override
	public void handleSessionReply(SocketAddress from, SessionReply reply) {
		sessionClientProtocolHandler.handleSessionReply(from, reply);
	}

	@Override
	public Future<SessionInfo> sendSessionInit(SocketAddress to, SessionInit init) {
		return sessionClientProtocolHandler.sendSessionInit(to, init);
	}

	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		sessionClientProtocolHandler.handleSessionAck(from, ack);
	}
	
	@Override
	public void sendServiceStart(SocketAddress to, ServiceStart startService) {
		serviceClientProtocolHandler.sendServiceStart(to, startService);
	}

	@Override
	public void sendServiceStop(SocketAddress to, ServiceStop stopService) {
		serviceClientProtocolHandler.sendServiceStop(to, stopService);
	}

	@Override
	public void sendServiceReply(SocketAddress to, ServiceReply reply) {
		serviceClientProtocolHandler.sendServiceReply(to, reply);
	}

	@Override
	public void sendServiceDecline(SocketAddress to, ServiceDecline decline) {
		serviceClientProtocolHandler.sendServiceDecline(to, decline);
	}

	@Override
	public void handleServiceRequest(SocketAddress from, ServiceRequest request) {
		serviceClientProtocolHandler.handleServiceRequest(from, request);
	}

	@Override
	public void handleServiceOk(SocketAddress from, ServiceOk ok) {
		serviceClientProtocolHandler.handleServiceOk(from, ok);
	}

	@Override
	public void connect(String hostname, int port) throws IOException {
		tcpNetworkHandler.connect(new InetSocketAddress(hostname, port));
	}

	@Override
	public void scan(String hostname, String link) {
		executor.scan(hostname, link);
	}

	@Override
	public void setProtocolHandler(ServiceClientProtocolHandler protocolHandler) {
		this.serviceClientProtocolHandler = protocolHandler;
	}

	@Override
	public int getNbProcessUnits() {
		return ProcessExecutor.THREAD_CAPACITY;
	}

	@Override
	public int getNbTaskMax() {
		return ProcessExecutor.TASK_CAPACITY;
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		for(int i = 1; i <= 10; i++)
			new Thread(new SimpleClient("Arnaud", new InetSocketAddress("127.0.0.1", 10000 +i), new InetSocketAddress("127.0.0.1", 9090))).start();
	}

}
