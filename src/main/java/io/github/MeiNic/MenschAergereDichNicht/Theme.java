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
        File fontFile = new File("src/resources/fonts/jetBrainsMono/JetBrainsMono-" + thickness + ".ttf");

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
