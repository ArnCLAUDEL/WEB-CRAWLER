package protocol;

import java.util.HashSet;
import java.util.Set;

import io.Creator;
import io.SerializerBuffer;
import server.Link;

public class Reply extends Message {
	public static final Creator<Reply> CREATOR = Reply::new;
	
	// TODO ?
	private Request request;
	
	private String hostname;
	private Set<String> urls;
	
	private Reply() {
		super(Flag.REPLY);
	}
	
	public Reply(String hostname, Set<String> urls) {
		this();
		this.hostname = hostname;
		this.urls = urls;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public Set<String> getUrls() {
		return urls;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(hostname);
		ms.putInt(urls != null ? urls.size() : 0);
		urls.stream().forEach(ms::putString);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.hostname = ms.getString();
		int size = ms.getInt();
		this.urls = new HashSet<>();
		for(int i = 0; i < size; i++) {
			urls.add(ms.getString());
		}
	}
	
	@Override
	public String toString() {
		return "Reply";
	}
}
