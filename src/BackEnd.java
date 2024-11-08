package src;

public class BackEnd {
    Figure[] figures;
    static int randomNumber;

    private Player[] players;
    private Player currentPlayer;
    private int currentPlayerIndex;

    Logger LOGGER = LoggerFactory.getLoggerInstance();
    
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
        int indexOfFirstFigure = currentPlayer.getIndexOfFirstFigure();
        int indexOfLastFigure = currentPlayer.getIndexOfLastFigure();

        if(!baseOfCurrentPlayerIsEmpty()){
            if(figures[figureOnStartfield].getOwner().equals(currentPlayer.getName())){
                moveFigure(figureOnStartfield);
            } else if (randomNumber == 6) {
                for(int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
                    if (figures[i].isInBase()) {
                        moveFigure(i);
                        break;
                    }
                }
            }
        }
        else {
            for (int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
                if(beatPossible(i)) {
                    moveFigure(i);
                    return;
                }
            }
            for(int i = indexOfFirstFigure; i < indexOfLastFigure; i++) {
                if (figures[i].isMovable()) {
                    moveFigure(i);
                    return;
                }
            }
        }
    }

    //move the given figure by the given number
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

    private void moveOnField(int figureNumber) {
        Figure figureToBeMoved = figures[figureNumber];
        int figureColor = figureToBeMoved.color;

        //store the old and new field-number in local variables
        int numberOld = figureToBeMoved.getField();
        int numberNew = numberOld + randomNumber;

        if (numberNew > 39){
            numberNew -= 40;
        }

        // Figure is about to enter their house.
        boolean goToHouse = 39 - figureToBeMoved.getProgress() < randomNumber;

        if(!goToHouse) {
            //move the figure, if the new field is free
            if(figureOnField(numberNew) == -1) {
                figureToBeMoved.movebyValue(randomNumber);
            } else {
                //move the figure, and move the figure before on the field
                //to the base
                if (figures[figureOnField(numberNew)].getOwner() != figureToBeMoved.getOwner()){
                    moveToBase(figureOnField(numberNew));
                    figureToBeMoved.setField(numberNew, randomNumber);
                } else {
                    //perform the moveFigure-method with the figure,
                    //standing on the field th figure at the moment wants
                    //to move, and the same stepLength
                    moveFigure(figureOnField(numberNew));
                }
            }
        }
        else {
            int positionInHouse = randomNumber - (39 - figureToBeMoved.getProgress()) - 1;
            //Move would exceed fields in house
            if (positionInHouse > 4) return;

            for (int i = 0; i < positionInHouse; i++) {
                if (figureOnField(figureToBeMoved.color * 4 + i) != -1) return;
            }
            figureToBeMoved.setInHouse();
            figureToBeMoved.setField(positionInHouse, randomNumber);
        }
    }

    //move figure in the house by the given value
    private void moveInHouse(int figureNumber){
        Figure figureToBeMoved = figures[figureNumber];

        int newField = figureToBeMoved.getField() + randomNumber;
        int maxField = figureToBeMoved.color * 4 + 4    ;

        if (maxField < newField) {
            LOGGER.info("Move would exceed the number of fields in the house. Aborting move...");
            return;
        }

        for (int i = figureToBeMoved.getField(); i <= newField; i++){
            if (figureOnHouseField(i) != -1){
                LOGGER.info("Figure would have to jump over other figures on field: " + i+ " in the house, which is forbidden. Aborting move...");
                return;
            }
        }

        // Finally we can move the figure to its new position.
        figureToBeMoved.movebyValue(randomNumber);
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

        if (39 - figureToBeMoved.getProgress() < randomNumber) {
            return false;
        }

        if (figureOnField(newField) == -1) {
            return false;
        }
        return figures[figureOnField(newField)].getOwner() != figureToBeMoved.getOwner();
    }

    //move the given figure to the base
    public void moveToBase(int figureNumber){
        Figure figureToBeMoved = figures[figureNumber];
        figureToBeMoved.setInBase();
        figureToBeMoved.setField(figureNumber, randomNumber);
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
        figureToBeMoved.setField(firstField, 0);
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
