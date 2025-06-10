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

import io.github.MeiNic.MenschAergereDichNicht.dice.Dice;
import io.github.MeiNic.MenschAergereDichNicht.dice.LaPlaceDice;
import io.github.MeiNic.MenschAergereDichNicht.dice.LoadedDice;
import io.github.MeiNic.MenschAergereDichNicht.figure.Figure;
import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;
import io.github.MeiNic.MenschAergereDichNicht.player.Bot;
import io.github.MeiNic.MenschAergereDichNicht.player.Dummy;
import io.github.MeiNic.MenschAergereDichNicht.player.Human;
import io.github.MeiNic.MenschAergereDichNicht.player.Player;
import java.util.Optional;

public class BackEnd {
    public final Figure[] figures;

    public int randomNumber;

    protected final Player[] players;
    protected Player currentPlayer;
    protected int currentPlayerIndex;

    final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public BackEnd(String[] names, int numberOfHumanPlayers, boolean fillWithBots) {
        figures = new Figure[16];
        players = new Player[4];

        for (int i = 0; i < 16; i++) {
            int index = Math.floorDiv(i, 4);
            figures[i] = new Figure(i, index, names[index]);
        }

        randomNumber = 0;

        for (int i = 0; i < 4; i++) {
            if (i < numberOfHumanPlayers) {
                players[i] = new Human(names[i], i);
            } else if (fillWithBots) {
                players[i] = new Bot(names[i], i);
            } else {
                players[i] = new Dummy(names[i], i);
            }
        }

        currentPlayerIndex = 0;
        currentPlayer = players[currentPlayerIndex];
    }

    public String getNameOfCurrentPlayer() {
        return currentPlayer.getName();
    }

    public int getPlayerStateOfCurrentPlayer() {
        return currentPlayer.getPlayerState();
    }

    // progress a dice input
    public boolean playerMove() {
        if (!generateRandomNumber()) {
            return false;
        }

        // cache a much used value, make the code look cleaner
        Optional<Integer> figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        boolean ownFigureOnStartfield =
                figureOnStartfield.isPresent()
                        && figures[figureOnStartfield.get()]
                                .getOwner()
                                .equals(currentPlayer.getName());

        if (ownFigureOnStartfield && !baseOfCurrentPlayerIsEmpty()) {
            figures[figureOnStartfield.get()].enablePlacement();
        } else if (!baseOfCurrentPlayerIsEmpty() && randomNumber == 6) {
            for (int i = currentPlayer.getIndexOfFirstFigure();
                    i < currentPlayer.getIndexOfLastFigure();
                    i++) {
                if (figures[i].isInBase()) {
                    figures[i].enablePlacement();
                }
            }
        } else {
            playerMoveOnField();
        }
        return true;
    }

    // part of the playerMove-method - don't use out of it
    private void playerMoveOnField() {
        boolean beatsPossible = false;

        for (int i = currentPlayer.getIndexOfFirstFigure();
                i < currentPlayer.getIndexOfLastFigure();
                i++) {
            if (beatIsPossible(figures[i])) {
                figures[i].enablePlacement();
                beatsPossible = true;
            }
        }

        if (beatsPossible) {
            // Only figures able to beat another figure should be
            // moved now.
            return;
        }

        // Any movable figure should be moved now.
        for (int i = currentPlayer.getIndexOfFirstFigure();
                i < currentPlayer.getIndexOfLastFigure();
                i++) {
            if (figures[i].isMovable()) {
                figures[i].enablePlacement();
            }
        }
    }

    public void disablePlacementForAllFigures() {
        for (Figure figure : figures) {
            figure.disablePlacement();
        }
    }

