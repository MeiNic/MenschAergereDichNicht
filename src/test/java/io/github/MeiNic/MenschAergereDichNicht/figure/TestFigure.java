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

package io.github.MeiNic.MenschAergereDichNicht.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TestFigure {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 37, 38, 39})
    void moveByValueDoesNotWrapField(int value) {
	Figure figure = new Figure(0, 0, "");
	figure.moveByValue(value);
	assertEquals(value, figure.getField());
    }

    @ParameterizedTest
    @ValueSource(ints = {40, 41, 42})
    void moveByValueDoesWrapField(int value) {
	Figure figure = new Figure(0, 0, "");
	figure.moveByValue(value);
	assertEquals(value - 40, figure.getField());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 37, 38, 39, 40, 41, 42})
    void moveByValueDoesNotWrapProgress(int value) {
	Figure figure = new Figure(0, 0, "");
	figure.moveByValue(value);
	assertEquals(value, figure.getProgress());
    }

    // TODO: Maybe support this in the future? - @guemax, 2025-02-19
    // @ParameterizedTest
    // @ValueSource(ints = {-1, -2, -3})
    // void moveByValueDoesNotAcceptNegativeIntegers(int value) {
    // 	Figure figure = new Figure(0, 0, "");
    // 	assertThrows(AssertionError.class, () -> {
    // 		figure.moveByValue(value);
    // 	    });
    // }
}
