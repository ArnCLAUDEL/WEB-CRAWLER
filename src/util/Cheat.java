package util;

import java.nio.charset.Charset;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cheat {
	public final static Charset CHARSET = Charset.forName("UTF-8");
	public final static Logger LOGGER = Logger.getLogger("WEB-CRAWLER");
	
	public static void setLoggerLevelDisplay(Level level) {
		setLoggerLevelDisplay(LOGGER, level);
	}
	
	private static void setLoggerLevelDisplay(Logger logger, Level level) {
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(level);
		logger.addHandler(consoleHandler);
		logger.setLevel(level);
		logger.setUseParentHandlers(false);
	}
}
