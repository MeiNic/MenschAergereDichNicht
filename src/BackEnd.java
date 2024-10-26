package src;

public class BackEnd {
    Figure[] figures;
    int randomNumber;

    private Player[] players;
    private Player currentPlayer;
    private int currentPlayerIndex;
    
    BackEnd(String[] names, int numberOfHumanPlayers, boolean fillWithBots) {
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

    //progress a dice input
    public boolean playerMove() {
        Dice dice = new LaPlaceDice();
        int allowedTries = getNumberOfAllowedTries();
        int tries = 0;

        do {
            randomNumber = dice.roll();
            tries++;
        } while (tries < allowedTries && randomNumber != 6);

        if (randomNumber != 6 && allowedTries == 3) {
            return false;
        }
        
        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        boolean ownFigureOnStartfield = false;

        if (figureOnStartfield != -1 && figures[figureOnStartfield].getOwner() == currentPlayer.getName()){
            ownFigureOnStartfield = true;
        }

        if (ownFigureOnStartfield && !baseOfCurrentPlayerIsEmpty()){
            figures[figureOnStartfield].enablePlacement();
        } else if (!baseOfCurrentPlayerIsEmpty() && randomNumber == 6) {
            for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
                if (figures[i].isInBase()) {
                    figures[i].enablePlacement();
                }
            }
        } else {
            playerMoveOnField();
        }
        return true;
    }

