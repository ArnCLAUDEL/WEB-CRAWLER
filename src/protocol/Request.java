package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	private String hostname;
	private String link;
	
	public Request(String hostname,String link) {
		super(Flag.REQUEST);
		this.hostname = hostname;
		this.link=link;
	}
	
	public Request() {
		this("www.example.com","");
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
