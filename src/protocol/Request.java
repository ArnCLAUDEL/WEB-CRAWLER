package protocol;

import util.Creator;
import util.SerializerBuffer;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	private String hostname;
	
	public Request(String hostname) {
		super(Flag.REQUEST);
		this.hostname = hostname;
	}
	
	public Request() {
		this("www.example.com");
	}
	
	public String getHostname() {
		return hostname;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(hostname);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.hostname = ms.getString();
	}

	@Override
	public String toString() {
		return "Request";
	}
}
