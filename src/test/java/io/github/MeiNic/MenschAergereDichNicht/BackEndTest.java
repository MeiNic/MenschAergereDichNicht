package io.github.MeiNic.MenschAergereDichNicht;

import io.github.MeiNic.MenschAergereDichNicht.figure.FigureState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.swing.*;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class BackEndTest {
    BackEnd backEnd;
    static Random rand;

    @BeforeAll
    static void setUpAll() {
        rand = new  Random();
    }

    @Nested
    class setFinishedFiguresTest{
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenNoFiguresToFinish_whenSetFinishedFigures_thenNoFiguresFinished() {
            backEnd.setFinishedFigures();
            assertAll(IntStream.range(0, 16)
                    .mapToObj(i -> (Executable)(() -> assertFalse(backEnd.figures[i].isFinished())))
                    .toArray(Executable[]::new));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureOnLastHouseField_whenSetFinishedFigures_thenFigureFinished(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setField(playerIndex * 4 + 3, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setInHouse();
            backEnd.setFinishedFigures();
            assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isFinished());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureOnHouseField_whenSetFinishedFigures_thenFigureNotFinished(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setField(playerIndex * 4 + 2, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setInHouse();
            backEnd.setFinishedFigures();
            assertFalse(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isFinished());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenTwoFiguresToFinish_whenSetFinishedFigures_thenTwoFiguresFinished(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setField(playerIndex * 4 + 3, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].setField(playerIndex * 4 + 2, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setInHouse();
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].setInHouse();
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isFinished())//,
                    //() -> assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].isFinished())
            );
        }
    }

    @Nested
    class figureOnFieldTest{
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenNoFigureOnField_whenGetFigureOnField_thenReturnMinusOne() {
            int expected = -1;
            assertEquals(expected, backEnd.figureOnField(0));
        }

        @Test
        void givenFigureOnField_whenGetFigureOnField_thenReturnFigureIndex() {
            backEnd.figures[0].setOnField();
            backEnd.figures[0].setField(0, 0);
            int expected = 0;
            assertEquals(expected, backEnd.figureOnField(0));
        }
    }

    @Nested
    class figureOnHouseField{
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenNoFigureOnHouseField_whenGetFigureOnHouseField_thenReturnMinusOne() {
            setCurrentPlayer(0);
            int expected = -1;
            assertEquals(expected, backEnd.figureOnHouseField(0));
        }

        @Test
        void givenFigureOnHouseField_whenGetFigureOnHouseField_thenReturnFigureIndex() {
            backEnd.figures[0].setInHouse();
            backEnd.figures[0].setField(0, 0);
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(0));
        }

        @Test
        void givenFinishedFigureOnHouseField_whenGetFigureOnHouseField_thenReturnFigureIndex() {
            backEnd.figures[0].setField(0, 0);
            backEnd.figures[0].setFinished();
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(0));
        }
    }

    @Nested
    class baseOfCurrentPlayerIsEmptyTest{
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresInBase_whenCheckIfBaseIsEmpty_thanReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setInBase();
            }
            assertFalse(backEnd.baseOfCurrentPlayerIsEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureInBas_whenCheckIfBaseIsEmpty_thanReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setInBase();
            }
            backEnd.moveOutOfBase(getFirstFigureOfCurrentPlayer());
            assertFalse(backEnd.baseOfCurrentPlayerIsEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresOnField_whenCheckIfBaseIsEmpty_thanReturnTrue(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setOnField();
                backEnd.figures[i].setField(rand.nextInt(40), 0);
            }
            assertTrue(backEnd.baseOfCurrentPlayerIsEmpty());
        }
    }

    @Nested
    class getNumberOfAllowedTriesTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresInBase_whenCalculateTries_thanReturnThree(int playerIndex) {
            setCurrentPlayer(playerIndex);
            int expected = 3;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresOnField_whenCalculateTries_thanReturnOne(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setOnField();
                backEnd.figures[i].setField(rand.nextInt(40), 0);
            }
            int expected = 1;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresFinished_whenCalculateTries_thanReturnThree(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setFinished();
            }
            int expected = 3;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureOnField_whenCalculateTries_thanReturnOne(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = getFirstFigureOfCurrentPlayer(); i < getFigureAfterOfCurrentPlayer(); i++) {
                backEnd.figures[i].setInBase();
            }
            backEnd.figures[playerIndex * 4].setOnField();
            backEnd.figures[playerIndex * 4].setField(rand.nextInt(40), 0);
            int expected = 1;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }
    }

    @Nested
    class moveOutOfBaseTest {
        @BeforeEach
        void setup() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInBase_whenMoveOutOfBase_thenFigureOnStartField(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.moveOutOfBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            int expectedField = playerIndex * 10;
            FigureState expectedState = FigureState.ON_FIELD;
            assertAll(
                    () -> assertEquals(expectedField, backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].getField()),
                    () -> assertEquals(expectedState, backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].getState())
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnStartField_whenMoveOutOfBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            BackEnd.randomNumber = 6;
            backEnd.moveOutOfBase(getFirstFigureOfCurrentPlayer() + 1);backEnd.moveOutOfBase(getFirstFigureOfCurrentPlayer());
            FigureState expectedStateFigureToMove = FigureState.ON_FIELD;
            FigureState expectedStateFigureOnStartField = FigureState.IN_BASE;
            int expectedFieldFigureToMove = playerIndex * 10;
            int exopecetdFieldFigureOnStartField = getFirstFigureOfCurrentPlayer() + 1;
            assertAll(
                    () -> assertEquals(expectedFieldFigureToMove, backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].getField()),
                    () -> assertEquals(expectedStateFigureToMove, backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].getState()),
                    () -> assertEquals(exopecetdFieldFigureOnStartField, backEnd.figures[getFirstFigureOfCurrentPlayer() + 1].getField()),
                    () -> assertEquals(expectedStateFigureOnStartField, backEnd.figures[getFirstFigureOfCurrentPlayer() + 1].getState())
            );
        }
    }

    @Nested
    class setNewCurrentPlayerIfNecessary{
        @BeforeEach
        void setup() {
            backEnd = new BackEnd(new String[]{"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenRandomNumberIsNotSix_whenSetNewCurrentPlayer_thenCurrentPlayerChanged(int playerIndex){
            setCurrentPlayer(playerIndex);
            BackEnd.randomNumber = 5;
            backEnd.setNewCurrentPlayerIfNecessary();
            int expectedPlayerIndex = (playerIndex + 1) % 4;
            assertEquals(expectedPlayerIndex, backEnd.currentPlayerIndex);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenRandomNumberIsSix_whenSetNewCurrentPlayer_thenCurrentPlayerNotChanged(int playerIndex){
            setCurrentPlayer(playerIndex);
            BackEnd.randomNumber = 6;
            backEnd.setNewCurrentPlayerIfNecessary();
            assertEquals(playerIndex, backEnd.currentPlayerIndex);
        }
    }

    //Method for easier setting of the current player
    private void setCurrentPlayer(int playerIndex){
        if (playerIndex >= 0 && playerIndex < 4) {
            backEnd.currentPlayerIndex = playerIndex;
            backEnd.currentPlayer = backEnd.players[playerIndex];

        } else {
            throw new IllegalArgumentException("Invalid player index");
        }

    }

    // Method to get the first figure of the current player
    private int getFirstFigureOfCurrentPlayer() {
        return backEnd.currentPlayerIndex * 4;
    }

    // Method to get the figure after of the current player
    private int getFigureAfterOfCurrentPlayer() {
        return (backEnd.currentPlayerIndex + 1) * 4;
    }
}
