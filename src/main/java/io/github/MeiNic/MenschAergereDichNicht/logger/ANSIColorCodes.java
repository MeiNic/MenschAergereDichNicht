// This file is part of MenschAergereDichNicht.
// Copyright (C) 2023-2025 MeiNic, TastingComb and contributors.

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

package io.github.MeiNic.MenschAergereDichNicht.logger;

public enum ANSIColorCodes {
    RESET("\u001B[0m"),
    BLUE("\u001B[34m"),
    FOREGROUND("\u001B[39m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    RED("\u001B[31m");

    private final String code;

    ANSIColorCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