    //part of the playerMove-method - don't use out of it
    private void playerMoveOnField() {
        boolean beatsPossible = false;

        for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
            if (beatPossible(i)) {
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
        for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
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

    //bot-move on the "normal" fields
    public void botMove(){
        Dice dice = new LoadedDice();

        int allowedTries = getNumberOfAllowedTries();
        int tries = 0;

        do {
            randomNumber = dice.roll();
            tries++;
        } while (tries < allowedTries && randomNumber != 6);

        if (randomNumber != 6 && allowedTries == 3) {
            return;
        }

        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(currentPlayer.getIndexOfStartField());
        boolean ownFigureOnStartfield = false;

        if (figureOnStartfield != -1 && figures[figureOnStartfield].getOwner() == currentPlayer.getName()) {
            ownFigureOnStartfield = true;
        }

        if (ownFigureOnStartfield  && !baseOfCurrentPlayerIsEmpty()){
            moveFigure(figureOnStartfield);
        } else if (!baseOfCurrentPlayerIsEmpty() && randomNumber == 6) {
            for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++){
                if (figures[i].isInBase()){
                    moveFigure(i);
                    break;
                }
            }
        } else {
            boolean beatsPossible = false;
            for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
                if (beatPossible(i)) {
                    moveFigure(i);
                    beatsPossible = true;
                    break;
                }
            }
            //make user figure chooser for all figures of the player
            if (!beatsPossible){
                for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
                    if (figures[i].isMovable()) {
                        moveFigure(i);
                        break;
                    }
                }
            }
        }
    }

    //move the given figure by the given number
    public void moveFigure(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];
        int figureColor = figureToBeMoved.color;

        if (figureToBeMoved.isInBase() && randomNumber == 6) {
            moveOutOfBase(figureNumber);
            return;
        }

        // Figure is not movable.
        if (!figureToBeMoved.isMovable()) {
            return;
        }

        //store the old and new field-number in local variables
        int numberOld = figureToBeMoved.getField();
        int numberNew = numberOld + randomNumber;

        if (numberNew > 39){
            numberNew -= 40;
        }

        // Figure is moving in their house.
        if (figureToBeMoved.isInHouse()) {
            moveInHouse(figureNumber);
            return;
        }

        // Figure is about to enter their house.
        boolean goToHouse = false;
        if ((numberOld < figureColor * 10 && numberNew >= figureColor * 10) || (figureColor == 0 && numberOld > 34 && numberNew >= 0)){
            goToHouse = true;
        }

        //move the figure, if the new field is free
        if (!goToHouse && figureOnField(numberNew) == -1){
            figureToBeMoved.setField(numberNew);
            return;
        }

        if (!goToHouse) {
            //move the figure, and move the figure before on the field
            //to the base
            if (figures[figureOnField(numberNew)].getOwner() != figureToBeMoved.getOwner()){
                moveToBase(figureOnField(numberNew));
                figureToBeMoved.setField(numberNew);
            } else {
                //perform the moveFigure-method with the figure,
                //standing on the field th figure at the moment wants
                //to move, and the same stepLength
                moveFigure(figureOnField(numberNew));
            }
            return;
        }

        int toMove = randomNumber;
        int figurePosition = figureToBeMoved.getField();

        //unifying the values for cleaner code
        switch (figureColor){
            case 0 -> figurePosition -= 30;
            case 2 -> figurePosition -= 10;
            case 3 -> figurePosition -= 20;
        }

        //move figure in front of the base
        while (toMove > 0 && figurePosition <= 9){
            figurePosition++;
            toMove--;
        }

        // Don't move figure if entering the house is not possible.
        if (0 < toMove && toMove < 5) {
            for (int i = 0; i <= toMove; i++) {
                if (figureOnField(i + (figureColor * 4)) != -1) {
                    return;
                }
            }
        }

        // Move figure into house.
        toMove--;
        figureToBeMoved.setInHouse();
        figureToBeMoved.setField(figureColor * 4);

        if (toMove > 0) {
            randomNumber = toMove;
            moveInHouse(figureNumber);
        }
    }

    //move figure in the house by the given value
    private void moveInHouse(int figureNumber){
        Figure figureToBeMoved = figures[figureNumber];
        if (!figureToBeMoved.isInHouse()) {
            return;
        }

        int newField = figureToBeMoved.getField() + randomNumber;
        int maxField = (figureToBeMoved.color * 4) + 4;

        if (maxField < newField) {
            // Move would exceed the number of available fields in the game.
            return;
        }

        for (int i = figureToBeMoved.getField(); i < 4; i++){
            if (figureOnField(i) != -1){
                // Figure would have to jump over other figures in
                // the house, which is not allowed.
                return;
            }
        }

        // Finally we can move the figure to its new position.
        figureToBeMoved.addField(randomNumber);
    }

    //check if a beat is possible
    private boolean beatPossible(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];

        if (!figureToBeMoved.isOnField()) {
            return false;
        }

        int figureColor = figureToBeMoved.color;
        int oldField = figureToBeMoved.getField();
        int newField = oldField + randomNumber;

        if (newField > 39) {
            newField -= 40;
        }

        if ((oldField < figureColor * 10 && figureColor * 10 <= newField) || (figureColor == 0 && 34 < oldField && 0 <= newField)) {
            return false;
        }

        if (figureOnField(newField) == -1) {
            return false;
        }

        boolean beatableFigureIsOwnedByOtherPlayer = figures[figureOnField(newField)].getOwner() != figureToBeMoved.getOwner();

        if (beatableFigureIsOwnedByOtherPlayer) {
            return true;
        }

        return false;
    }

    //move the given figure to the base
    public void moveToBase(int figureNumber){
        Figure figureToBeMoved = figures[figureNumber];
        figureToBeMoved.setInBase();
        figureToBeMoved.setField(figureNumber);
    }

    //return to which player the given figure belongs to
    private int giveColor(int figureNumber){
        if (figureNumber < 4){
            return 0;
        } else if (figureNumber < 8) {
            return 1;
        } else if (figureNumber < 12) {
            return 2;
        } else {
            return 3;
        }
    }

    //check which player has won
    public String getNameOfWinner(){
        setFinishedFigures();

        for (int i = 0; i < 16; i += 4) {
            if (figures[i].isFinished() && figures[i + 1].isFinished() && figures[i + 2].isFinished() && figures[i + 3].isFinished()) {
                return figures[i].getOwner();
            }
        }
        return null;
    }

    //check all figures if they are finished
    private void setFinishedFigures() {
        for (int i = figures.length - 1; 0 <= i; i--) {
            int figureNumber = figureOnHouseField(i);

            if (figureNumber == -1) {
                continue;
            }

            Figure currentFigure = figures[figureNumber];

            // If figure is on the last field in the house, this one
            // is definitely finished and does not have to be moved
            // any further.
            if (i == 15 || i == 11 || i == 7 || i == 3) {
                currentFigure.setFinished();
                continue;
            }

            int nextFigureNumber = figureOnHouseField(i + 1);
            if (nextFigureNumber == -1) {
                continue;
            }

            Figure nextFigureInHouse = figures[nextFigureNumber];
            if (nextFigureInHouse.isFinished()) {
                currentFigure.setFinished();
            }
        }
    }

    //check which figure is on the normal field
    private int figureOnField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && figures[i].isOnField()) {
                return i;
            }
        }
        return -1;
    }

    //check which figure is on the house field
    private int figureOnHouseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && ( figures[i].isInHouse()) || figures[i].isFinished()) {
                return i;
            }
        }
        return -1;
    }

    public int figureOnBaseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].getField() == fieldNumber && figures[i].isInBase()) {
                return i;
            }
        }
        return -1;
    }

    private boolean baseOfCurrentPlayerIsEmpty() {
        int firstOwnedFigureIndex = currentPlayer.getPlayerIndex() * 4;
        int lastOwnedFigureIndex = firstOwnedFigureIndex + 4;

        for (int i = firstOwnedFigureIndex; i < lastOwnedFigureIndex; i++) {
            if (figures[i].isInBase()) {
                return false;
            }
        }
        return true;
    }

    private int getNumberOfAllowedTries() {
        int numberOfFiguresInBase = 0;
        int numberOfFinishedFigures = 0;

        for (int i = currentPlayer.getIndexOfFirstFigure(); i < currentPlayer.getIndexOfLastFigure(); i++) {
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

    //move a figure out of base
    public void moveOutOfBase(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];
        int firstField = 10 * figureToBeMoved.color;
        int figureOnFirstField = figureOnField(firstField);

        if (figureOnFirstField != -1) {
            moveToBase(figureOnFirstField);
        }
        figureToBeMoved.setOnField();
        figureToBeMoved.setField(firstField);
    }

    public void setNewCurrentPlayerIfNecessary() {
        if (currentPlayerIsAllowedToRollTheDiceAgain()) {
            return;
        }

        currentPlayerIndex = ++currentPlayerIndex % 4;
        currentPlayer = players[currentPlayerIndex];
    }

    private boolean currentPlayerIsAllowedToRollTheDiceAgain() {
        return 6 == randomNumber;
    }
}
