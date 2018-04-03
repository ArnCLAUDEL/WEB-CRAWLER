package certification;

import java.net.InetSocketAddress;

import io.MySerializable;
import util.Creator;
import util.SerializerBuffer;

public class CertificateRequest implements MySerializable {
	public final static Creator<CertificateRequest> CREATOR = CertificateRequest::new;
	
	private String name;
	private byte type;
	private String hostname;
	private int port;
	
	private CertificateRequest() {}
	
	public CertificateRequest(String name, byte type, String hostname, int port) {
		this.name = name;
		this.type = type;
		this.hostname = hostname;
		this.port = port;
	}
	
	public CertificateRequest(String name, byte type, InetSocketAddress local) {
		this(name, type, local.getHostName(), local.getPort());
	}
	
	public String getName() {
		return name;
	}

	public byte getType() {
		return type;
	}
	
	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(name);
		ms.put(type);
		ms.putString(hostname);
		ms.putInt(port);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		name = ms.getString();
		type = ms.get();
		hostname = ms.getString();
		port = ms.getInt();
	}

}
