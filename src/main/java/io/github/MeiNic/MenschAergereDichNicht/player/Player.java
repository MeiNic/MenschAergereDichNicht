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

package io.github.MeiNic.MenschAergereDichNicht.player;

public interface Player {
    String getName();

    int getPlayerState();

    int getPlayerIndex();

    default int getIndexOfFirstFigure() {
        return 4 * getPlayerIndex();
    }

    default int getIndexOfLastFigure() {
        return 4 + getIndexOfFirstFigure();
    }

    default int getIndexOfStartField() {
        return 10 * getPlayerIndex();
    }
}
