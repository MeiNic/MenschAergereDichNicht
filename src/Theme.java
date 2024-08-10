package src;

import java.io.*;
import java.awt.*;

public interface Theme {
    Color foregroundColor = Color.decode("#f3f5f9");
    Color backgroundColor = Color.decode("#6c6f85");
    Font font = getJetBrainsMonoFont("SemiBold");
    Font fontBolder = getJetBrainsMonoFont("Bold");
    Font fontBold = getJetBrainsMonoFont("ExtraBold");

    static private Font getJetBrainsMonoFont(String thickness) {
        int fontFormat = Font.TRUETYPE_FONT;
        float fontSize = 13f;
        File fontFile = new File("fonts/jetBrainsMono/JetBrainsMono-" + thickness + ".ttf");

        try {
            return Font.createFont(fontFormat, fontFile).deriveFont(fontSize);
        } catch (IOException | FontFormatException e) {
            Logger logger = LoggerFactory.getLoggerInstance();
            logger.fatal("Unable to initialize font: " + e);
            System.exit(1);
        }

        // Will be never reached.  But otherwise freaking Java will
        // complain about a missing return statement...
        return new Font("Default", Font.PLAIN, 13);
    }
}
