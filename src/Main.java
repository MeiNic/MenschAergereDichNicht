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

	UIManager.put("Button.font", Theme.font);
	UIManager.put("CheckBox.font", Theme.font);
	UIManager.put("Label.font", Theme.font);
	UIManager.put("CheckBoxMenuItem.font", Theme.font);
	UIManager.put("Panel.font", Theme.font);
	UIManager.put("TextField.font", Theme.font);

	UIManager.put("Button.foreground", Theme.foregroundColor);
	UIManager.put("CheckBox.foreground", Theme.foregroundColor);
	UIManager.put("Label.foreground", Theme.foregroundColor);
	UIManager.put("CheckBoxMenuItem.foreground", Theme.foregroundColor);
	UIManager.put("Panel.foreground", Theme.foregroundColor);
	UIManager.put("TextField.foreground", Theme.foregroundColor);

	UIManager.put("Button.background", Theme.backgroundColor);
	UIManager.put("CheckBox.background", Theme.backgroundColor);
	UIManager.put("Label.background", Theme.backgroundColor);
	UIManager.put("CheckBoxMenuItem.background", Theme.backgroundColor);
	UIManager.put("Panel.background", Theme.backgroundColor);
	UIManager.put("TextField.background", Theme.backgroundColor);
    }
}
