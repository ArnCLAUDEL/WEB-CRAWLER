package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cheat {
	public static final String CAPTAIN_OBVIOUS_URL = "https://en.wiktionary.org/wiki/Captain_Obvious";
	public static final String ONISEP_URL = "http://www.onisep.fr";
	public static final String EXAMPLE_URL = "http://www.example.org";
	public static final long DEFAULT_ID = -1;
	public final static Random RANDOM = new Random();
	public final static Charset CHARSET = Charset.forName("UTF-8");
	public final static Logger LOGGER = Logger.getLogger("WEB-CRAWLER");
	public static PrintWriter pw;
	
	public static void setLoggerLevelDisplay(Level level) {
		setLoggerLevelDisplay(LOGGER, level);
	}
	
	public static long getId() {
		return RANDOM.nextInt();
	}
	
	private static void setLoggerLevelDisplay(Logger logger, Level level) {
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(level);
		logger.addHandler(consoleHandler);
		logger.setLevel(level);
		logger.setUseParentHandlers(false);
	}
	
	public static void initPrintW() {
		File f = new File("resultat.html");
		if(f.exists())
			f.delete();
		try {
			pw = new PrintWriter("resultat.html");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.println("<html>\n" + 
				"\n" + 
				"<head>\n" + 
				"<title>Resultat Exploration</title>\n" + 
				"</head>\n" + 
				"\n" + 
				"<body>");
		
	}
	

	
	public static void writeResult(String s) {
		pw.println(s);
	}
}
