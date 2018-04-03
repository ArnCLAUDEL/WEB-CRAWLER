package protocol;

import java.util.HashSet;
import java.util.Set;

import process.ProcessUnitReply;
import util.Creator;
import util.SerializerBuffer;

public class Reply extends Message {
	public static final Creator<Reply> CREATOR = Reply::new;
	
	// TODO ?
	private Request request;
	
	private String hostname;
	private String link;
	private Set<String> urls;
	private Set<String> keyWords;
	
	private Reply() {
		super(Flag.REPLY);
	}
	
	public Reply(String hostname, String link, Set<String> urls) {
		this();
		this.hostname = hostname;
		this.link = link;
		this.urls = urls;
	}
	
	public Reply(String hostname,String link,ProcessUnitReply puReply) {
		// TODO Auto-generated constructor stub
		this();
		this.hostname = hostname;
		this.link = link;
		this.urls = puReply.getLinks();
		this.keyWords= puReply.getKeyWords();
	}

	public String getHostname() {
		return hostname;
	}

	public String getLink() {
		return link;
	}
	
	public Set<String> getUrls() {
		return urls;
	}
	
	public Set<String> getKeyWords() {
		return keyWords;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putString(hostname);
		ms.putString(link);
		ms.putInt(urls != null ? urls.size() : 0);
		urls.stream().forEach(ms::putString);
		ms.putInt(keyWords != null ? keyWords.size() : 0);
		keyWords.stream().forEach(ms::putString);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.hostname = ms.getString();
		this.link =ms.getString();
		int size = ms.getInt();
		this.urls = new HashSet<>();
		for(int i = 0; i < size; i++) {
			urls.add(ms.getString());
		}
		size = ms.getInt();
		this.keyWords = new HashSet<>();
		for(int i = 0; i < size; i++) {
			keyWords.add(ms.getString());
		}
	}
	
	@Override
	public String toString() {
		return "Reply";
	}
}
