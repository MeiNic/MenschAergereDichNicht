import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;
    int activePlayer;
    GameBoardGui gui;
    int randomNumber;
    boolean nextMove;

    BackEnd(Landingpage landingpage) {
        figures = new Figure[16];
        for (int i = 0; i < figures.length; i++) {
            figures[i] = new Figure(i, 0);
        }
        for (int i = 0; i < 4; i++) {
            figures[i + 4].setColor(1);
            figures[i + 8].setColor(2);
            figures[i + 12].setColor(3);
        }

        activePlayer = 0;
        randomNumber = 0;
        nextMove = true;
        usernames = new String[4];

        //progress input from landingpage
        startpage = landingpage;
        usernames = startpage.getNames();

        gui = new GameBoardGui(usernames[0], figures, this);
    }
    //generate random number (copy from frontend)
    private int submitRandomNumber(){
        Random rand = new Random();
        return 1 + rand.nextInt(6);
    }

    //progress a dice input
    public void playerMove() throws InterruptedException {
        //set the PlaceOption in all figures to false
        for (int i = 0; i < figures.length; i++){
            figures[i].setPlaceOption(false);
        }

        //Generate new randomNumber and show it on the gui
        randomNumber = submitRandomNumber();
        gui.displayResult(randomNumber);

        //if user is allowed to roll the dice three time operate this option
        int counter = 0;
        if (threeTimesAllowed(activePlayer)){
            while (counter < 3 && randomNumber != 6){
                randomNumber = submitRandomNumber();
                gui.displayResult(randomNumber);
                counter++;
            }
            if (randomNumber != 6){
                if (activePlayer == 3) {
                    activePlayer = 0;
                } else {
                    activePlayer++;
                }
                //trigger new move in fontEnd
                gui.setActivePlayer(usernames[activePlayer]);
                return;
            }
        }

        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(activePlayer * 10);
        if (randomNumber == 6) {

            //check if an own figure is on the startfield
            if (figures[figureOnStartfield].getColor() == activePlayer) {
                //own figure is on the startfield
                moveFigure(figureOnStartfield, randomNumber);
            }
            //if base not empty move a player out of base
            else if (!isBaseEmpty(activePlayer)) {
                if (figures[activePlayer].isInBase()) {
                    moveOutOfBase(activePlayer);
                } else if (figures[activePlayer + 1].isInBase()) {
                    moveOutOfBase(activePlayer + 1);
                } else if (figures[activePlayer + 2].isInBase()) {
                    moveOutOfBase(activePlayer + 2);
                } else {
                    moveOutOfBase(activePlayer + 3);
                }
            } else {
                playerMoveOnField();
            }
            //display all changes in the frontEnd
            gui.replaceFigures(figures);

            while (!nextMove){
                Thread.sleep(500);
            }
            //trigger new move in frontEnd
            gui.setActivePlayer(usernames[activePlayer]);
        }
        else {

            //check if an own figure is on the startfield
            if (figures[figureOnStartfield].getColor() == activePlayer) {
                //own figure is on the startfield
                moveFigure(figureOnStartfield, randomNumber);
            }else {
                playerMoveOnField();
            }
            //display all changes in the frontEnd
            gui.replaceFigures(figures);

            while (!nextMove){
                Thread.sleep(500);
            }

            if (activePlayer == 3) {
                activePlayer = 0;
            } else {
                activePlayer++;
            }
            //trigger new move in fontEnd
            gui.setActivePlayer(usernames[activePlayer]);
        }
    }


    //part of the playerMove-method - don't use out of it
    private void playerMoveOnField() {
        //check with how much figures can do a beat
        int beatsPossible = 0;
        for (int i = 0; i < 4; i++) {
            if (beatPossible(activePlayer * 4 + i, randomNumber)) {
                beatsPossible++;
            }
        }

        //check if one or more beats are possible
        if (beatsPossible > 0) {
            if (beatsPossible == 1) {
                //move the figure, where the beat is possible
                for (int i = 0; i < 4; i++) {
                    int activeFigure = activePlayer * 4 + i;
                    if (beatPossible(activeFigure, randomNumber)) {
                        moveFigure(activeFigure, randomNumber);
                    }
                }
            } else {
                //add the figure to the chooser
                for (int i = 0; i < 4; i++) {
                    int activeFigure = activePlayer * 4 + i;
                    if (beatPossible(activeFigure, randomNumber)) {
                        figures[activeFigure].setPlaceOption(true);
                    }
                }
                nextMove = false;
                //perform the user choice of the frontEnd
                gui.setUserFigureOption(figures);
            }
        }
        //make user figure chooser for all figures of the player
        else {
            for (int i = 0; i < 4; i++) {
                int activeFigure = activePlayer * 4 + i;
                if (!figures[activeFigure].isFinished()) {
                    figures[activeFigure].setPlaceOption(true);
                }
            }
            nextMove = false;
            //perform the user choice of the frontEnd
            gui.setUserFigureOption(figures);
        }
    }

    public void performUserChoice(int buttonNumber){
        if (buttonNumber == 1){
            for (int i = 0; i < 4; i++){
                if (figures[activePlayer * 4 + i].isPlaceOption()){
                    moveFigure(activePlayer * 4 + i, randomNumber);
                    figures[activePlayer * 4 + i].setPlaceOption(false);
                    break;
                }
            }
            nextMove = true;
        } else if (buttonNumber == 2) {
            for (int i = 0; i < 4; i++){
                if (figures[activePlayer * 4 + i].isPlaceOption()){
                    moveFigure(activePlayer * 4 + i, randomNumber);
                    figures[activePlayer * 4 + i].setPlaceOption(false);
                    break;
                }
            }
            nextMove = true;
        } else if (buttonNumber == 3) {
            for (int i = 0; i < 4; i++){
                if (figures[activePlayer * 4 + i].isPlaceOption()){
                    moveFigure(activePlayer * 4 + i, randomNumber);
                    figures[activePlayer * 4 + i].setPlaceOption(false);
                    break;
                }
            }
            nextMove = true;
        }else {
            for (int i = 0; i < 4; i++){
                if (figures[activePlayer * 4 + i].isPlaceOption()){
                    moveFigure(activePlayer * 4 + i, randomNumber);
                    figures[activePlayer * 4 + i].setPlaceOption(false);
                    break;
                }
            }
            nextMove = true;
        }
    }

    //move the given figure by the given number
    private void moveFigure(int figureNumber, int stepLength) {
        //store the color of the figure in a local variable
        int figureColor = figures[figureNumber].getColor();

        //check if the figure isn't finished and not in the base
        if(!figures[figureNumber].isFinished() && !figures[figureNumber].isInBase()){
            //store the old and new field-number in local variables
            int numberOld = figures[figureNumber].getField();
            int cache = numberOld + stepLength;
            int numberNew = cache;
            if (numberNew > 39){
                numberNew -= 40;
            }

            //check if the figure is on the gamefield
            if (!figures[figureNumber].isInBase()){
                //make some small if-blocks for less code complexity
                boolean goToBase = false;
                if (numberOld < figureColor * 10 && cache >= figureColor * 10){
                    goToBase = true;
                }
                if (figureColor == 0 && numberOld > 34 && numberNew >= 0){
                    goToBase = true;
                }

                //if the figure comes over its startfield -> move the figure in the base
                if (goToBase) {
                    //variable caching some information for the further progress
                    int steplengthInBase = numberNew - figureColor * 10;

                    //check if you don't jump over figures in the base
                    boolean fieldsFree = true;
                    for (int i = 0; i <= steplengthInBase; i++){
                        if (figureOnField(i + figureColor * 4) != 99){
                            fieldsFree = false;
                        }
                    }

                    //progress move only if figure doesn't jump over figures
                    if (fieldsFree){
                        figures[figureNumber].setInHouse(true);
                        numberNew -= figureColor * 10;
                        figures[figureNumber].setField(numberNew);
                    }

                }
                //move the figure, if the new field is free
                else if (figureOnField(numberNew) == 99){
                    figures[figureNumber].setField(numberNew);
                }

                else {
                    //move the figure, and move the figure before on the field to the base
                    if (figures[figureOnField(numberNew)].getColor() != figureColor){
                        moveToBase(figureOnField(numberNew));
                        figures[figureNumber].setField(numberNew);
                    } else {
                        //perform the moveFigure-method with the figure, standing on the field th figure at the moment wants to move, and the same stepLength
                        moveFigure(figureOnField(numberNew), stepLength);
                    }
                }
            }
            else if (numberNew < figures[figureNumber].getColor() * 4 + 4){
                figures[figureNumber].setField(numberNew);
            }
        }
        //if figure is in the base and the step-length is 6 move figure out of base
        else if (figures[figureNumber].isInBase() && stepLength == 6) {
            moveOutOfBase(figureNumber);
        }
    }

    //check if a beat is possible
    private boolean beatPossible(int figureNumber, int stepLength){
        if (!figures[figureNumber].isInBase() && !figures[figureNumber].isInHouse()){
            //store some useful variables
            int numberOld = figures[figureNumber].getField();
            int numberNew = numberOld + stepLength;
            if (numberNew > 39){
                numberNew -= 40;
            }
            int figureColor = figures[figureNumber].getColor();

            //real check, if a figure of another color is standing on the field
            if (figureOnField(numberNew) != 99){
                return false;
            } else if (figures[figureOnField(numberNew)].getColor() != figureColor) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //move the given figure to the base
    private void moveToBase(int figureNumber){
        int figureColor = giveColor(figureNumber);
        int emptyField = 0 + 4 * figureColor;
        while (figureOnField(emptyField) != 99){
            emptyField++;
        }
        figures[figureNumber].setInBase(true);
        figures[figureNumber].setField(emptyField);
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

    //check if a player has won (return true/false)
    private boolean finished() {
        for (int i = 0; i < 4; i++) {
            if (figures[i].isFinished() && figures[i + 1].isFinished() && figures[i + 2].isFinished() && figures[i + 3].isFinished()) {
                return true;
            }
        }
        return false;
    }

    //check which player has won
    private int whoFinished(){
        if (figures[0].isFinished() && figures[1].isFinished() && figures[2].isFinished() && figures[3].isFinished()){
            return 0;            
        } else if (figures[4].isFinished() && figures[5].isFinished() && figures[6].isFinished() && figures[7].isFinished()) {
            return 1;
        } else if (figures[9].isFinished() && figures[9].isFinished() && figures[10].isFinished() && figures[11].isFinished()) {
            return 2;
        } else {
            return 3;
        }
    }

    //check all figures if they are finished
    private void checkFiguresIfFinished() {
        for (int i = figures.length - 1; i >= 0; i--) {
            int cache = figureOnHouseField(i);
            if (cache != 99) {
                if (i == 15 || i == 11 || i == 7 || i == 3) {
                    figures[cache].setFinished(true);
                } else {
                    int figureDeeper = figureOnHouseField(i + 1);
                    if (figureDeeper != 99) {
                        if (figures[figureDeeper].isFinished()) {
                            figures[cache].setFinished(true);
                        }
                    }
                }
            }
        }
    }

    //check which figure is on the normal field
    private int figureOnField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].getField() == fieldNumber){
                return i;
            }
        }
        return 99;
    }

    //check which figure is on the house field
    private int figureOnHouseField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].isInHouse()){
                if (figures[i].getField() == fieldNumber){
                    return i;
                }
            }
        }
        return 99;
    }

    //check if base is empty (argument between 0 & 3)
    private boolean isBaseEmpty(int playerNumber){
        boolean BaseStatus = true;
        for(int i = playerNumber*4; i<playerNumber*4+4; i++){
            if (figures[i].isInBase()) {
                BaseStatus = false;
                break;
            }
        }
        return BaseStatus;
    }
    
    //check if all figures are in the base
    private boolean isBaseFull(int playerNumber){
        boolean BaseStatus = true;
        for(int i = playerNumber*4; i<playerNumber*4+4; i++){
            if (!figures[i].isInBase()) {
                BaseStatus = false;
                break;
            }
        }
        return BaseStatus;
    }

    //check if given player is allowed to roll the dice three times
    private boolean threeTimesAllowed(int playerNumber){
        if (isBaseFull(playerNumber)){
            return true;
        }
        int finished = 0;
        int inBase = 0;
        //count figures in base
        for (int i = playerNumber * 4; i < playerNumber * 4 + 4; i++){
            if (figures[i].isInBase()){
                inBase++;
            }
        }
        //count figures finished
        for (int i = playerNumber * 4; i < playerNumber * 4 + 4; i++){
            if (figures[i].isFinished()){
                finished++;
            }
        }
        int cache = inBase + finished;
        if (cache == 4){
            return true;
        }else {
            return false;
        }
    }

    //move a figure out of base
    private void moveOutOfBase(int figureNumber){
        int figureColor = giveColor(figureNumber);
        figures[figureNumber].setField(10*figureColor);
        figures[figureNumber].setInBase(false);
    }
}
