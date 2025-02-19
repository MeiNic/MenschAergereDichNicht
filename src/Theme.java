package src;

import java.io.*;
import java.awt.*;

public interface Theme {
    Color FOREGROUND_COLOR = Color.decode("#f3f5f9");
    Color BACKGROUND_COLOR = Color.decode("#6c6f85");
    Font SEMI_BOLD = getJetBrainsMonoFont("SemiBold");
    Font BOLD = getJetBrainsMonoFont("Bold");
    Font EXTRA_BOLD = getJetBrainsMonoFont("ExtraBold");

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

        // Will never be reached.  But otherwise freaking Java will
        // complain about a missing return statement...
        return new Font("Default", Font.PLAIN, 13);
    }
}
