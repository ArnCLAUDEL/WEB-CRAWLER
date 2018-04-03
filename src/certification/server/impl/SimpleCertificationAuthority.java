package certification.server.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Optional;

import certification.CertificationStorer;
import certification.impl.SimpleCertificationStorer;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import certification.server.CertificationAuthority;
import certification.server.CertificationProvider;
import certification.server.CertificationServerProtocolHandler;
import certification.server.impl.state.ConnectedCertificationServerProtocolHandler;
import certification.server.impl.state.NotConnectedCertificationServerProtocolHandler;
import io.AbstractIOEntity;

public class SimpleCertificationAuthority extends AbstractIOEntity implements CertificationAuthority {

	private final SocketAddress localAddress;
	private final CertificationProvider provider;
	private final CertificationStorer storer;
	
	private CertificationServerProtocolHandler certificationProtocolHandler;
	private Optional<CertificationAuthorityUDPNetworkHandler> udpNetworkHandler;
	
	private boolean active;
	
	public SimpleCertificationAuthority(SocketAddress localAddress) {
		super();
		this.localAddress = localAddress;
		this.provider = new SimpleCertificationProvider();
		this.storer = new SimpleCertificationStorer();
		this.certificationProtocolHandler = new NotConnectedCertificationServerProtocolHandler();
		this.udpNetworkHandler = Optional.empty();
	}
	
	
	@Override
	public void handleCertificationGet(SocketAddress from, CertificationGet request) {
		certificationProtocolHandler.handleCertificationGet(from, request);
	}

	@Override
	public void handleCertificationRequest(SocketAddress from, CertificationRequest request) {
		certificationProtocolHandler.handleCertificationRequest(from, request);
	}

	@Override
	public void sendCertificationReply(SocketAddress to, CertificationReply reply) {
		certificationProtocolHandler.sendCertificationReply(to, reply);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	protected void start() throws IOException {
		active = true;
		DatagramChannel datagramChannel = DatagramChannel.open();
		datagramChannel.bind(localAddress);
		udpNetworkHandler = Optional.of(new CertificationAuthorityUDPNetworkHandler(datagramChannel, this));
		addHandler(udpNetworkHandler.get());
		certificationProtocolHandler = new ConnectedCertificationServerProtocolHandler(udpNetworkHandler.get(), storer, provider);
	}
	
	public static void main(String[] args) {
		CertificationAuthority ca = new SimpleCertificationAuthority(new InetSocketAddress(8090));
		new Thread(ca).start();
	}

}
