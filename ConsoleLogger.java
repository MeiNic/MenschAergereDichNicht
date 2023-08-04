import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public final class ConsoleLogger implements Logger {
    private static ConsoleLogger instance;
    private static DateTimeFormatter formatter;
    
    private enum Level {
	DEBUG,
	INFO,
	WARN,
	ERROR,
	FATAL,
    }

    private ConsoleLogger() {
	formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
    }

    public static ConsoleLogger getInstance() {
	if (instance == null) {
	    instance = new ConsoleLogger();
	}
	return instance;
    }

    public void debug(String message) {
    	System.out.println(format(Level.DEBUG, message));
    };

    private String format(Level level, String message) {
	String levelEnclosingStart = "[";
	String levelEnclosingEnd = "] ";
	String messageSeparator = ": ";
	
	if (Level.INFO == level || Level.WARN == level) {
	    // Because info and warn levels are one character shorter
	    // than all other levels, we append a single space to
	    // align all messages properly.
	    levelEnclosingStart = " [";
	}

	return levelEnclosingStart + level.toString() + levelEnclosingEnd
	    + formatter.format(LocalDateTime.now()) + messageSeparator + message;
    };
    
    public void info(String message) {
	System.out.println(format(Level.INFO, message));
    };
    public void warn(String message) {
	System.out.println(format(Level.WARN, message));
    };
    public void error(String message) {
    	System.out.println(format(Level.ERROR, message));
    };
    public void fatal(String message) {
    	System.out.println(format(Level.FATAL, message));
    };
}
