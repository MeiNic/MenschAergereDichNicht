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

class Human implements Player {
    private final String name;
    private final int playerIndex;

    public Human(String name, int playerIndex) {
	this.name = name;
	this.playerIndex = playerIndex;
    }

    public String getName() {
	return name;
    }

    public int getPlayerState() {
	return 0;
    }

    public int getPlayerIndex() {
	return playerIndex;
    }
}
