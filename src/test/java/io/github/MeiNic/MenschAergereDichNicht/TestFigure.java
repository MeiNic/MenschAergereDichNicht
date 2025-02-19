package io.github.MeiNic.MenschAergereDichNicht;

import org.junit.jupiter.api.*;
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
