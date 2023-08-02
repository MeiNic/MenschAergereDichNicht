import java.util.Random;

public class BackEnd {
    enum PlayerState {
        IS_PLAYER,
        IS_BOT,
        NOTHING,
    }
    record Player(String name, PlayerState status){
        int getPlayerState(){
            if (status == PlayerState.IS_PLAYER){
                return 0;
            } else if (status == PlayerState.IS_BOT) {
                return 1;
            }
            return -1;
        }
    }
    Figure[] figures;
    Landingpage startpage;
    final Player[] players;
    int activePlayer;
    GameBoardGui gui;
    int randomNumber;
    WinWindow winner;

    BackEnd(Landingpage landingpage) {
        figures = new Figure[16];
        for (int i = 0; i < figures.length; i++) {
            figures[i] = new Figure(i, 0);
        }
        for (int i = 0; i < 4; i++) {
            figures[i + 4].color = 1;
            figures[i + 8].color = 2;
            figures[i + 12].color = 3;
        }

        activePlayer = 0;
        randomNumber = 0;
        players = new Player[4];

        //progress input from landingpage
        startpage = landingpage;
        boolean bots = startpage.getBotsSelection();
        for (int i = 0; i < 4; i++){
            if (i <= startpage.getPlayerNumber()){
                players[i] = new Player(startpage.getNames()[i], PlayerState.IS_PLAYER);
            }else {
                if (bots){
                    players[i] = new Player(startpage.getNames()[i], PlayerState.IS_BOT);
                }else {
                    players[i] = new Player(startpage.getNames()[i], PlayerState.NOTHING);
                }
            }
        }
        gui = new GameBoardGui(players[0].name, this);
    }

    //progress a dice input
    public void playerMove() {
	Dice dice = new LaPlaceDice();
	
	int allowedTries = getNumberOfAllowedTries();
	int tries = 0;
	
	do {
	    randomNumber = dice.roll();
	    tries++;
	} while (tries < allowedTries && randomNumber != 6);
	
	if (randomNumber != 6 && allowedTries == 3) {
	    nextMove();
	    return;
	}
        
        gui.displayResult(randomNumber);
	
        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(activePlayer * 10);
        boolean ownFigureOnStartfield = false;
	
        if (figureOnStartfield != 99 && figures[figureOnStartfield].color == activePlayer){
	    ownFigureOnStartfield = true;
        }

        if (ownFigureOnStartfield && !isBaseEmpty(activePlayer)){
            figures[figureOnStartfield].enablePlacement();
        } else if (!isBaseEmpty(activePlayer) && randomNumber == 6) {
	    int firstOwnedFigure = activePlayer * 4;
	    int lastOwnedFigure = firstOwnedFigure + 4;
	    
	    for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
		if (figures[i].isInBase()) {
		    figures[i].enablePlacement();
		}
	    }
	} else {
	    playerMoveOnField();
	}
	
