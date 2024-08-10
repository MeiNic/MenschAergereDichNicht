package src;

import javax.swing.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLoggerInstance();
    
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

	UIManager.put("Button.font", Theme.SEMI_BOLD);
	UIManager.put("CheckBox.font", Theme.SEMI_BOLD);
	UIManager.put("Label.font", Theme.SEMI_BOLD);
	UIManager.put("CheckBoxMenuItem.font", Theme.SEMI_BOLD);
	UIManager.put("Panel.font", Theme.SEMI_BOLD);
	UIManager.put("TextField.font", Theme.SEMI_BOLD);

	UIManager.put("Button.foreground", Theme.FOREGROUND_COLOR);
	UIManager.put("CheckBox.foreground", Theme.FOREGROUND_COLOR);
	UIManager.put("Label.foreground", Theme.FOREGROUND_COLOR);
	UIManager.put("CheckBoxMenuItem.foreground", Theme.FOREGROUND_COLOR);
	UIManager.put("Panel.foreground", Theme.FOREGROUND_COLOR);
	UIManager.put("TextField.foreground", Theme.FOREGROUND_COLOR);

	UIManager.put("Button.background", Theme.BACKGROUND_COLOR);
	UIManager.put("CheckBox.background", Theme.BACKGROUND_COLOR);
	UIManager.put("Label.background", Theme.BACKGROUND_COLOR);
	UIManager.put("CheckBoxMenuItem.background", Theme.BACKGROUND_COLOR);
	UIManager.put("Panel.background", Theme.BACKGROUND_COLOR);
	UIManager.put("TextField.background", Theme.BACKGROUND_COLOR);
    }
}
