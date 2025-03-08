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

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public final class ConsoleLogger implements Logger {
    private static ConsoleLogger instance;
    private static DateTimeFormatter formatter;
    private static Level logForLevelEqualOrAbove;

    private ConsoleLogger(Level loggingLevel) {
        formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        logForLevelEqualOrAbove = loggingLevel;
    }

    public static ConsoleLogger getInstance(Level loggingLevel) {
        if (instance == null) {
            instance = new ConsoleLogger(loggingLevel);
        }
        return instance;
    }

    public void debug(String message) {
        if (shouldLog(Level.DEBUG)) {
            System.out.println(ANSIColorCodes.BLUE.getCode()
                    + format(Level.DEBUG, message)
                    + ANSIColorCodes.RESET.getCode());
        }
    }

    private boolean shouldLog(Level level) {
        // Only log messages whose level is equal or above the level
        // specified in the constructor. This allows for example
        // turning off all message below WARN to only print severe
        // messages.
        return logForLevelEqualOrAbove.ordinal() <= level.ordinal();
    }


    private String format(Level level, String message) {
        String levelEnclosingStart = "[";
        String levelEnclosingEnd = "] ";
        String messageSeparator = ": ";

        if (Level.INFO == level || Level.WARN == level) {
            // Because info and warn levels are one character shorter
            // than all other levels, we append a single space at the
            // start for properly aligning all messages.
            levelEnclosingStart = " [";
        }

        return levelEnclosingStart
                + level.toString()
                + levelEnclosingEnd
                + formatter.format(LocalDateTime.now())
                + messageSeparator
                + message;
    }

    public void info(String message) {
        if (shouldLog(Level.INFO)) {
            System.out.println(ANSIColorCodes.WHITE.getCode()
                    + format(Level.INFO, message)
                    + ANSIColorCodes.RESET.getCode());
        }
    }
    public void warn(String message) {
        if (shouldLog(Level.WARN)) {
            System.out.println(ANSIColorCodes.YELLOW.getCode()
                    + format(Level.WARN, message)
                    + ANSIColorCodes.RESET.getCode());
        }
    }
    public void error(String message) {
        if (shouldLog(Level.ERROR)) {
            System.out.println(ANSIColorCodes.PURPLE.getCode()
                    + format(Level.ERROR, message)
                    + ANSIColorCodes.RESET.getCode());
        }
    }
    public void fatal(String message) {
        if (shouldLog(Level.FATAL)) {
            System.out.println(ANSIColorCodes.RED.getCode()
                    + format(Level.FATAL, message)
                    + ANSIColorCodes.RESET.getCode());
        }
    }
}
