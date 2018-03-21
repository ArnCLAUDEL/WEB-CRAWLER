package process;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import HttpRequest.HttpGet;
import io.SerializerBuffer;
import util.Cheat;

public class ProcessUnit implements Callable<Set<String>>{
	
	
	private Set<String> links;
	private final SerializerBuffer sb = new SerializerBuffer() ;
	private final static Pattern patternCode = Pattern.compile("[0-9]{3}");
	private final static Pattern patternLoc = Pattern.compile("Location: (https?)://(.*)");
	private final static String AMP_ESCAPED = "&amp;";
	private final static String ACHOR_ESCAPED = "#";
	private final static String AMP_ESCAPED_REPLACEMENT = "";

	private String hostname;
	private String relativLink;
	private SocketChannel s;
	private int code = 0;
	private boolean doparse=false;
	
	
	public ProcessUnit(String hostname , String relativLink) {
		this.hostname = hostname;
		this.relativLink = relativLink;
		this.links = new TreeSet<>();
	}
	
	@Override
	public Set<String> call() throws IOException {
		do {
			Cheat.LOGGER.log(Level.INFO, "Send create Socket"); createSocket();
			Cheat.LOGGER.log(Level.INFO, "Generate Socket"); generateRequest();
			Cheat.LOGGER.log(Level.INFO, "Read on Socket And GET REP"); socketRead();
		}while(canRedirect());
		if(doparse)
			Cheat.LOGGER.log(Level.INFO, "Parse all links in page"); links.addAll(parse());
		return links;
	}
	
	
	private Set<String> parse() throws IOException {
		String HREF_REGEX = "href=\"(http://"+hostname+")?/([^\"]*)";
		Pattern pattern = Pattern.compile(HREF_REGEX);
		Scanner scanner = new Scanner(s.socket().getInputStream());
		Matcher m;
		String line,urlMatched;
		while(scanner.hasNextLine()) {
			line= scanner.nextLine();
			m = pattern.matcher(line);
			while(m.find()) {
				urlMatched = format(m.group(2));
				Cheat.LOGGER.log(Level.FINEST, "URL found: " + urlMatched);
				links.add(urlMatched);
			}
		}
		scanner.close();
		return links;
	}

	private void createSocket() throws IOException {
		s = SocketChannel.open(new InetSocketAddress(hostname, 80));
	}

	
	private void generateRequest() throws IOException {
		Cheat.LOGGER.log(Level.INFO, "Create HTTP GET");
		createHttpGet();
		sb.flip();
		Cheat.LOGGER.log(Level.INFO, "Write on Serializable Buffer");
		socketWrite();
		
	}
	
	private boolean canRedirect() {
		switch(code) {
		case 200:
			doparse =true;
			return false;
		case 300:
		case 301:
		case 302:
			if(getRdir().contentEquals(hostname)) {
				return false;}
			 hostname=getRdir();
			 return true;
		default:
			return false;
		}
	}
	
	private void createHttpGet() {
		HttpGet hget = new HttpGet(hostname,relativLink);
		hget.writeToBuff(sb);

	}
	
	private void socketWrite() throws IOException {
		s.write(sb.getBuffer());
	}
	
	private int getCode() {
		Scanner s = new Scanner(BuffToString(sb));
		int i =Integer.parseInt(s.findInLine(patternCode));
		s.close();
		return i;
	}
	
	private String getRdir() {
		Scanner scanner = new Scanner(BuffToString(sb));
		String location=scanner.findWithinHorizon(patternLoc, 0);
		if(location.contains("https")) {
			location=hostname;
		}
		else {
			location=location.replaceAll("Location: http://", "");
			location= format(location);
		}
		scanner.close();
		return location;
	}
	
	
	private String BuffToString(SerializerBuffer sb) {
		sb.mark();
		String s = sb.toString();
		sb.reset();
		return s;
	}
	
	private void socketRead() throws IOException {
		sb.clear();
		sb.read(s);
		sb.flip();
		code = getCode();
	}

	public String format(String s) {
		s=s.replaceAll(AMP_ESCAPED, AMP_ESCAPED_REPLACEMENT);
		s=s.replaceAll(ACHOR_ESCAPED, AMP_ESCAPED_REPLACEMENT);
		s=s.replaceAll("/", AMP_ESCAPED_REPLACEMENT);
		return s;
	}

	public static void main(String[] args) throws IOException {
		ProcessUnit pu = new ProcessUnit("www.onisep.fr","/");
		Cheat.LOGGER.log(Level.INFO, "Send create Socket");
		do {
			Cheat.LOGGER.log(Level.INFO, "Send create Socket"); pu.createSocket();
			Cheat.LOGGER.log(Level.INFO, "Generate Socket"); pu.generateRequest();
			Cheat.LOGGER.log(Level.INFO, "Read on Socket And GET REP"); pu.socketRead();
		}while(pu.canRedirect());
		if(pu.doparse) {
			Cheat.LOGGER.log(Level.INFO, "Parse all links in page"); pu.links.addAll(pu.parse());}
			
		System.out.println(pu.links);
	}
	
}
