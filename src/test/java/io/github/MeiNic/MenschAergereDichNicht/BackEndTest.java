package io.github.MeiNic.MenschAergereDichNicht;

import static org.junit.jupiter.api.Assertions.*;

import io.github.MeiNic.MenschAergereDichNicht.figure.Figure;
import io.github.MeiNic.MenschAergereDichNicht.figure.FigureState;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BackEndTest {
    BackEnd backEnd;
    static Random rand;

    @BeforeAll
    static void setUpAll() {
        rand = new Random();
    }

    @Nested
    class botMoveTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, true);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenPlayerOnStartfield_whenBotMove_thenFigureMovedToStartField(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 6;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10, 0);
            backEnd.botMove();
            assertEquals(
                    playerIndex * 10 + backEnd.randomNumber,
                    backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnStartfieldAndEmptyBase_whenBotMove_thenFigureNotMoved(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 6;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10, 0);
            backEnd.figures[testFigureIndex + 1].setInHouse();
            backEnd.figures[testFigureIndex + 2].setInHouse();
            backEnd.figures[testFigureIndex + 3].setOnField();
            backEnd.figures[testFigureIndex + 3].setField(playerIndex * 10 + 7, 7);
            backEnd.botMove();
            assertEquals(playerIndex * 10, backEnd.figures[testFigureIndex].getField());
        }
    }

    @Nested
    class moveOnFieldTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnField_whenMoveFigureOnField_thenFigureMoved(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10, 0);
            final int expectedField =
                    backEnd.figures[testFigureIndex].getField() + backEnd.randomNumber;
            backEnd.moveOnField(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnMaxField_whenMoveFigureOnField_thenFigureMovedOverBoarder(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(39, 0);
            final int expectedField = backEnd.randomNumber - 1;
            backEnd.moveOnField(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureBeatOtherFigure_whenMoveFigureOnField_thenFigureMovedAndOtherFigureInBase(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10, 0);
            backEnd.figures[(testFigureIndex + 4) % 16].setOnField();
            backEnd.figures[(testFigureIndex + 4) % 16].setField(
                    playerIndex * 10 + backEnd.randomNumber, 0);
            final int expectedField = playerIndex * 10 + backEnd.randomNumber;
            backEnd.moveOnField(testFigureIndex);
            assertAll(
                    () -> assertEquals(expectedField, backEnd.figures[testFigureIndex].getField()),
                    () -> assertTrue(backEnd.figures[(testFigureIndex + 4) % 16].isInBase()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void
                givenFigureOnFieldWouldBeatOwnFigure_whenMoveFigureOnField_thenFigureNotMovedOtherFigureMoved(
                        int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedFieldFirstFigure = 0;
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(expectedFieldFirstFigure, 0);
            backEnd.figures[testFigureIndex + 1].setOnField();
            backEnd.figures[testFigureIndex + 1].setField(backEnd.randomNumber, 0);
            final int expectedFieldSecondFigure = backEnd.randomNumber * 2;
            backEnd.moveOnField(testFigureIndex);
            assertAll(
                    () ->
                            assertEquals(
                                    expectedFieldFirstFigure,
                                    backEnd.figures[testFigureIndex].getField()),
                    () ->
                            assertEquals(
                                    expectedFieldSecondFigure,
                                    backEnd.figures[testFigureIndex + 1].getField()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnFieldMovetoFreeBase_whenMoveFigureOnField_thenFigureMovedToBase(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(4) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedField = playerIndex * 4 + backEnd.randomNumber - 1;
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField((playerIndex * 10 + 39) % 40, 39);
            backEnd.moveOnField(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnFieldNotAbleToMoveInHouse_whenMoveFigureOnField_thenFigureNotMoved(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(4, 6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedField = (playerIndex * 10 + 39) % 40;
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(expectedField, 39);
            backEnd.figures[testFigureIndex + 1].setInHouse();
            backEnd.figures[testFigureIndex + 1].setField(playerIndex * 4, 0);
            backEnd.moveOnField(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnFieldWouldExceedMaxHouseField_whenMoveFigureOnField_thenFigureNotMoved(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(4, 6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedField = (playerIndex * 10 + 39) % 40;
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(expectedField, 39);
            backEnd.moveOnField(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }
    }

    @Nested
    class moveInHouseTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnMaxHosueField_whenMoveFigureInHouse_thenNotMoveFigure(int playerIndex) {
            setCurrentPlayer(playerIndex);
            final int expectedField = playerIndex * 4 + 3;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setInHouse();
            backEnd.figures[testFigureIndex].setField(expectedField, 0);
            backEnd.moveInHouse(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigurInHouseAndMovable_whenMoveFigureInHouse_thenFigureMoved(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(0, 3) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedField = playerIndex * 4 + backEnd.randomNumber;
            backEnd.figures[testFigureIndex].setInHouse();
            backEnd.figures[testFigureIndex].setField(playerIndex * 4, 0);
            backEnd.moveInHouse(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnFieldNotMovable_whenMoveFigureInHouse_thenNotMoveFigure(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = rand.nextInt(4, 6) + 1;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            final int expectedField = playerIndex * 4 + 2;
            backEnd.figures[testFigureIndex].setInHouse();
            backEnd.figures[testFigureIndex].setField(expectedField, 0);
            backEnd.moveInHouse(testFigureIndex);
            assertEquals(expectedField, backEnd.figures[testFigureIndex].getField());
        }
    }

    @Nested
    class beatIsPossibleTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureIsInBase(int randomNumber) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInBase();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureIsInHouse(int randomNumber) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInHouse();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureIsFinished(int randomNumber) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setFinished();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1",
            "5,2", "5,3", "6,0", "6,1", "6,2", "6,3",
        })
        void noBeatIsPossibleIfFigureIsAboutToEnterTheHouse(int randomNumber, int offset) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, 0 + offset);
            placeFigureOnField(currentFigure, 40 - randomNumber + offset);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1",
            "5,2", "5,3", "5,4", "6,0", "6,1", "6,2", "6,3", "6,4", "6,5",
        })
        void noBeatIsPossibleIfFigureCannotReachToOtherFigure(int randomNumber, int offset) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, randomNumber + 1);
            placeFigureOnField(currentFigure, 0);
            backEnd.randomNumber = randomNumber - offset;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "2,0", "3,0", "3,1", "4,0", "4,1", "4,2", "5,0", "5,1", "5,2", "5,3",
        })
        void noBeatIsPossibleIfFigureJumpsOverOtherFigure(int randomNumber, int offset) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, offset + 1);
            placeFigureOnField(currentFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureOnNewFieldIsFromTheSamePlayer(int randomNumber) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[1];

            placeFigureOnField(otherFigure, randomNumber);
            placeFigureOnField(currentFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void beatIsPossible(int randomNumber) {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, randomNumber);
            placeFigureOnField(currentFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertTrue(backEnd.beatIsPossible(currentFigure));
        }

        @Test
        void newFieldIsNotWrapped() {
            int currentFigureIndex = 0;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, 39);
            placeFigureOnField(currentFigure, 38);
            backEnd.randomNumber = 1;

            assertTrue(backEnd.beatIsPossible(currentFigure));
        }

        @ParameterizedTest
        @CsvSource({
                "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1", "5,2", "5,3", "5,4", "6,0", "6,1", "6,2", "6,3", "6,4", "6,5",
            })
        void newFieldIsWrapped(int randomNumber, int offset) {
            int currentFigureIndex = 4;
            Figure currentFigure = backEnd.figures[currentFigureIndex];
            Figure otherFigure = backEnd.figures[0];

            placeFigureOnField(otherFigure, randomNumber - 1 - offset);
            placeFigureOnField(currentFigure, 39 - offset);
            backEnd.randomNumber = randomNumber;

            assertTrue(backEnd.beatIsPossible(currentFigure));
        }

        void placeFigureOnField(Figure figure, int value) {
            figure.setOnField();
            figure.setField(value, 0);
            figure.setProgress(value - (10 * figure.color));
        }
    }

    @Nested
    class moveSensibleTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenFigureInBase_whenCheckSensibleMove_thenReturnFalse() {
            assertAll(
                    IntStream.range(0, 16)
                            .mapToObj(
                                    i -> (Executable) (() -> assertFalse(backEnd.moveSensible(i))))
                            .toArray(Executable[]::new));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInMaxHouseField_whenCheckSensibleMove_thenReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setField(4 * ++playerIndex, 0);
            backEnd.figures[testFigureIndex].setInHouse();
            assertFalse(backEnd.moveSensible(testFigureIndex));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInHouse_whenCheckSensibleMove_thenReturnTrue(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 2;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setInHouse();
            backEnd.figures[testFigureIndex].setField(playerIndex * 4, 0);
            assertTrue(backEnd.moveSensible(testFigureIndex));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnFieldAndFigureHigherProgressOnField_whenCheckSensibleMove_thenReturnFalse(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10, 0);
            backEnd.figures[testFigureIndex + 1].setOnField();
            backEnd.figures[testFigureIndex + 1].setField(playerIndex * 10 + 5, 5);
            assertFalse(backEnd.moveSensible(testFigureIndex));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void fivenFigureOnFieldAndFigureLowerProgressOnField_whenCheckSensibleMove_thenReturnTrue(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setOnField();
            backEnd.figures[testFigureIndex].setField(playerIndex * 10 + 5, 4);
            backEnd.figures[testFigureIndex + 1].setOnField();
            backEnd.figures[testFigureIndex + 1].setField(playerIndex * 10, 0);
            assertTrue(backEnd.moveSensible(testFigureIndex));
        }
    }

    @Nested
    class moveToBaseTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnField_whenMoveFigureToBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setOnField();
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setField(30, 0);
            int expected = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.moveToBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            assertAll(
                    () ->
                            assertTrue(
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .isInBase()),
                    () ->
                            assertEquals(
                                    expected,
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .getField()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInHouse_whenMoveFigureToBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setInHouse();
            backEnd.moveToBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isInBase());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInBase_whenMoveFigureToBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.moveToBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isInBase());
        }
    }

    @Nested
    class getNameOfWinnerTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenNotAllFiguresFinished_whenGetNameOfWinner_thenReturnNull(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                backEnd.figures[i].setOnField();
            }
            assertTrue(backEnd.getNameOfWinner().isEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresFinished_whenGetNameOfWinner_thenReturnPlayerName(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                backEnd.figures[i].setFinished();
            }
            String expected = backEnd.players[playerIndex].getName();
            assertEquals(expected, backEnd.getNameOfWinner().get());
        }
    }

    @Nested
    class setFinishedFiguresTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oooo(int i) {
            placeFiguresInHouse(false, false, false, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oooi(int i) {
            placeFiguresInHouse(false, false, false, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void ooio(int i) {
            placeFiguresInHouse(false, false, true, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void ooii(int i) {
            placeFiguresInHouse(false, false, true, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oioo(int i) {
            placeFiguresInHouse(false, true, false, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oioi(int i) {
            placeFiguresInHouse(false, true, false, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oiio(int i) {
            placeFiguresInHouse(false, true, true, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void oiii(int i) {
            placeFiguresInHouse(false, true, true, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertFalse(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iooo(int i) {
            placeFiguresInHouse(true, false, false, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iooi(int i) {
            placeFiguresInHouse(true, false, false, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void ioio(int i) {
            placeFiguresInHouse(true, false, true, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void ioii(int i) {
            placeFiguresInHouse(true, false, true, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iioo(int i) {
            placeFiguresInHouse(true, true, false, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iioi(int i) {
            placeFiguresInHouse(true, true, false, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iiio(int i) {
            placeFiguresInHouse(true, true, true, false, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void iiii(int i) {
            placeFiguresInHouse(true, true, true, true, i);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void alreadyFinishedFiguresAreStillFinished(int i) {
            placeFiguresInHouse(true, false, false, false, i);
            backEnd.setFinishedFigures();
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, i).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, i).isFinished()));
        }

        @Test
        void multiplePlayersMayHaveFinishedFigures() {
            placeFiguresInHouse(true, false, false, false, 0);
            placeFiguresInHouse(true, false, true, false, 1);
            placeFiguresInHouse(true, true, false, false, 2);
            placeFiguresInHouse(true, true, true, true, 3);
            backEnd.setFinishedFigures();
            assertAll(
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, 0).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, 0).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, 0).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, 0).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, 1).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(2, 1).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, 1).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, 1).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, 2).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, 2).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(1, 2).isFinished()),
                    () -> assertFalse(nthFigureOfCurrentPlayer(0, 2).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(3, 3).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(2, 3).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(1, 3).isFinished()),
                    () -> assertTrue(nthFigureOfCurrentPlayer(0, 3).isFinished()));
        }

        void placeFiguresInHouse(
                boolean third, boolean second, boolean first, boolean zeroth, int i) {
            boolean[] figuresInHouse = {third, second, first, zeroth};
            int figureIndex, houseFieldIndex;
            figureIndex = houseFieldIndex = 4 * i + 3;

            for (boolean figureIsOnHouseField : figuresInHouse) {
                if (figureIsOnHouseField) {
                    backEnd.figures[figureIndex].setInHouse();
                    backEnd.figures[figureIndex].setField(houseFieldIndex, 0);
                    figureIndex--;
                }
                houseFieldIndex--;
            }
        }

        Figure nthFigureOfCurrentPlayer(int n, int i) {
            return backEnd.figures[4 * i + n];
        }
    }

    @Nested
    class figureOnFieldTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenNoFigureOnField_whenGetFigureOnField_thenReturnMinusOne() {
            assertTrue(backEnd.figureOnField(0).isEmpty());
        }

        @Test
        void givenFigureOnField_whenGetFigureOnField_thenReturnFigureIndex() {
            backEnd.figures[0].setOnField();
            backEnd.figures[0].setField(0, 0);
            int expected = 0;
            assertEquals(expected, backEnd.figureOnField(0).get());
        }
    }

    @Nested
    class figureOnHouseFieldTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @Test
        void givenNoFigureOnHouseField_whenGetFigureOnHouseField_thenReturnMinusOne() {
            setCurrentPlayer(0);
            assertTrue(backEnd.figureOnHouseField(0).isEmpty());
        }

        @Test
        void givenFigureOnHouseField_whenGetFigureOnHouseField_thenReturnFigureIndex() {
            backEnd.figures[0].setInHouse();
            backEnd.figures[0].setField(0, 0);
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(0).get());
        }

        @Test
        void givenFinishedFigureOnHouseField_whenGetFigureOnHouseField_thenReturnFigureIndex() {
            backEnd.figures[0].setField(0, 0);
            backEnd.figures[0].setFinished();
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(0).get());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureFinishedAndOneInHouse_whenGetFigureOnHouseField_thenReturnFigureIndex(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setField(
                    playerIndex * 4 + 3, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].setField(
                    playerIndex * 4 + 2, 0);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setFinished();
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].setInHouse();
            int expected = backEnd.currentPlayer.getIndexOfFirstFigure() + 1;
            assertEquals(expected, backEnd.figureOnHouseField(playerIndex * 4 + 2).get());
        }
    }

    @Nested
    class baseOfCurrentPlayerIsEmptyTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresInBase_whenCheckIfBaseIsEmpty_thanReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                backEnd.figures[i].setInBase();
            }
            assertFalse(backEnd.baseOfCurrentPlayerIsEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureInBas_whenCheckIfBaseIsEmpty_thanReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                backEnd.figures[i].setInBase();
            }
            backEnd.moveOutOfBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            assertFalse(backEnd.baseOfCurrentPlayerIsEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenAllFiguresOnField_whenCheckIfBaseIsEmpty_thanReturnTrue(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
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
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
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
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
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
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                backEnd.figures[i].setFinished();
            }
            int expected = 3;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenOneFigureOnField_whenCalculateTries_thanReturnOne(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
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
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInBase_whenMoveOutOfBase_thenFigureOnStartField(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.moveOutOfBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            int expectedField = playerIndex * 10;
            FigureState expectedState = FigureState.ON_FIELD;
            assertAll(
                    () ->
                            assertEquals(
                                    expectedField,
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .getField()),
                    () ->
                            assertEquals(
                                    expectedState,
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .getState()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureOnStartField_whenMoveOutOfBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 6;
            backEnd.moveOutOfBase(backEnd.currentPlayer.getIndexOfFirstFigure() + 1);
            backEnd.moveOutOfBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            FigureState expectedStateFigureToMove = FigureState.ON_FIELD;
            FigureState expectedStateFigureOnStartField = FigureState.IN_BASE;
            int expectedFieldFigureToMove = playerIndex * 10;
            int exopecetdFieldFigureOnStartField =
                    backEnd.currentPlayer.getIndexOfFirstFigure() + 1;
            assertAll(
                    () ->
                            assertEquals(
                                    expectedFieldFigureToMove,
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .getField()),
                    () ->
                            assertEquals(
                                    expectedStateFigureToMove,
                                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]
                                            .getState()),
                    () ->
                            assertEquals(
                                    exopecetdFieldFigureOnStartField,
                                    backEnd
                                            .figures[
                                            backEnd.currentPlayer.getIndexOfFirstFigure() + 1]
                                            .getField()),
                    () ->
                            assertEquals(
                                    expectedStateFigureOnStartField,
                                    backEnd
                                            .figures[
                                            backEnd.currentPlayer.getIndexOfFirstFigure() + 1]
                                            .getState()));
        }
    }

    @Nested
    class setNewCurrentPlayerIfNecessary {
        @BeforeEach
        void setup() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenRandomNumberIsNotSix_whenSetNewCurrentPlayer_thenCurrentPlayerChanged(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 5;
            backEnd.setNewCurrentPlayerIfNecessary();
            int expectedPlayerIndex = (playerIndex + 1) % 4;
            assertEquals(expectedPlayerIndex, backEnd.currentPlayerIndex);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenRandomNumberIsSix_whenSetNewCurrentPlayer_thenCurrentPlayerNotChanged(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 6;
            backEnd.setNewCurrentPlayerIfNecessary();
            assertEquals(playerIndex, backEnd.currentPlayerIndex);
        }
    }

    // Method for easier setting of the current player
    private void setCurrentPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 4) {
            backEnd.currentPlayerIndex = playerIndex;
            backEnd.currentPlayer = backEnd.players[playerIndex];

        } else {
            throw new IllegalArgumentException("Invalid player index");
        }
    }

    // Method to get the figure after of the current player
    private int getFigureAfterOfCurrentPlayer() {
        return (backEnd.currentPlayerIndex + 1) * 4;
    }
}
