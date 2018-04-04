package certification;

import java.net.InetSocketAddress;
import java.util.Date;

import io.MySerializable;
import util.Creator;
import util.SerializerBuffer;

public class Certificate implements MySerializable {
	public final static Creator<Certificate> CREATOR = Certificate::new;
	public final static byte AGENT = 1;
	public final static byte EXPLORER = 2;
	public final static byte WEB_SERVER = 3;
	
	private long id;
	private String name;
	private byte type;
	private Date date;
	private String hostname;
	private int port;
	
	private Certificate() {}
	
	public Certificate(long id, String name, byte type, Date date, String hostname, int port) {
		checkType(type);
		this.id = id;
		this.name = name;
		this.type = type;
		this.date = date;
		this.hostname = hostname;
		this.port = port;
	}
	
	private void checkType(byte type) throws IllegalArgumentException {
		switch(type) {
			case AGENT:
			case EXPLORER:
			case WEB_SERVER: break;
			default: throw new IllegalArgumentException("Unknown type : " + type);
		}
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public byte getType() {
		return type;
	}
	
	public Date getDate() {
		return date;
	}
	
	public CertificateIdentifier getIdentifier() {
		 return new CertificateIdentifier(id, type);
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return port;
	}
	
	public InetSocketAddress getAddress() {
		return new InetSocketAddress(hostname, port);
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		ms.putString(name);
		ms.put(type);
		ms.putLong(date.getTime());
		ms.putString(hostname);
		ms.putInt(port);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		name = ms.getString();
		type = ms.get();
		date = new Date(ms.getLong());
		hostname = ms.getString();
		port = ms.getInt();
	}
	
}