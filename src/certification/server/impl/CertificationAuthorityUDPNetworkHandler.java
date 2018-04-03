package certification.server.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import certification.server.CertificationAuthority;
import io.AbstractUDPNetworkHandler;
import protocol.NetworkWriter;
import util.SerializerBuffer;

public class CertificationAuthorityUDPNetworkHandler extends AbstractUDPNetworkHandler implements NetworkWriter {

	private final ExecutorService executor;
	private final Map<SocketAddress, CertificationAuthorityMessageHandler> messageHandlers;
	private final CertificationAuthority certificationAuthority;
	
	public CertificationAuthorityUDPNetworkHandler(DatagramChannel channel, CertificationAuthority certificationAuthority) throws IOException {
		super(channel, SelectionKey.OP_READ, certificationAuthority);
		this.certificationAuthority = certificationAuthority;
		this.executor = Executors.newCachedThreadPool();
		this.messageHandlers = new HashMap<>();
	}
	
	@Override
	protected void register(SocketAddress from, SerializerBuffer serializerBuffer) {
		CertificationAuthorityMessageHandler handler = new CertificationAuthorityMessageHandler(serializerBuffer, certificationAuthority, from);
		executor.execute(handler);
		messageHandlers.put(from, handler);
	}

	@Override
	public int write(SocketAddress address, SerializerBuffer buffer) throws IOException {
		return send(address, buffer);
	}
	
}
