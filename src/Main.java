package src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLoggerInstance();
        logger.info("Starting game.");

        Thread quittingHook = new Thread(() -> logger.info("Quitting game."));
        Runtime.getRuntime().addShutdownHook(quittingHook);
        String[] names = {"yellow", "green", "blue", "red"};

        try {
            logger.info("Try setting look and feel of windows.");
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            logger.info("Instantiating gameboard.");
            new GameBoardGui(names, 4, false);
        } catch (UnsupportedLookAndFeelException e) {
            logger.fatal("Unable to set the look and feel of windows.");
            e.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.fatal("An unexpected error occured.");
            e.printStackTrace();
        }
    }
}
