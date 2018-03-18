package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	
	public Explorer(Server server, String hostname) {
		this.server = server;
		this.hostname = hostname;
		this.links = new HashMap<>();
		this.links.put(hostname, STATE.NOT_EXPLORED);
	}
	
	public synchronized void processReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Processing reply.");
		links.put(reply.getHostname(), STATE.EXPLORED);
		reply	.getUrls().stream()
				.map( url -> hostname.concat(url))
				.filter(url -> links.getOrDefault(url, STATE.NOT_EXPLORED) == STATE.NOT_EXPLORED)
				.forEach(url -> links.put(url, STATE.NOT_EXPLORED));
		sendRequests();
		Cheat.LOGGER.log(Level.FINER, "Reply processed.");
		System.out.println();
		long count = links.entrySet().stream()
				.filter(e -> e.getValue() == STATE.EXPLORED)
				.map(Map.Entry::getKey)
				.count();
		System.out.println(count + " url explored");
	}
	
	public synchronized void sendRequests() {
		Cheat.LOGGER.log(Level.FINER, "Preparing requests.");
		links	.entrySet().stream()
				.filter((e) -> e.getValue() == STATE.NOT_EXPLORED)
				.map(Map.Entry::getKey)
				.map(Request::new)
				.collect(Collectors.groupingBy(server::sendRequest))
				.getOrDefault(Boolean.TRUE, new ArrayList<>())
				.stream()
				.map(Request::getHostname)
				.forEach(url -> links.put(url, STATE.EXPLORING));
		Cheat.LOGGER.log(Level.FINER, "Requests sent.");
	}
}
