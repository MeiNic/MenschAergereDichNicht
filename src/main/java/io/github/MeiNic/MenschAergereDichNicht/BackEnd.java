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
import io.github.MeiNic.MenschAergereDichNicht.stateMachine.Event;
import io.github.MeiNic.MenschAergereDichNicht.stateMachine.StateMachine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class BackEnd {
    public final Figure[] figures;

    public int randomNumber;

    protected final Player[] players;
    protected Player currentPlayer;

    public StateMachine stateMachine;

    final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public BackEnd(String[] names, int numberOfHumanPlayers, boolean fillWithBots) {
        stateMachine = new StateMachine();

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

        currentPlayer = players[0];
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
        Optional<Figure> figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        boolean ownFigureOnStartfield =
                figureOnStartfield.isPresent()
                        && figureOnStartfield.get().getOwner().equals(currentPlayer.getName());

        if (ownFigureOnStartfield && !baseOfCurrentPlayerIsEmpty()) {
            Figure figure = figureOnStartfield.get();
            int goalField = figure.getField() + randomNumber;
            while (figureOnField(goalField).isPresent()
                    && figureOnField(goalField).get()
                            .getOwner()
                            .equals(currentPlayer.getName())) {
                figure = figureOnField(goalField).get();
                goalField = figure.getField() + randomNumber;
            }
            figure.enablePlacement();
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
        Figure[] finishedFigures = getFinishedFigures(currentPlayer);
        for (int i = currentPlayer.getIndexOfFirstFigure();
                i < currentPlayer.getIndexOfLastFigure();
                i++) {
            Figure figure = figures[i];
            boolean isMovable =
                    !(Arrays.asList(finishedFigures).contains(figure) || figure.isInBase());
            if (isMovable) {
                figure.enablePlacement();
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
        Optional<Figure> figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        int indexOfFirstFigure = currentPlayer.getIndexOfFirstFigure();
        int indexOfLastFigure = currentPlayer.getIndexOfLastFigure();

        // LOGGER.info("Bot " + currentPlayer.getName() + " rolled a " + randomNumber);
        if (!baseOfCurrentPlayerIsEmpty()) {
            if (figureOnStartfield.isPresent()
                    && figureOnStartfield.get().getOwner().equals(currentPlayer.getName())) {
                moveFigure(figureOnStartfield.get());
                return;
            } else if (randomNumber == 6) {
                for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
                    if (figures[i].isInBase()) {
                        moveFigure(figures[i]);
                        return;
                    }
                }
            }
        }
        for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
            if (beatIsPossible(figures[i])) {
                moveFigure(figures[i]);
                return;
            }
        }
        for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
            if (moveSensible(figures[i])) {
                moveFigure(figures[i]);
                return;
            }
        }
    }

    // move the given figure by the given number
    public void moveFigure(Figure figure) {
        switch (figure.getState()) {
            case IN_HOUSE -> moveInHouse(figure);
            case IN_BASE -> {
                if (randomNumber == 6) moveOutOfBase(figure);
            }
            case ON_FIELD -> moveOnField(figure);
        }
    }

    protected void moveOnField(Figure figureToBeMoved) {
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
                if (!figureOnField(numberNew).get().getOwner().equals(figureToBeMoved.getOwner())) {
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
    protected void moveInHouse(Figure figureToBeMoved) {
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

        Figure otherFigure = figureOnField(newField).get();
        return !thisFigure.getOwner().equals(otherFigure.getOwner());
    }

    protected boolean moveSensible(Figure figure) {
        switch (figure.getState()) {
            case IN_HOUSE -> {
                int newField = figure.getField() + randomNumber;
                int maxField = figure.color * 4 + 4;
                return maxField > newField;
            }
            case ON_FIELD -> {
                int color = figure.color;
                for (int i = color * 4; i < (color + 1) * 4; i++) {
                    if (figure.getProgress() < figures[i].getProgress()) return false;
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    // move the given figure to the base
    public void moveToBase(Figure figure) {
        figure.setInBase();
        figure.setField(figure.getIndex(), randomNumber);
    }

    public Optional<String> getNameOfWinner() {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (getFinishedFigures(player).length == 4) {
                return Optional.of(player.getName());
            }
        }

        return Optional.empty();
    }

    protected Figure[] getFinishedFigures(Player player) {
        int lastHouseFieldIndex = player.getPlayerIndex() * 4 + 3;
        int firstHouseFieldIndex = player.getPlayerIndex() * 4;

        ArrayList<Figure> finishedFigures = new ArrayList<>();

        for (int i = lastHouseFieldIndex; i >= firstHouseFieldIndex; i--) {
            Optional<Figure> figure = figureOnHouseField(i);
            if (!figure.isPresent()) {
                break;
            }
            finishedFigures.add(figure.get());
        }

        return finishedFigures.toArray(new Figure[] {});
    }

    // check which figure is on the normal field
    protected Optional<Figure> figureOnField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && figures[i].isOnField()) {
                return Optional.of(figures[i]);
            }
        }
        return Optional.empty();
    }

    // check which figure is on the house field
    protected Optional<Figure> figureOnHouseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && (figures[i].isInHouse())) {
                return Optional.of(figures[i]);
            }
        }
        return Optional.empty();
    }

    protected boolean baseOfCurrentPlayerIsEmpty() {
        int firstOwnedFigureIndex = currentPlayer.getIndexOfFirstFigure();
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

        for (int i = currentPlayer.getIndexOfFirstFigure();
                i < currentPlayer.getIndexOfLastFigure();
                i++) {
            if (figures[i].isInBase()) {
                numberOfFiguresInBase++;
            }
        }
        if (4 == numberOfFiguresInBase + getFinishedFigures(currentPlayer).length) {
            return 3;
        } else {
            return 1;
        }
    }

    // move a figure out of base
    public void moveOutOfBase(Figure figureToBeMoved) {
        int firstField = 10 * figureToBeMoved.color;
        Optional<Figure> figureOnFirstField = figureOnField(firstField);

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
        currentPlayer = players[(currentPlayer.getPlayerIndex() + 1) % 4];
        switch (getPlayerStateOfCurrentPlayer()) {
            case 0 -> stateMachine.handleEvent(Event.TURN_COMPLETED_ENTER_PLAYER);
            case 1 -> stateMachine.handleEvent(Event.TURN_COMPLETED_ENTER_BOT);
            default -> setNewCurrentPlayerIfNecessary();
        }
    }

    protected boolean generateRandomNumber() {
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
