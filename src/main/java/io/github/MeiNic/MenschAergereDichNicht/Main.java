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

import io.github.MeiNic.MenschAergereDichNicht.gui.Landingpage;
import io.github.MeiNic.MenschAergereDichNicht.gui.Theme;
import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;

import javax.swing.*;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();
    
    public static void main(String[] args) {
        LOGGER.info("Starting game.");

        Thread quittingHook = new Thread(() -> LOGGER.info("Quitting game."));
        Runtime.getRuntime().addShutdownHook(quittingHook);

        try {
	    LOGGER.info("Try setting the look and feel of windows.");
	    setLookAndFeel();
	} catch (ClassNotFoundException | InstantiationException
		 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            LOGGER.fatal("Unable to set the look and feel of windows:" + e);
	    System.exit(1);
        }

	LOGGER.info("Instantiating Landingpage.");
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
