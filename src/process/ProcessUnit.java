package process;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Cheat;

public class ProcessUnit implements Callable<Set<String>>{
	private final static String HREF_REGEX = "href=\"(/[^\"]*)";
	private final static Pattern pattern = Pattern.compile(HREF_REGEX);
	private final static String AMP_ESCAPED = "&amp;";
	private final static String AMP_ESCAPED_REPLACEMENT = "";
	
	private final String hostname;
	private final Set<String> links;
	
	public ProcessUnit(String hostname) {
		this.hostname = hostname;
		this.links = new TreeSet<>();
	}
	
	private URLConnection connect(String hostname) throws IOException {
		URL url = new URL(hostname);
		URLConnection uc = url.openConnection();
		return uc;
	}
	
	private String format(String s) {
		return s.replaceAll(AMP_ESCAPED, AMP_ESCAPED_REPLACEMENT);
	}
	
	private Set<String> getContent(InputStream in) {
		Scanner scanner = new Scanner(in);
		String line, urlMatched;
		Matcher m;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			m = pattern.matcher(line);
			while(m.find()) {
				urlMatched = format(m.group(1));
				Cheat.LOGGER.log(Level.FINEST, "URL found: " + urlMatched);
				links.add(urlMatched);
			}
		}
		scanner.close();
		return links;
	}
	
	@Override
	public Set<String> call() throws IOException {
		Cheat.LOGGER.log(Level.FINE, "Unit " + Thread.currentThread().getId() + " is starting");
		URLConnection uc = connect(hostname);
		Set<String> links = getContent(uc.getInputStream());
		Cheat.LOGGER.log(Level.FINE, "Unit " + Thread.currentThread().getId() + " has found " + links.size() + " links from url: " + hostname);
		return links;
	}
	
}
