package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import protocol.Reply;
import protocol.Request;
import util.Cheat;

public class Explorer {
	
	
	public static enum STATE { NOT_EXPLORED, EXPLORING, EXPLORED };
	
	private final Server server;
	private final String hostname;
	private final Map<String, STATE> links;
	private Map<String,Set<String>> keyWords;
	
	public Explorer(Server server, String hostname) {
		this.server = server;
		this.hostname= hostname;
		this.links = new HashMap<>();
		this.links.put("/", STATE.NOT_EXPLORED);
	}
	
	public synchronized void processReply(Reply reply) {
		
		Cheat.LOGGER.log(Level.FINER, "Processing reply.");
		links.put(reply.getLink(), STATE.EXPLORED);
		Cheat.writeResult("<p>Link : "+reply.getLink()+"</p><table>");
		reply	.getUrls().stream()
				.filter(url -> links.getOrDefault(url, STATE.NOT_EXPLORED) == STATE.NOT_EXPLORED)
				.forEach(url -> {links.put(url, STATE.NOT_EXPLORED);Cheat.writeResult("<tr><td>"+url+"</td></tr>");});
		Cheat.writeResult("</table>");
		sendRequests();
		Cheat.LOGGER.log(Level.FINER, "Reply processed.");
		System.out.println();
		Map<STATE, List<String>> count = links.entrySet().stream()
				.collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
		
		System.out.println(count.getOrDefault(STATE.NOT_EXPLORED, new ArrayList<>()).size() + " url not explored");
		System.out.println(count.getOrDefault(STATE.EXPLORING, new ArrayList<>()).size() + " url pending");
		System.out.println(count.getOrDefault(STATE.EXPLORED, new ArrayList<>()).size() + " url explored");
		
		
	}
	
	public synchronized void sendRequests() {
		Cheat.LOGGER.log(Level.INFO, "Preparing requests.");
		System.out.println(hostname);
		links	.entrySet().stream()
				.filter((e) -> e.getValue() == STATE.NOT_EXPLORED)
				.map(Map.Entry::getKey)
				.map((key)-> new Request(hostname,key))
				.collect(Collectors.groupingBy(server::sendRequest))
				.getOrDefault(Boolean.TRUE, new ArrayList<>())
				.stream()
				.map(Request::getLink)
				.forEach(url -> links.put(url, STATE.EXPLORING));
		Cheat.LOGGER.log(Level.FINER, "Requests sent.");
	}
}
