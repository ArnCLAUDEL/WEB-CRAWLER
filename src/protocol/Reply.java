package protocol;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import io.Creator;
import io.SerializerBuffer;
import util.Cheat;

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
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(hostname);
		ms.putInt(urls.size());
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

}
