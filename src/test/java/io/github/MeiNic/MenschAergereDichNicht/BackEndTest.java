package io.github.MeiNic.MenschAergereDichNicht;

import static org.junit.jupiter.api.Assertions.*;

import io.github.MeiNic.MenschAergereDichNicht.figure.Figure;
import io.github.MeiNic.MenschAergereDichNicht.figure.FigureState;
import io.github.MeiNic.MenschAergereDichNicht.player.Player;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
            backEnd =
                    new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, true) {
                        @Override
                        protected boolean generateRandomNumber() {
                            return true;
                        }
                    };
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void noFigureIsMoved(int playerIndex) {
            Player player = backEnd.players[playerIndex];
            BackEnd backEnd =
                    new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, true) {
                        @Override
                        protected boolean generateRandomNumber() {
                            return false;
                        }
                    };

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.botMove();

            assertEquals(4, numberOfFiguresInBase(player));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void noFigureIsMoved2(int playerIndex) {
            Player player = backEnd.players[playerIndex];

            for (int i = player.getIndexOfFirstFigure(); i < player.getIndexOfLastFigure(); i++) {
                Figure ownIthFigure = backEnd.figures[i];
                placeFigureInHouse(ownIthFigure, i);
                ownIthFigure.setFinished();
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.botMove();

            assertEquals(4, numberOfFiguresInHouse(player));
        }

        @ParameterizedTest
        @CsvSource({
            "0,1,3", "0,2,3", "0,3,3", "0,4,3", "0,5,3", "0,6,3",
            "1,1,3", "1,2,3", "1,3,3", "1,4,3", "1,5,3", "1,6,3",
            "2,1,3", "2,2,3", "2,3,3", "2,4,3", "2,5,3", "2,6,3",
            "3,1,3", "3,2,3", "3,3,3", "3,4,3", "3,5,3", "3,6,3",
            "0,1,2", "0,2,2", "0,3,2", "0,4,2", "0,5,2", "0,6,2",
            "1,1,2", "1,2,2", "1,3,2", "1,4,2", "1,5,2", "1,6,2",
            "2,1,2", "2,2,2", "2,3,2", "2,4,2", "2,5,2", "2,6,2",
            "3,1,2", "3,2,2", "3,3,2", "3,4,2", "3,5,2", "3,6,2",
            "0,1,1", "0,2,1", "0,3,1", "0,4,1", "0,5,1", "0,6,1",
            "1,1,1", "1,2,1", "1,3,1", "1,4,1", "1,5,1", "1,6,1",
            "2,1,1", "2,2,1", "2,3,1", "2,4,1", "2,5,1", "2,6,1",
            "3,1,1", "3,2,1", "3,3,1", "3,4,1", "3,5,1", "3,6,1",
        })
        void ownFigureOnStartFieldIsMoved(
                int playerIndex, int randomNumber, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];

            placeFigureOnField(ownFigure, player.getIndexOfStartField());

            for (int i = 1; i < 4 - numberOfFiguresInBase; i++) {
                Figure otherOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(otherOwnFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () ->
                            assertEquals(
                                    ownFigure.getField(),
                                    player.getIndexOfStartField() + randomNumber),
                    () -> assertEquals(numberOfFiguresInBase, numberOfFiguresInBase(player)));
        }

        @ParameterizedTest
        @CsvSource({
            "0,3", "0,2", "0,1",
            "1,3", "1,2", "1,1",
            "2,3", "2,2", "2,1",
            "3,3", "3,2", "3,1",
        })
        void otherFigureOnStartFieldIsBeaten(int playerIndex, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];
            int randomNumber = 6;

            Player otherPlayer = backEnd.players[(player.getPlayerIndex() + 1) % 4];
            Figure otherFigure = backEnd.figures[otherPlayer.getIndexOfFirstFigure()];

            placeFigureOnField(otherFigure, player.getIndexOfStartField());

            for (int i = 1; i < 4 - numberOfFiguresInBase; i++) {
                Figure otherOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(otherOwnFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () -> assertEquals(ownFigure.getField(), player.getIndexOfStartField()),
                    () -> assertTrue(otherFigure.isInBase()),
                    () -> assertEquals(numberOfFiguresInBase, numberOfFiguresInBase(player)));
        }

        @ParameterizedTest
        @CsvSource({
            "0,3", "0,2", "0,1",
            "1,3", "1,2", "1,1",
            "2,3", "2,2", "2,1",
            "3,3", "3,2", "3,1",
        })
        void ownFigureIsPlacedOnStartField(int playerIndex, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure =
                    backEnd.figures[player.getIndexOfLastFigure() - numberOfFiguresInBase];
            int randomNumber = 6;

            for (int i = 0; i < 4 - numberOfFiguresInBase; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () -> assertEquals(ownFigure.getField(), player.getIndexOfStartField()),
                    () -> assertEquals(numberOfFiguresInBase - 1, numberOfFiguresInBase(player)));
        }

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3", "0,4", "0,5", "0,6",
            "1,1", "1,2", "1,3", "1,4", "1,5", "1,6",
            "2,1", "2,2", "2,3", "2,4", "2,5", "2,6",
            "3,1", "3,2", "3,3", "3,4", "3,5", "3,6",
        })
        void ownFigureThatCanBeatOtherFigureIsMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Player otherPlayer = backEnd.players[(player.getPlayerIndex() + 1) % 4];
            Figure ownFigure = backEnd.figures[player.getIndexOfLastFigure() - 1];
            Figure otherFigure = backEnd.figures[otherPlayer.getIndexOfFirstFigure()];

            placeFigureOnField(ownFigure, player.getIndexOfStartField());
            placeFigureOnField(otherFigure, player.getIndexOfStartField() + randomNumber);

            for (int i = 0; i < 3; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () ->
                            assertEquals(
                                    ownFigure.getField(),
                                    player.getIndexOfStartField() + randomNumber),
                    () -> assertTrue(otherFigure.isInBase()));
        }

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3", "0,4", "0,5",
            "1,1", "1,2", "1,3", "1,4", "1,5",
            "2,1", "2,2", "2,3", "2,4", "2,5",
            "3,1", "3,2", "3,3", "3,4", "3,5",
        })
        void ownFigureIsMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfLastFigure() - 1];

            placeFigureOnField(ownFigure, player.getIndexOfStartField() + 1);

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () ->
                            assertEquals(
                                    ownFigure.getField(),
                                    player.getIndexOfStartField() + 1 + randomNumber));
        }

        int numberOfFiguresInBase(Player player) {
            int count = 0;

            for (int i = player.getIndexOfFirstFigure(); i < player.getIndexOfLastFigure(); i++) {
                if (backEnd.figures[i].isInBase()) {
                    count++;
                }
            }

            return count;
        }

        int numberOfFiguresInHouse(Player player) {
            int count = 0;

            for (int i = player.getIndexOfFirstFigure(); i < player.getIndexOfLastFigure(); i++) {
                if (backEnd.figures[i].isInHouse() || backEnd.figures[i].isFinished()) {
                    count++;
                }
            }

            return count;
        }
    }

    @Nested
    class getFigureThatMustBeMovedTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, true);
        }
        ;

        @ParameterizedTest
        @CsvSource({
            "0,1,3", "0,2,3", "0,3,3", "0,4,3", "0,5,3", "0,6,3",
            "1,1,3", "1,2,3", "1,3,3", "1,4,3", "1,5,3", "1,6,3",
            "2,1,3", "2,2,3", "2,3,3", "2,4,3", "2,5,3", "2,6,3",
            "3,1,3", "3,2,3", "3,3,3", "3,4,3", "3,5,3", "3,6,3",
            "0,1,2", "0,2,2", "0,3,2", "0,4,2", "0,5,2", "0,6,2",
            "1,1,2", "1,2,2", "1,3,2", "1,4,2", "1,5,2", "1,6,2",
            "2,1,2", "2,2,2", "2,3,2", "2,4,2", "2,5,2", "2,6,2",
            "3,1,2", "3,2,2", "3,3,2", "3,4,2", "3,5,2", "3,6,2",
            "0,1,1", "0,2,1", "0,3,1", "0,4,1", "0,5,1", "0,6,1",
            "1,1,1", "1,2,1", "1,3,1", "1,4,1", "1,5,1", "1,6,1",
            "2,1,1", "2,2,1", "2,3,1", "2,4,1", "2,5,1", "2,6,1",
            "3,1,1", "3,2,1", "3,3,1", "3,4,1", "3,5,1", "3,6,1",
        })
        void ownFigureOnStartFieldMustBeMoved(
                int playerIndex, int randomNumber, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfLastFigure() - 1];

            for (int i = 0; i < 4 - numberOfFiguresInBase - 1; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            placeFigureOnField(ownFigure, player.getIndexOfStartField());

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatMustBeMoved = backEnd.getFigureThatMustBeMoved();

            assertAll(
                    () -> assertTrue(figureThatMustBeMoved.isPresent()),
                    () -> assertEquals(ownFigure, figureThatMustBeMoved.get()));
        }

        @ParameterizedTest
        @CsvSource({
            "0,3", "0,2", "0,1",
            "1,3", "1,2", "1,1",
            "2,3", "2,2", "2,1",
            "3,3", "3,2", "3,1",
        })
        void firstOwnFigureInBaseMustBeMoved(int playerIndex, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];

            for (int i = 1; i <= 4 - numberOfFiguresInBase; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = 6;
            Optional<Figure> figureThatMustBeMoved = backEnd.getFigureThatMustBeMoved();

            assertAll(
                    () -> assertTrue(figureThatMustBeMoved.isPresent()),
                    () -> assertEquals(ownFigure, figureThatMustBeMoved.get()));
        }

        @ParameterizedTest
        @CsvSource({
            "0,1,3", "0,2,3", "0,3,3", "0,4,3", "0,5,3",
            "1,1,3", "1,2,3", "1,3,3", "1,4,3", "1,5,3",
            "2,1,3", "2,2,3", "2,3,3", "2,4,3", "2,5,3",
            "3,1,3", "3,2,3", "3,3,3", "3,4,3", "3,5,3",
            "0,1,2", "0,2,2", "0,3,2", "0,4,2", "0,5,2",
            "1,1,2", "1,2,2", "1,3,2", "1,4,2", "1,5,2",
            "2,1,2", "2,2,2", "2,3,2", "2,4,2", "2,5,2",
            "3,1,2", "3,2,2", "3,3,2", "3,4,2", "3,5,2",
            "0,1,1", "0,2,1", "0,3,1", "0,4,1", "0,5,1",
            "1,1,1", "1,2,1", "1,3,1", "1,4,1", "1,5,1",
            "2,1,1", "2,2,1", "2,3,1", "2,4,1", "2,5,1",
            "3,1,1", "3,2,1", "3,3,1", "3,4,1", "3,5,1",
        })
        void ownFigureInBaseDoesNotHaveToBeMoved(
                int playerIndex, int randomNumber, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];

            for (int i = 1; i <= 4 - numberOfFiguresInBase; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouse(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatMustBeMoved = backEnd.getFigureThatMustBeMoved();

            assertFalse(figureThatMustBeMoved.isPresent());
        }

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3", "0,4", "0,5",
            "1,1", "1,2", "1,3", "1,4", "1,5",
            "2,1", "2,2", "2,3", "2,4", "2,5",
            "3,1", "3,2", "3,3", "3,4", "3,5",
        })
        void firstOwnFigureThatCanBeatOtherFigureMustBeMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Player otherPlayer = backEnd.players[(playerIndex + 1) % 4];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];
            Figure ownSecondFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure ownThirdFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure ownFourthFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];
            Figure otherFigure = backEnd.figures[otherPlayer.getIndexOfFirstFigure()];

            placeFigureInHouse(ownFigure, ownFigure.getIndex());
            placeFigureInHouse(ownThirdFigure, ownThirdFigure.getIndex());
            placeFigureInHouse(ownFourthFigure, ownFourthFigure.getIndex());

            placeFigureOnField(ownSecondFigure, player.getIndexOfStartField());
            placeFigureOnField(otherFigure, player.getIndexOfStartField() + randomNumber);

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatMustBeMoved = backEnd.getFigureThatMustBeMoved();

            assertAll(
                    () -> assertTrue(figureThatMustBeMoved.isPresent()),
                    () -> assertEquals(ownSecondFigure, figureThatMustBeMoved.get()));
        }
    }

    @Nested
    class getFigureThatShouldBeMovedTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, true);
        }
        ;

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3",
            "1,1", "1,2", "1,3",
            "2,1", "2,2", "2,3",
            "3,1", "3,2", "3,3",
        })
        void ownFigureInHouseWhichDoesNotExceedFieldsShouldBeMoved(
                int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];

            placeFigureInHouse(ownFigure, player.getIndexOfFirstFigure());

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatShouldBeMoved = backEnd.getFigureThatShouldBeMoved();

            assertAll(
                    () -> assertTrue(figureThatShouldBeMoved.isPresent()),
                    () -> assertEquals(ownFigure, figureThatShouldBeMoved.get()));
        }

        @ParameterizedTest
        @CsvSource({
            "0,4,0", "0,3,1", "0,2,2", "0,1,3",
            "1,4,0", "1,3,1", "1,2,2", "1,1,3",
            "2,4,0", "2,3,1", "2,2,2", "2,1,3",
            "3,4,0", "3,3,1", "3,2,2", "3,1,3",
        })
        void ownFigureInHouseWhichExceedsFieldsShouldNotBeMoved(
                int playerIndex, int randomNumber, int offset) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];

            placeFigureInHouse(ownFigure, player.getIndexOfFirstFigure() + offset);

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatShouldBeMoved = backEnd.getFigureThatShouldBeMoved();

            assertFalse(figureThatShouldBeMoved.isPresent());
        }

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3", "0,4", "0,5",
            "1,1", "1,2", "1,3", "1,4", "1,5",
            "2,1", "2,2", "2,3", "2,4", "2,5",
            "3,1", "3,2", "3,3", "3,4", "3,5",
        })
        void ownFigureWithHighestProgressShouldBeMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];
            Figure ownSecondFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure ownThirdFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure ownFourthFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureOnField(ownFourthFigure, player.getIndexOfStartField());
            placeFigureOnField(ownFigure, player.getIndexOfStartField() + 2);
            placeFigureOnField(ownSecondFigure, player.getIndexOfStartField() + 4);
            placeFigureOnField(ownThirdFigure, player.getIndexOfStartField() + 6);

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatShouldBeMoved = backEnd.getFigureThatShouldBeMoved();

            assertAll(
                    () -> assertTrue(figureThatShouldBeMoved.isPresent()),
                    () -> assertEquals(ownThirdFigure, figureThatShouldBeMoved.get()));
        }

        @ParameterizedTest
        @CsvSource({
            "0,1", "0,2", "0,3", "0,4", "0,5", "0,6",
            "1,1", "1,2", "1,3", "1,4", "1,5", "1,6",
            "2,1", "2,2", "2,3", "2,4", "2,5", "2,6",
            "3,1", "3,2", "3,3", "3,4", "3,5", "3,6",
        })
        void ownFigureInBaseShouldNotBeMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];

            backEnd.currentPlayer = player;
            backEnd.currentPlayerIndex = player.getPlayerIndex();
            backEnd.randomNumber = randomNumber;
            Optional<Figure> figureThatShouldBeMoved = backEnd.getFigureThatShouldBeMoved();

            assertFalse(figureThatShouldBeMoved.isPresent());
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveOnField(backEnd.figures[testFigureIndex]);
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
            backEnd.moveInHouse(backEnd.figures[testFigureIndex]);
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
            backEnd.moveInHouse(backEnd.figures[testFigureIndex]);
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
            backEnd.moveInHouse(backEnd.figures[testFigureIndex]);
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
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInBase();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureIsInHouse(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInHouse();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureIsFinished(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setFinished();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1",
            "5,2", "5,3", "6,0", "6,1", "6,2", "6,3",
        })
        void noBeatIsPossibleIfFigureIsAboutToEnterTheHouse(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, 0 + offset);
            placeFigureOnField(thisFigure, 40 - randomNumber + offset);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1",
            "5,2", "5,3", "5,4", "6,0", "6,1", "6,2", "6,3", "6,4", "6,5",
        })
        void noBeatIsPossibleIfFigureCannotReachToOtherFigure(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, randomNumber + 1);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber - offset;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "2,0", "3,0", "3,1", "4,0", "4,1", "4,2", "5,0", "5,1", "5,2", "5,3",
        })
        void noBeatIsPossibleIfFigureJumpsOverOtherFigure(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, offset + 1);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void noBeatIsPossibleIfFigureOnNewFieldIsFromTheSamePlayer(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[1];

            placeFigureOnField(otherFigure, randomNumber);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6})
        void beatIsPossible(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, randomNumber);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertTrue(backEnd.beatIsPossible(thisFigure));
        }

        @Test
        void newFieldIsNotWrapped() {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, 39);
            placeFigureOnField(thisFigure, 38);
            backEnd.randomNumber = 1;

            assertTrue(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @CsvSource({
            "1,0", "2,0", "2,1", "3,0", "3,1", "3,2", "4,0", "4,1", "4,2", "4,3", "5,0", "5,1",
            "5,2", "5,3", "5,4", "6,0", "6,1", "6,2", "6,3", "6,4", "6,5",
        })
        void newFieldIsWrapped(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[4];
            Figure otherFigure = backEnd.figures[0];

            placeFigureOnField(otherFigure, randomNumber - 1 - offset);
            placeFigureOnField(thisFigure, 39 - offset);
            backEnd.randomNumber = randomNumber;

            assertTrue(backEnd.beatIsPossible(thisFigure));
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
            backEnd.moveToBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
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
            backEnd.moveToBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
            assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isInBase());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void givenFigureInBase_whenMoveFigureToBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.moveToBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
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
            assertEquals(expected, backEnd.figureOnField(0).get().getField());
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
            assertEquals(expected, backEnd.figureOnHouseField(0).get().getIndex());
        }

        @Test
        void givenFinishedFigureOnHouseField_whenGetFigureOnHouseField_thenReturnFigureIndex() {
            backEnd.figures[0].setField(0, 0);
            backEnd.figures[0].setFinished();
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(0).get().getIndex());
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
            assertEquals(
                    expected, backEnd.figureOnHouseField(playerIndex * 4 + 2).get().getIndex());
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
            backEnd.moveOutOfBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
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
            backEnd.moveOutOfBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
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
            backEnd.moveOutOfBase(
                    backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1]);
            backEnd.moveOutOfBase(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()]);
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

    private void placeFigureOnField(Figure figure, int value) {
        assert value >= 0 && value <= 39
                : String.format("Value must be in range [0; 39], but was %d", value);

        figure.setOnField();
        figure.setField(value, 0);

        int progress = value - (10 * figure.color);
        if (progress < 0) {
            progress += 40;
        }
        figure.setProgress(progress);
    }

    private void placeFigureInHouse(Figure figure, int value) {
        assert value >= figure.color * 4 && value <= figure.color * 4 + 3
                : String.format(
                        "Value must be in range [%d; %d], but was %d",
                        figure.color * 4, figure.color * 4 + 3, value);

        figure.setInHouse();
        figure.setField(value, 0);
    }
}
