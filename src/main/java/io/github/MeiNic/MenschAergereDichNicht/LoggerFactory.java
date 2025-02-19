package io.github.MeiNic.MenschAergereDichNicht;

public class LoggerFactory {
    private LoggerFactory() {}
    
    public static Logger getLoggerInstance() {
        return ConsoleLogger.getInstance(Level.DEBUG);
    }
}
