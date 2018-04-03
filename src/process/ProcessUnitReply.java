package process;

import java.util.Set;

public class ProcessUnitReply{

	private Set<String> links;
	private Set<String> keyWords;
	
	public ProcessUnitReply(Set<String> links,Set<String> keyWords) {
		this.links=links;
		this.keyWords=keyWords;
	}
	
	public Set<String> getLinks() {
		return links;
	}
	public Set<String> getKeyWords() {
		return keyWords;
	}
}