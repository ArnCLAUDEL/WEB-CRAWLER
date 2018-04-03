package app.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
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
import io.AbstractKeyboardHandler;
import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Init;
import service.message.Ok;
import service.message.Reply;
import service.message.Request;
import service.message.StartService;
import service.message.StopService;
import session.SessionInfo;
import session.client.SessionClientProtocolHandler;
import session.client.impl.state.ConnectedSessionClientProtocolHandler;
import session.client.impl.state.NotConnectedSessionClientProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;
import util.Cheat;
import util.SerializerBuffer;

public class SimpleClient extends AbstractIOEntity implements Client {

	private final InetSocketAddress local;
	private final InetSocketAddress serverAddress;
	
	private CertificationClientProtocolHandler certificationProtocolHandler;
	private SessionClientProtocolHandler sessionClientProtocolHandler;
	private ClientUDPNetworkHandler networkHandler;
	private boolean active;
	
	public SimpleClient(String name, InetSocketAddress local, InetSocketAddress serverAddress) {
		super(name);
		this.local = local;
		this.serverAddress = serverAddress;
		this.certificationProtocolHandler = new NotConnectedCertificationClientProtocolHandler();
		this.sessionClientProtocolHandler = new NotConnectedSessionClientProtocolHandler(this);
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
		networkHandler = new ClientUDPNetworkHandler(channel, this);
		addHandler(networkHandler);
		
		certificationProtocolHandler = new ConnectedCertificationClientProtocolHandler(networkHandler);
		sessionClientProtocolHandler = new ConnectedSessionClientProtocolHandler(networkHandler, this, this);
		
		try {
			Future<Certificate> futureCertificate = requestCertification(new CertificateRequest(getName(), Certificate.AGENT, local));
			
			Certificate myCertificate = futureCertificate.get();
			
			Future<Certificate> futureExplorerCertificate = sendSessionRequest(serverAddress, new SessionRequest(Cheat.getId(), myCertificate.getIdentifier()));
			
			Certificate explorerCertificate = futureExplorerCertificate.get();
			
			Future<SessionInfo> futureInfo = sendSessionInit(explorerCertificate.getAddress(), new SessionInit(Cheat.getId(), getName(), 100, 4, myCertificate.getIdentifier()));
			
			SessionInfo info = futureInfo.get();
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

	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		return certificationProtocolHandler.sendCertertificationRequest(to, request);
	}

	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		return certificationProtocolHandler.sendCertificationGet(to, request);
	}

	public void handleCertificationReply(SocketAddress from, CertificationReply reply) {
		certificationProtocolHandler.handleCertificationReply(from, reply);
	}

	public Future<Certificate> sendSessionRequest(SocketAddress to, SessionRequest request) {
		return sessionClientProtocolHandler.sendSessionRequest(to, request);
	}

	public void handleSessionReply(SocketAddress from, SessionReply reply) {
		sessionClientProtocolHandler.handleSessionReply(from, reply);
	}

	public Future<SessionInfo> sendSessionInit(SocketAddress to, SessionInit init) {
		return sessionClientProtocolHandler.sendSessionInit(to, init);
	}

	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		sessionClientProtocolHandler.handleSessionAck(from, ack);
	}

	@Override
	public void sendInit(Init init) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStartService(StartService startService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStopService(StopService stopService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendReply(Reply reply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDecline(Decline decline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendForget(Forget forget) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRequest(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleOk(Ok ok) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleAbort(Abort abort) {
		// TODO Auto-generated method stub
		
	}


	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		for(int i = 1; i <= 10; i++)
			new Thread(new SimpleClient("Arnaud", new InetSocketAddress(10000 +i), new InetSocketAddress(9090))).start();
	}
	
}
