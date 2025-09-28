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

package io.github.MeiNic.MenschAergereDichNicht;

import static org.junit.jupiter.api.Assertions.*;

import io.github.MeiNic.MenschAergereDichNicht.figure.Figure;
import io.github.MeiNic.MenschAergereDichNicht.figure.FigureState;
import io.github.MeiNic.MenschAergereDichNicht.player.Player;
import io.github.MeiNic.MenschAergereDichNicht.stateMachine.Event;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
        @PermutationSource({@Range(lower = 0, upper = 3)})
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
            backEnd.botMove();

            assertEquals(4, numberOfFiguresInBase(player));
        }

        @ParameterizedTest
        @PermutationSource({@Range(lower = 0, upper = 3)})
        void noFigureIsMoved2(int playerIndex) {
            Player player = backEnd.players[playerIndex];

            for (int i = player.getIndexOfFirstFigure(); i < player.getIndexOfLastFigure(); i++) {
                Figure ownIthFigure = backEnd.figures[i];
                placeFigureInHouseDeprecated(ownIthFigure, i);
            }

            backEnd.currentPlayer = player;
            backEnd.botMove();

            assertEquals(4, numberOfFiguresInHouse(player));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
            @Range(lower = 1, upper = 6),
            @Range(lower = 1, upper = 3),
        })
        void ownFigureOnStartFieldIsMoved(
                int playerIndex, int randomNumber, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfFirstFigure()];

            placeFigureOnField(ownFigure, player.getIndexOfStartField());

            for (int i = 1; i < 4 - numberOfFiguresInBase; i++) {
                Figure otherOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouseDeprecated(otherOwnFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
            @Range(lower = 1, upper = 3),
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
                placeFigureInHouseDeprecated(otherOwnFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () -> assertEquals(ownFigure.getField(), player.getIndexOfStartField()),
                    () -> assertTrue(otherFigure.isInBase()),
                    () -> assertEquals(numberOfFiguresInBase, numberOfFiguresInBase(player)));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
            @Range(lower = 1, upper = 3),
        })
        void ownFigureIsPlacedOnStartField(int playerIndex, int numberOfFiguresInBase) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure =
                    backEnd.figures[player.getIndexOfLastFigure() - numberOfFiguresInBase];
            int randomNumber = 6;

            for (int i = 0; i < 4 - numberOfFiguresInBase; i++) {
                Figure ownIthFigure = backEnd.figures[player.getIndexOfFirstFigure() + i];
                placeFigureInHouseDeprecated(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
            backEnd.randomNumber = randomNumber;
            backEnd.botMove();

            assertAll(
                    () -> assertTrue(ownFigure.isOnField()),
                    () -> assertEquals(ownFigure.getField(), player.getIndexOfStartField()),
                    () -> assertEquals(numberOfFiguresInBase - 1, numberOfFiguresInBase(player)));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
            @Range(lower = 1, upper = 6),
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
                placeFigureInHouseDeprecated(ownIthFigure, player.getIndexOfFirstFigure() + i);
            }

            backEnd.currentPlayer = player;
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
            @Range(lower = 1, upper = 5), // Exclude six so we don't have to move out of the base
        })
        void ownFigureIsMoved(int playerIndex, int randomNumber) {
            Player player = backEnd.players[playerIndex];
            Figure ownFigure = backEnd.figures[player.getIndexOfLastFigure() - 1];

            placeFigureOnField(ownFigure, player.getIndexOfStartField() + 1);

            backEnd.currentPlayer = player;
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
                if (backEnd.figures[i].isInHouse()) {
                    count++;
                }
            }

            return count;
        }
    }

    @Nested
    class moveOnFieldTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "blue", "green", "red"}, 4, false);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 1, upper = 6),
        })
        void noBeatIsPossibleIfFigureIsInBase(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInBase();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 1, upper = 6),
        })
        void noBeatIsPossibleIfFigureIsInHouse(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            backEnd.figures[0].setInHouse();
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 1, upper = 6),
        })
        void noBeatIsPossibleIfFigureIsFinished(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(backEnd.figures[4], randomNumber);
            placeFigureInHouse(backEnd.figures[0], 3);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @MethodSource
        void noBeatIsPossibleIfFigureIsAboutToEnterTheHouse(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, 0 + offset);
            placeFigureOnField(thisFigure, 40 - randomNumber + offset);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        static Stream<Arguments> noBeatIsPossibleIfFigureIsAboutToEnterTheHouse() {
            return IntStream.rangeClosed(1, 6)
                    .boxed()
                    .flatMap(
                            i ->
                                    IntStream.rangeClosed(0, Math.min(i - 1, 3))
                                            .mapToObj(j -> Arguments.of(i, j)));
        }

        @ParameterizedTest
        @MethodSource
        void noBeatIsPossibleIfFigureCannotReachToOtherFigure(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, randomNumber + 1);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber - offset;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        static Stream<Arguments> noBeatIsPossibleIfFigureCannotReachToOtherFigure() {
            return IntStream.rangeClosed(1, 6)
                    .boxed()
                    .flatMap(
                            i -> IntStream.rangeClosed(0, i - 1).mapToObj(j -> Arguments.of(i, j)));
        }

        @ParameterizedTest
        @MethodSource
        void noBeatIsPossibleIfFigureJumpsOverOtherFigure(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[4];

            placeFigureOnField(otherFigure, offset + 1);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        static Stream<Arguments> noBeatIsPossibleIfFigureJumpsOverOtherFigure() {
            return IntStream.rangeClosed(2, 5)
                    .boxed()
                    .flatMap(
                            i -> IntStream.rangeClosed(0, i - 2).mapToObj(j -> Arguments.of(i, j)));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 1, upper = 6),
        })
        void noBeatIsPossibleIfFigureOnNewFieldIsFromTheSamePlayer(int randomNumber) {
            Figure thisFigure = backEnd.figures[0];
            Figure otherFigure = backEnd.figures[1];

            placeFigureOnField(otherFigure, randomNumber);
            placeFigureOnField(thisFigure, 0);
            backEnd.randomNumber = randomNumber;

            assertFalse(backEnd.beatIsPossible(thisFigure));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 1, upper = 6),
        })
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
        @MethodSource
        void newFieldIsWrapped(int randomNumber, int offset) {
            Figure thisFigure = backEnd.figures[4];
            Figure otherFigure = backEnd.figures[0];

            placeFigureOnField(otherFigure, randomNumber - 1 - offset);
            placeFigureOnField(thisFigure, 39 - offset);
            backEnd.randomNumber = randomNumber;

            assertTrue(backEnd.beatIsPossible(thisFigure));
        }

        static Stream<Arguments> newFieldIsWrapped() {
            return IntStream.rangeClosed(1, 6)
                    .boxed()
                    .flatMap(
                            i -> IntStream.rangeClosed(0, i - 1).mapToObj(j -> Arguments.of(i, j)));
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenFigureInMaxHouseField_whenCheckSensibleMove_thenReturnFalse(int playerIndex) {
            setCurrentPlayer(playerIndex);
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setField(4 * ++playerIndex, 0);
            backEnd.figures[testFigureIndex].setInHouse();
            assertFalse(backEnd.moveSensible(testFigureIndex));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenFigureInHouse_whenCheckSensibleMove_thenReturnTrue(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 2;
            final int testFigureIndex = backEnd.currentPlayer.getIndexOfFirstFigure();
            backEnd.figures[testFigureIndex].setInHouse();
            backEnd.figures[testFigureIndex].setField(playerIndex * 4, 0);
            assertTrue(backEnd.moveSensible(testFigureIndex));
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenFigureInHouse_whenMoveFigureToBase_thenFigureInBase(int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].setInHouse();
            backEnd.moveToBase(backEnd.currentPlayer.getIndexOfFirstFigure());
            assertTrue(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()].isInBase());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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

        @Test
        void noWinnerFoundWhenAllFiguresInBase() {
            assertTrue(backEnd.getNameOfWinner().isEmpty());
        }

        @Test
        void noWinnerFoundWhenAllFiguresOnField() {
            for (int i = 0; i < backEnd.figures.length; i++) {
                Figure figure = backEnd.figures[i];
                placeFigureOnField(figure, i);
            }
            assertTrue(backEnd.getNameOfWinner().isEmpty());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void winnerFoundWhenAllFiguresOfPlayerAreInHouse(int playerIndex) {
            Player player = backEnd.players[playerIndex];
            for (int i = player.getIndexOfFirstFigure(); i < player.getIndexOfLastFigure(); i++) {
                Figure figure = backEnd.figures[i];
                placeFigureInHouse(figure, i - player.getIndexOfFirstFigure());
            }
            assertEquals(player.getName(), backEnd.getNameOfWinner().get());
        }
    }

    @Nested
    class getFinishedFiguresTest {
        @BeforeEach
        void setUp() {
            backEnd = new BackEnd(new String[] {"orange", "green", "blue", "red"}, 4, false);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oooo(int i) {
            Player player = backEnd.players[i];

            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oooi(int i) {
            Player player = backEnd.players[i];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void ooio(int i) {
            Player player = backEnd.players[i];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 1);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void ooii(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 1);
            placeFigureInHouse(thirdOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oioo(int i) {
            Player player = backEnd.players[i];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 2);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oioi(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 2);
            placeFigureInHouse(thirdOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oiio(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 2);
            placeFigureInHouse(thirdOwnFigure, 1);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void oiii(int i) {
            Player player = backEnd.players[i];
            Figure secondOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 2);
            placeFigureInHouse(thirdOwnFigure, 1);
            placeFigureInHouse(secondOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iooo(int i) {
            Player player = backEnd.players[i];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iooi(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void ioio(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 1);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void ioii(int i) {
            Player player = backEnd.players[i];
            Figure secondOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 1);
            placeFigureInHouse(secondOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iioo(int i) {
            Player player = backEnd.players[i];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 2);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure, thirdOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iioi(int i) {
            Player player = backEnd.players[i];
            Figure secondOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 2);
            placeFigureInHouse(secondOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(new Figure[] {fourthOwnFigure, thirdOwnFigure}, finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iiio(int i) {
            Player player = backEnd.players[i];
            Figure secondOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 2);
            placeFigureInHouse(secondOwnFigure, 1);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(
                    new Figure[] {fourthOwnFigure, thirdOwnFigure, secondOwnFigure},
                    finishedFigures);
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void iiii(int i) {
            Player player = backEnd.players[i];
            Figure firstOwnFigure = backEnd.figures[player.getIndexOfFirstFigure()];
            Figure secondOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 1];
            Figure thirdOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 2];
            Figure fourthOwnFigure = backEnd.figures[player.getIndexOfFirstFigure() + 3];

            placeFigureInHouse(fourthOwnFigure, 3);
            placeFigureInHouse(thirdOwnFigure, 2);
            placeFigureInHouse(secondOwnFigure, 1);
            placeFigureInHouse(firstOwnFigure, 0);
            Figure[] finishedFigures = backEnd.getFinishedFigures(player);

            assertArrayEquals(
                    new Figure[] {fourthOwnFigure, thirdOwnFigure, secondOwnFigure, firstOwnFigure},
                    finishedFigures);
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
            placeFigureInHouse(backEnd.figures[0], 3);
            int expected = 0;
            assertEquals(expected, backEnd.figureOnHouseField(3).get());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenOneFigureFinishedAndOneInHouse_whenGetFigureOnHouseField_thenReturnFigureIndex(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            placeFigureInHouse(backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure()], 3);
            backEnd.figures[backEnd.currentPlayer.getIndexOfFirstFigure() + 1].setField(
                    playerIndex * 4 + 2, 0);
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenAllFiguresInBase_whenCalculateTries_thanReturnThree(int playerIndex) {
            setCurrentPlayer(playerIndex);
            int expected = 3;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenAllFiguresFinished_whenCalculateTries_thanReturnThree(int playerIndex) {
            setCurrentPlayer(playerIndex);
            for (int i = backEnd.currentPlayer.getIndexOfFirstFigure();
                    i < getFigureAfterOfCurrentPlayer();
                    i++) {
                placeFigureInHouse(
                        backEnd.figures[i], i - backEnd.currentPlayer.getIndexOfFirstFigure());
            }
            int expected = 3;
            assertEquals(expected, backEnd.getNumberOfAllowedTries());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
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
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenRandomNumberIsNotSix_whenSetNewCurrentPlayer_thenCurrentPlayerChanged(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 5;
            backEnd.stateMachine.handleEvent(Event.ROLL_DICE_CANNOT_MOVE);
            backEnd.setNewCurrentPlayerIfNecessary();
            int expectedPlayerIndex = (playerIndex + 1) % 4;
            assertEquals(expectedPlayerIndex, backEnd.currentPlayer.getPlayerIndex());
        }

        @ParameterizedTest
        @PermutationSource({
            @Range(lower = 0, upper = 3),
        })
        void givenRandomNumberIsSix_whenSetNewCurrentPlayer_thenCurrentPlayerNotChanged(
                int playerIndex) {
            setCurrentPlayer(playerIndex);
            backEnd.randomNumber = 6;
            backEnd.stateMachine.handleEvent(Event.ROLL_DICE_CAN_MOVE);
            backEnd.setNewCurrentPlayerIfNecessary();
            assertEquals(playerIndex, backEnd.currentPlayer.getPlayerIndex());
        }
    }

    // Method for easier setting of the current player
    private void setCurrentPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 4) {
            backEnd.currentPlayer = backEnd.players[playerIndex];

        } else {
            throw new IllegalArgumentException("Invalid player index");
        }
    }

    // Method to get the figure after of the current player
    private int getFigureAfterOfCurrentPlayer() {
        return (backEnd.currentPlayer.getPlayerIndex() + 1) * 4;
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

    private void placeFigureInHouseDeprecated(Figure figure, int value) {
        assert value >= figure.color * 4 && value <= figure.color * 4 + 3
                : String.format(
                        "Value must be in range [%d; %d], but was %d",
                        figure.color * 4, figure.color * 4 + 3, value);

        figure.setInHouse();
        figure.setField(value, 0);
    }

    private void placeFigureInHouse(Figure figure, int value) {
        assert value >= 0 && value <= 3
                : String.format("Value must be in range [0; 3], but was %d", value);

        figure.setInHouse();
        figure.setField(figure.color * 4 + value, 0);
    }
}
