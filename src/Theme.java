package src;

import java.io.*;
import java.awt.*;

public class Theme {
    public static final Color foregroundColor = Color.decode("#f3f5f9");
    public static final Color backgroundColor = Color.decode("#6c6f85");
    public static final Font font = getJetBrainsMonoFont();

    static private Font getJetBrainsMonoFont() {
	int fontFormat = Font.TRUETYPE_FONT;
	float fontSize = 13f;
	File fontFile = new File("fonts/jetBrainsMono/JetBrainsMono-SemiBold.ttf");
	
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
