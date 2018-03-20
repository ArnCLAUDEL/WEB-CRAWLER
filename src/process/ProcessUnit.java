package process;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import HttpRequest.HttpGet;
import io.SerializerBuffer;
import util.Cheat;

public class ProcessUnit implements Callable<Set<String>>{
	
	
	private final Set<String> links;
	private final SerializerBuffer sb = new SerializerBuffer() ;
	
	private String hostname;
	private String relativLink;
	private SocketChannel s;
	
	
	public ProcessUnit(String hostname , String relativLink) {
		this.hostname = hostname;
		this.relativLink = relativLink;
		this.links = new TreeSet<>();
	}
	
	@Override
	public Set<String> call() throws IOException {
		//connect(hostname);
		createSocket();
		generateRequest();
		//HttpRequest OK/RDir = generateRequest(link,hostname,SerializableBuffer): OK -> PARSE | RDIR -> CONNECT
		
		//Set<String> = parse(SerializableBuffer)
		
		return links;
		/*Cheat.LOGGER.log(Level.FINE, "Unit " + Thread.currentThread().getId() + " is starting");
		URLConnection uc = connect(hostname);
		Cheat.LOGGER.log(Level.FINE, "Unit " + Thread.currentThread().getId() + " connected to " + hostname);
		Set<String> links = getContent(uc.getInputStream());
		Cheat.LOGGER.log(Level.FINE, "Unit " + Thread.currentThread().getId() + " has found " + links.size() + " links from url: " + hostname);
		return links;*/
	}
	
	
	private void createSocket() throws IOException {
		s = SocketChannel.open(new InetSocketAddress(hostname, 80));
	}

	
	private void generateRequest() throws IOException {
		Cheat.LOGGER.log(Level.INFO, "Create HTTP GET");
		createHttpGet();
		Cheat.LOGGER.log(Level.INFO, "Write on Serializable Buffer");
		socketWrite();
	}
	
	
	
	private void createHttpGet() {
		HttpGet hget = new HttpGet(hostname,relativLink);
		hget.writeToBuff(sb);
	}
	
	private void socketWrite() throws IOException {
		s.write(sb.getBuffer());
	}
	
	private void socketRead() throws IOException {
		sb.clear();
		sb.read(s);
		sb.flip();
		System.out.println(Cheat.CHARSET.decode(sb.getBuffer()));
	}
	


	public static void main(String[] args) throws IOException {
		ProcessUnit pu = new ProcessUnit("www.onisep.fr","/");
		Cheat.LOGGER.log(Level.INFO, "Send create Socket");
		pu.createSocket();
		Cheat.LOGGER.log(Level.INFO, "Generate Socket");
		pu.generateRequest();
		Cheat.LOGGER.log(Level.INFO, "Read on Socket");
		pu.socketRead();

	}
	
}
