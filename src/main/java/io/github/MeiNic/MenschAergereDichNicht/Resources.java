// This file is part of MenschAergereDichNicht.
// Copyright (C) 2024-2025 MeiNic, TastingComb and contributors.

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.MeiNic.MenschAergereDichNicht;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Resources {
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public static ImageIcon loadImageIcon(String name) {
	BufferedImage image = loadBufferedImage(name);
	return new ImageIcon(image);
    }

    public static BufferedImage loadBufferedImage(String name) {
	String imagePath = "images/" + name + ".png";

	InputStream imageStream = Resources.class.getClassLoader()
	    .getResourceAsStream(imagePath);
	if (imageStream == null) {
	    LOGGER.fatal("Unable to find image \"" + imagePath + "\".");
	    System.exit(1);
	}

	BufferedImage image = null;

	try {
	    image = ImageIO.read(imageStream);
	} catch (IOException e) {
	    LOGGER.fatal("Unable to load image \"" + imagePath + "\":"
			 + e.getMessage());
	    System.exit(1);
	}

	return image;
    }

    public static ImageIcon loadScaledImageIcon(String name,
						int width, int height) {
	Image image = loadBufferedImage(name)
	    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
	return new ImageIcon(image);
    }

    public static Font loadFont(String name) {
	String fontPath = "fonts/jetBrainsMono/JetBrainsMono-"
	    + name + ".ttf";

	InputStream fontStream = Resources.class.getClassLoader()
	    .getResourceAsStream(fontPath);
	if (fontStream == null) {
            LOGGER.fatal("Unable to find font \"" + fontPath + "\".");
            System.exit(1);
	}

	Font font = null;

        try {
	    font = Font.createFont(Font.TRUETYPE_FONT, fontStream)
		.deriveFont(13f);
	} catch (IOException | FontFormatException e) {
	    LOGGER.fatal("Unable to load font \"" + fontPath + "\": "
			 + e.getMessage());
	    System.exit(1);
	}

	return font;
    }
}
