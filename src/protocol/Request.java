package protocol;

import io.Creator;
import io.SerializerBuffer;
import server.Link;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	private String hostname;
	private String link;
	
	public Request(String completPath) {
		super(Flag.REQUEST);
		
		Link link = parse(completPath);

		System.out.println(completPath);
		this.hostname = link.getHostname();
		this.link= link.getLink();
	}

	public Request() {
		this("example.com/");
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public Link parse(String completPath){
		System.out.println(completPath);
		String hostname = completPath.substring(0, completPath.indexOf("/"));
		String path = completPath.substring(completPath.indexOf("/"),completPath.length() );
		return new Link(hostname,path);
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
