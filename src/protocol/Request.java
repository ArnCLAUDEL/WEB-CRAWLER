package protocol;

import io.Creator;
import io.SerializerBuffer;
import server.Link;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	private String hostname;
	private String link;
	
	public Request(String hostname, String path) {
		this();
		this.hostname = hostname;
		this.link= path;
	}

	private Request() {
		super(Flag.REQUEST);
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getLink() {
		return link;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(hostname);
		ms.putString(link);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.hostname = ms.getString();
		this.link=ms.getString();
	}

	@Override
	public String toString() {
		return "Request";
	}
}
