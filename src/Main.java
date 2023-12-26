package src;

import javax.swing.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLoggerInstance();
    private static final Theme theme = new Theme();
    
    public static void main(String[] args) {
        logger.info("Starting game.");

        Thread quittingHook = new Thread(() -> logger.info("Quitting game."));
        Runtime.getRuntime().addShutdownHook(quittingHook);

        try {
	    logger.info("Try setting the look and feel of windows.");
	    setLookAndFeel();
	} catch (ClassNotFoundException | InstantiationException
		 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.fatal("Unable to set the look and feel of windows:" + e);
	    System.exit(1);
        }

	logger.info("Instantiating Landingpage.");
	new Landingpage();
    }

    private static void setLookAndFeel() throws ClassNotFoundException,
						InstantiationException,
						IllegalAccessException,
						UnsupportedLookAndFeelException {
	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

	UIManager.put("Button.font", theme.font);
	UIManager.put("CheckBox.font", theme.font);
	UIManager.put("Label.font", theme.font);
	UIManager.put("CheckBoxMenuItem.font", theme.font);
	UIManager.put("Panel.font", theme.font);
	UIManager.put("TextField.font", theme.font);
    }
}