    // bot-move on the "normal" fields
    public void botMove() {
        if (!generateRandomNumber()) {
            return;
        }

        // cache a much used value, make the code look cleaner
        Optional<Integer> figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        int indexOfFirstFigure = currentPlayer.getIndexOfFirstFigure();
        int indexOfLastFigure = currentPlayer.getIndexOfLastFigure();

        // LOGGER.info("Bot " + currentPlayer.getName() + " rolled a " + randomNumber);
        if (!baseOfCurrentPlayerIsEmpty()) {
            if (figureOnStartfield.isPresent()
                    && figures[figureOnStartfield.get()]
                            .getOwner()
                            .equals(currentPlayer.getName())) {
                moveFigure(figureOnStartfield.get());
                return;
            } else if (randomNumber == 6) {
                for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
                    if (figures[i].isInBase()) {
                        moveFigure(i);
                        return;
                    }
                }
            }
        }
        for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
            if (beatIsPossible(figures[i])) {
                moveFigure(i);
                return;
            }
        }
        for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
            if (moveSensible(i)) {
                moveFigure(i);
                return;
            }
        }
    }

    // move the given figure by the given number
    public void moveFigure(int figureNumber) {
        switch (figures[figureNumber].getState()) {
            case FINISHED -> LOGGER.info("Finished figure isn't moveable. Aborting...");
            case IN_HOUSE -> moveInHouse(figureNumber);
            case IN_BASE -> {
                if (randomNumber == 6) moveOutOfBase(figureNumber);
            }
            case ON_FIELD -> moveOnField(figureNumber);
        }
    }

    protected void moveOnField(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];

        // store the old and new field-number in local variables
        int numberNew = figureToBeMoved.getField() + randomNumber;

        if (numberNew > 39) {
            numberNew -= 40;
        }

        // Figure is about to enter their house.
        boolean goToHouse = 39 - figureToBeMoved.getProgress() < randomNumber;

        if (!goToHouse) {
            // move the figure, if the new field is free
            if (!figureOnField(numberNew).isPresent()) {
                figureToBeMoved.moveByValue(randomNumber);
            } else {
                // move the figure, and move the figure before on the field
                // to the base
                if (!figures[figureOnField(numberNew).get()]
                        .getOwner()
                        .equals(figureToBeMoved.getOwner())) {
                    moveToBase(figureOnField(numberNew).get());
                    figureToBeMoved.setField(numberNew, randomNumber);
                } else {
                    // perform the moveFigure-method with the figure,
                    // standing on the field th figure at the moment wants
                    // to move, and the same stepLength
                    moveFigure(figureOnField(numberNew).get());
                }
            }
        } else {
            int positionInHouse = randomNumber - (39 - figureToBeMoved.getProgress()) - 1;
            // Move would exceed fields in house
            if (positionInHouse >= 4) return;
            positionInHouse += figureToBeMoved.color * 4;
            for (int i = figureToBeMoved.color * 4; i <= positionInHouse; i++) {
                if (figureOnHouseField(i).isPresent()) return;
            }
            figureToBeMoved.setInHouse();
            figureToBeMoved.setField(positionInHouse, randomNumber);
        }
    }

    // move figure in the house by the given value
    protected void moveInHouse(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];

        int newField = figureToBeMoved.getField() + randomNumber;
        int maxField = figureToBeMoved.color * 4 + 4;

        if (maxField <= newField) {
            LOGGER.info("Move would exceed the number of fields in the house. Aborting move...");
            return;
        }

        for (int i = figureToBeMoved.getField() + 1; i <= newField; i++) {
            if (figureOnHouseField(i).isPresent()) {
                LOGGER.info(
                        "Figure would have to jump over other figures on field: "
                                + i
                                + " in the house, which is forbidden. Aborting move...");
                return;
            }
        }

        // Finally we can move the figure to its new position.
        figureToBeMoved.moveByValue(randomNumber);
    }

    protected boolean beatIsPossible(Figure thisFigure) {
        if (!thisFigure.isOnField()) {
            return false;
        }

        boolean thisFigureIsAboutToEnterTheHouse = thisFigure.getProgress() + randomNumber > 39;
        if (thisFigureIsAboutToEnterTheHouse) {
            return false;
        }

        int oldField = thisFigure.getField();
        int newField = oldField + randomNumber;
        if (newField > 39) {
            newField -= 40;
        }

        boolean newFieldIsEmpty = !figureOnField(newField).isPresent();
        if (newFieldIsEmpty) {
            return false;
        }

        Figure otherFigure = figures[figureOnField(newField).get()];
        return !thisFigure.getOwner().equals(otherFigure.getOwner());
    }

    protected boolean moveSensible(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];

        switch (figureToBeMoved.getState()) {
            case IN_HOUSE -> {
                int newField = figureToBeMoved.getField() + randomNumber;
                int maxField = figureToBeMoved.color * 4 + 4;
                return maxField > newField;
            }
            case ON_FIELD -> {
                int color = figureToBeMoved.color;
                for (int i = color * 4; i < (color + 1) * 4; i++) {
                    if (figureToBeMoved.getProgress() < figures[i].getProgress()) return false;
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    // move the given figure to the base
    public void moveToBase(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];
        figureToBeMoved.setInBase();
        figureToBeMoved.setField(figureNumber, randomNumber);
    }

    // check which player has won
    public Optional<String> getNameOfWinner() {
        setFinishedFigures();

        for (int i = 0; i < 16; i += 4) {
            if (figures[i].isFinished()
                    && figures[i + 1].isFinished()
                    && figures[i + 2].isFinished()
                    && figures[i + 3].isFinished()) {
                return Optional.of(figures[i].getOwner());
            }
        }
        return Optional.empty();
    }

    protected void setFinishedFigures() {
        for (int i = 0; i < players.length; i++) {
            for (int j = 4 * i + 3; j >= 4 * i; j--) {
                Optional<Integer> figureIndex = figureOnHouseField(j);
                if (!figureIndex.isPresent()) {
                    break;
                }
                figures[figureIndex.get()].setFinished();
            }
        }
    }

    // check which figure is on the normal field
    protected Optional<Integer> figureOnField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && figures[i].isOnField()) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    // check which figure is on the house field
    protected Optional<Integer> figureOnHouseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber
                    && (figures[i].isInHouse() || figures[i].isFinished())) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    protected boolean baseOfCurrentPlayerIsEmpty() {
        int firstOwnedFigureIndex = currentPlayerIndex * 4;
        int lastOwnedFigureIndex = firstOwnedFigureIndex + 4;
        for (int i = firstOwnedFigureIndex; i < lastOwnedFigureIndex; i++) {
            if (figures[i].isInBase()) {
                return false;
            }
        }
        return true;
    }

    protected int getNumberOfAllowedTries() {
        int numberOfFiguresInBase = 0;
        int numberOfFinishedFigures = 0;

        for (int i = currentPlayer.getIndexOfFirstFigure();
                i < currentPlayer.getIndexOfLastFigure();
                i++) {
            if (figures[i].isInBase()) {
                numberOfFiguresInBase++;
            } else if (figures[i].isFinished()) {
                numberOfFinishedFigures++;
            }
        }
        if (4 == numberOfFiguresInBase + numberOfFinishedFigures) {
            return 3;
        } else {
            return 1;
        }
    }

    // move a figure out of base
    public void moveOutOfBase(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];
        int firstField = 10 * figureToBeMoved.color;
        Optional<Integer> figureOnFirstField = figureOnField(firstField);

        if (figureOnFirstField.isPresent()) {
            moveToBase(figureOnFirstField.get());
        }
        figureToBeMoved.setOnField();
        figureToBeMoved.setField(firstField, 0);
    }

    public void setNewCurrentPlayerIfNecessary() {
        if (6 == randomNumber) {
            return;
        }
        currentPlayerIndex = ++currentPlayerIndex % 4;
        currentPlayer = players[currentPlayerIndex];
    }

    private boolean generateRandomNumber() {
        Dice dice = new LaPlaceDice();
        if (getPlayerStateOfCurrentPlayer() == 1) {
            dice = new LoadedDice();
        }

        int allowedTries = getNumberOfAllowedTries();
        int tries = 0;
        do {
            randomNumber = dice.roll();
            tries++;
        } while (tries < allowedTries && randomNumber != 6);
        return randomNumber == 6 || allowedTries != 3;
    }
}
