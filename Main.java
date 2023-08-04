import javax.swing.*;

public class Main {
    public static void main(String[] args) {
	Logger logger = ConsoleLogger.getInstance();
	logger.debug("Test! 1, 2, 3, Test!");
	logger.info("Hello World!");
	logger.warn("Not su good...");
	logger.error("What was that?");
	logger.fatal("Aaaaaargghhhh!");
	
        try {
            // Set the look and feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            new Landingpage();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        
        }
    }
}