        gui.setPromptValues();
    }


    //part of the playerMove-method - don't use out of it
    private void playerMoveOnField() {
        boolean beatsPossible = false;
	
	int firstOwnedFigure = activePlayer * 4;
	int lastOwnedFigure = firstOwnedFigure + 4;
	
        for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
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
	for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
	    if (figures[i].isMovable()) {
		figures[i].enablePlacement();
	    }
        }
    }

    public void performUserChoice(){
        //removing the place options on all figures
        for (Figure figure : figures) {
            figure.disablePlacement();
        }
        gui.removePrompt();
        gui.replaceFigures();

	displayWinWindowIfNecessary();
	
        //trigger new move in fontEnd
        nextMove();
    }

    //bot-move on the "normal" fields
    private void botMove(){
	Dice dice = new LoadedDice();

	int allowedTries = getNumberOfAllowedTries();
	int tries = 0;

	int firstOwnedFigure = activePlayer * 4;
	int lastOwnedFigure = firstOwnedFigure + 4;

	do {
	    randomNumber = dice.roll();
	    tries++;
	} while (tries < allowedTries && randomNumber != 6);

	if (randomNumber != 6 && allowedTries == 3) {
	    nextMove();
	    return;
	}
        
        gui.displayResult(randomNumber);

        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(activePlayer * 10);
        boolean ownFigureOnStartfield = false;
	
        if (figureOnStartfield != 99 && figures[figureOnStartfield].color == activePlayer) {
	    ownFigureOnStartfield = true;
        }

        if (ownFigureOnStartfield  && !isBaseEmpty(activePlayer)){
            moveFigure(figureOnStartfield);
        } else if (!isBaseEmpty(activePlayer) && randomNumber == 6) {
	    for (int i = firstOwnedFigure; i < lastOwnedFigure; i++){
                if (figures[i].isInBase()){
                    moveFigure(i);
                    break;
                }
            }
        } else {
            boolean beatsPossible = false;
            for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++) {
                if (beatPossible(i)) {
                    moveFigure(i);
                    beatsPossible = true;
                    break;
                }
            }
            //make user figure chooser for all figures of the player
            if (!beatsPossible){
                for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
                    if (figures[i].isMovable()) {
                        moveFigure(i);
                        break;
                    }
                }
            }
        }
        gui.replaceFigures();
        displayWinWindowIfNecessary();
        nextMove();
    }

    private void displayWinWindowIfNecessary() {
	setFinishedFigures();

	String nameOfWinner = getWinningPlayer();
	if (nameOfWinner == null) {
	    return;
	}
	winner = new WinWindow(nameOfWinner);
	gui.setVisible(false);
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
	int numberOld = figureToBeMoved.field;
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
	if ((numberOld < figureColor * 10 && numberNew >= figureColor * 10)
	    || (figureColor == 0 && numberOld > 34 && numberNew >= 0)){
	    goToHouse = true;
	}
	
	//move the figure, if the new field is free
	if (!goToHouse && figureOnField(numberNew) == 99){
	    figureToBeMoved.field = numberNew;
	    return;
	}

	if (!goToHouse) {
	    //move the figure, and move the figure before on the field
	    //to the base
	    if (figures[figureOnField(numberNew)].color != figureColor){
		moveToBase(figureOnField(numberNew));
	        figureToBeMoved.field = numberNew;
	    } else {
		//perform the moveFigure-method with the figure,
		//standing on the field th figure at the moment wants
		//to move, and the same stepLength
		moveFigure(figureOnField(numberNew));
	    }
	    return;
	}

	int toMove = randomNumber;
	int figurePosition = figureToBeMoved.field;
	
	//unifying the values for cleaner code
	if (figureColor == 0){
	    figurePosition -= 30;
	} else if (figureColor == 2) {
	    figurePosition -= 10;
	} else if (figureColor == 3) {
	    figurePosition -= 20;
	}
	
	//move figure in front of the base
	while (toMove > 0 && figurePosition <= 9){
	    figurePosition++;
	    toMove--;
	}

	// Don't move figure if entering the house is not possible.
	if (0 < toMove && toMove < 5) {
	    for (int i = 0; i <= toMove; i++) {
		if (figureOnField(i + (figureColor * 4)) != 99) {
		    return;
		}
	    }
	}

	// Move figure into house.
	toMove--;
        figureToBeMoved.setInHouse();
        figureToBeMoved.field = figureColor * 4;
	
	if (toMove > 0){
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

	int newField = figureToBeMoved.field + randomNumber;
	int maxField = (figureToBeMoved.color * 4) + 4;

	if (maxField < newField) {
	    // Move would exceed the number of available fields in the
	    // game.
	    return;
	}

	for (int i = figureToBeMoved.field; i < 4; i++){
	    if (figureOnField(i) != 99){
		// Figure would have to jump over other figures in
		// the house, which is not allowed.
		return;
	    }
	}

	// Finally we can move the figure to its new position.
	figureToBeMoved.field += randomNumber;
    }

    //check if a beat is possible
    private boolean beatPossible(int figureNumber) {
	Figure figureToBeMoved = figures[figureNumber];

	if (!figureToBeMoved.isOnField()) {
	    return false;
	}
	
	int figureColor = figureToBeMoved.color;
	int oldField = figureToBeMoved.field;
	int newField = oldField + randomNumber;

	if (newField > 39) {
	    newField -= 40;
	}

	if ((oldField < figureColor * 10 && figureColor * 10 <= newField)
	    || (figureColor == 0 && 34 < oldField && 0 <= newField)) {
	    return false;
	}

	if (figureOnField(newField) == 99) {
	    return false;
	}

	boolean beatableFigureIsOwnedByOtherPlayer =
	    figures[figureOnField(newField)].color != figureColor;

	if (beatableFigureIsOwnedByOtherPlayer) {
	    return true;
	}
	
	return false;
    }

    //move the given figure to the base
    public void moveToBase(int figureNumber){
	Figure figureToBeMoved = figures[figureNumber];

        figureToBeMoved.setInBase();
        figureToBeMoved.field = figureNumber;
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
    private String getWinningPlayer(){
	for (int i = 0; i < 16; i += 4) {
	    if (figures[i].isFinished() && figures[i + 1].isFinished() && figures[i + 2].isFinished() && figures[i + 3].isFinished()) {
		return players[i / 4].name;
	    }
	}
	return null;
    }

    //check all figures if they are finished
    private void setFinishedFigures() {
        for (int i = figures.length - 1; 0 <= i; i--) {
            int figureNumber = figureOnHouseField(i);

	    if (figureNumber == 99) {
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
	    if (nextFigureNumber == 99) {
		continue;
	    }
	    
	    Figure nextFigureInHouse = figures[nextFigureNumber];
	    if (nextFigureInHouse.isFinished()) {
		currentFigure.setFinished();
	    }
        }
    }

    //check which figure is on the normal field
    public int figureOnField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].field == fieldNumber && figures[i].isOnField()) {
                return i;
            }
        }
        return 99;
    }

    //check which figure is on the house field
    public int figureOnHouseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].field == fieldNumber && figures[i].isInHouse()) {
		return i;
            }
        }
        return 99;
    }

    public int figureOnBaseField(int fieldNumber) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].field == fieldNumber && figures[i].isInBase()) {
		return i;
            }
        }
        return 99;
    }

    //check if base is empty (argument between 0 & 3)
    private boolean isBaseEmpty(int playerNumber) {
	int firstOwnedFigure = playerNumber * 4;
	int lastOwnedFigure = firstOwnedFigure + 4;
	
        for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
            if (figures[i].isInBase()) {
		return false;
            }
        }
        return true;
    }
    
    //check if all figures are in the base
    private boolean isBaseFull(int playerNumber) {
	int firstOwnedFigure = playerNumber * 4;
	int lastOwnedFigure = firstOwnedFigure + 4;
	
        for(int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
            if (!figures[i].isInBase()) {
		return false;
            }
        }
        return true;
    }

    private int getNumberOfAllowedTries() {
	int numberOfFiguresInBase = 0;
	int numberOfFinishedFigures = 0;
	
	int firstOwnedFigure = activePlayer * 4;
	int lastOwnedFigure = firstOwnedFigure + 4;
	
	for (int i = firstOwnedFigure; i < lastOwnedFigure; i++) {
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

	if (figureOnFirstField != 99) {
	    moveToBase(figureOnFirstField);
	}

	figureToBeMoved.field = firstField;
	figureToBeMoved.setOnField();
    }

    //next player
    private void nextMove() {
	if (randomNumber != 6) {
	    activePlayer = (activePlayer++) % 4;
	}
	int playerState = players[activePlayer].getPlayerState();

	if (playerState == 1) {
	    gui.setBotAdvice();

	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		throw new RuntimeException(e);
	    }

	    botMove();
	} else if (playerState == 0) {
	    gui.setActivePlayer();
	} else {
	    nextMove();
	}
    }
}
