import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;
    int playerNumber;
    boolean bots;
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
        usernames = new String[4];
        playerNumber = 0;
        bots = false;

        //progress input from landingpage
        startpage = landingpage;
        usernames = startpage.getNames();
        playerNumber = startpage.getPlayerNumber();
        bots = startpage.getBotsSelection();

        gui = new GameBoardGui(usernames[0], this);
    }
    //generate random number (copy from frontend)
    private int submitRandomNumber(){
        Random rand = new Random();
        return 1 + rand.nextInt(6);
    }

    //generate a randomNumber for the bots-movement with lower chances for 5 & 6
    private int submitRandomNumberBots(){
        int[] cache = {1, 2, 3, 4, 5, 6, 1, 2, 3, 4};
        Random rand = new Random();
        return cache[rand.nextInt(10)];
    }

    public void move(){
        if (activePlayer > playerNumber){
            botMove();
        } else {
            playerMove();
        }
    }

    //progress a dice input
    private void playerMove() {
        randomNumber = submitRandomNumber();
        //if user is allowed to roll the dice three time operate this option
        int counter = 0;
        if (threeTimesAllowed(activePlayer)){
            while (counter < 3 && randomNumber != 6){
                randomNumber = submitRandomNumber();
                counter++;
            }
            if (randomNumber != 6){
                nextPlayer();
                //trigger new move in fontEnd
                gui.setActivePlayer();
                return;
            }
        }
        gui.displayResult(randomNumber);

        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(activePlayer * 10);
        boolean ownFigureOnStartfield = false;
        if (figureOnStartfield != 99){
            if (figures[figureOnStartfield].color == activePlayer) {
                //own figure is on the startfield
                ownFigureOnStartfield = true;

            }
        }

        //check if an own figure is on the startfield
        if (ownFigureOnStartfield  && !isBaseEmpty(activePlayer)){
            figures[figureOnStartfield].placeOption = true;
        }

        //if base not empty move a player out of base
        else if (!isBaseEmpty(activePlayer) && randomNumber == 6) {
            for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++){
                if (figures[i].isInBase()){
                    figures[i].placeOption = true;
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
        //add chooser for all figures, which can beat another figure
        for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++) {
            if (beatPossible(i)) {
                figures[i].placeOption = true;
                beatsPossible = true;
            }
        }
        //make user figure chooser for all figures on the gamefield or in the house
        if (!beatsPossible){
            for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++) {
                if (figures[i].isMovable()) {
                    figures[i].placeOption = true;
                }
            }
        }
    }

    public void performUserChoice(){
        //removing the place options on all figures
        for (Figure figure : figures) {
            figure.placeOption = false;
        }
        gui.removePrompt();
        gui.replaceFigures();
        //check if a player has won yet
        checkFiguresIfFinished();
        if(finished()){
            winner = new WinWindow(usernames[whoFinished()]);
            gui.setVisible(false);
        }
        //trigger new move in fontEnd
        nextPlayer();
        gui.setActivePlayer();
    }

    //bot-move on the "normal" fields
    private void botMove(){
        randomNumber = submitRandomNumberBots();
        //if user is allowed to roll the dice three time operate this option
        int counter = 0;
        if (threeTimesAllowed(activePlayer)){
            while (counter < 3 && randomNumber != 6){
                randomNumber = submitRandomNumberBots();
                counter++;
            }
            if (randomNumber != 6){
                nextPlayer();
                //trigger new move in fontEnd
                gui.setActivePlayer();
                return;
            }
        }
        gui.displayResult(randomNumber);

        //cache a much used value, make the code look cleaner
        int figureOnStartfield = figureOnField(activePlayer * 10);
        boolean ownFigureOnStartfield = false;
        if (figureOnStartfield != 99){
            if (figures[figureOnStartfield].color == activePlayer) {
                //own figure is on the startfield
                ownFigureOnStartfield = true;
            }
        }

        //check if an own figure is on the startfield
        if (ownFigureOnStartfield  && !isBaseEmpty(activePlayer)){
            moveFigure(figureOnStartfield);
        }

        //if base not empty move a player out of base
        else if (!isBaseEmpty(activePlayer) && randomNumber == 6) {
            for (int i = activePlayer * 4; i < 16; i++){
                if (figures[i].isInBase()){
                    moveFigure(i, randomNumber);
                    break;
                }
            }
        } else {
            boolean beatsPossible = false;
            //add the figures to the chooser
            for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++) {
                if (beatPossible(i)) {
                    moveFigure(i);
                    beatsPossible = true;
                    break;
                }
            }
            //make user figure chooser for all figures of the player
            if (!beatsPossible){
                for (int i = activePlayer * 4; i < activePlayer * 4 + 4; i++) {
                    if (figures[i].isMovable()) {
                        moveFigure(i, randomNumber);
                        break;
                    }
                }
            }
        }
        gui.replaceFigures();
        //check if a player has won yet
        checkFiguresIfFinished();
        if(finished()){
            winner = new WinWindow(usernames[whoFinished()]);
            gui.setVisible(false);
        }
        //trigger new move in fontEnd
        nextPlayer();
        gui.setActivePlayer();
    }

    //move the given figure by the given number
    public void moveFigure(int figureNumber) {
        //store the color of the figure in a local variable
        int figureColor = figures[figureNumber].color;

        //check if the figure isn't finished and not in the base
        if(figures[figureNumber].isMovable()) {
            //store the old and new field-number in local variables
            int numberOld = figures[figureNumber].field;
            int cache = numberOld + randomNumber;
            int numberNew = cache;
            if (numberNew > 39){
                numberNew -= 40;
            }

            //check if the figure is on the gamefield
            if (figures[figureNumber].isOnField()) {
                //check if a figure would come over its own startfield
                boolean goToHouse = false;
                if (numberOld < figureColor * 10 && cache >= figureColor * 10){
                    goToHouse = true;
                }
                if (figureColor == 0 && numberOld > 34 && numberNew >= 0){
                    goToHouse = true;
                }

                //if the figure comes over its startfield -> move the figure in the base
                if (goToHouse) {
                    int toMove = randomNumber;
                    int figurePosition = figures[figureNumber].field;
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
                    //check if a move into the base is possible
                    boolean movePossible = true;
                    if (toMove > 0 && toMove < 5){
                        for (int i = 0; i <= toMove; i++){
                            if (figureOnField(i + figureColor*4) != 99){
                                movePossible = false;
                                break;
                            }
                        }

                    }
                    if (movePossible){
                        toMove--;
                        figures[figureNumber].setInHouse();
                        figures[figureNumber].field = figureColor*4;
                        if (toMove > 0){
                            randomNumber = toMove;
                            moveInHouse(figureNumber);
                        }
                    }
                }
                //move the figure, if the new field is free
                else if (figureOnField(numberNew) == 99){
                    figures[figureNumber].field = numberNew;
                }

                else {
                    //move the figure, and move the figure before on the field to the base
                    if (figures[figureOnField(numberNew)].color != figureColor){
                        moveToBase(figureOnField(numberNew));
                        figures[figureNumber].field = numberNew;
                    } else {
                        //perform the moveFigure-method with the figure, standing on the field th figure at the moment wants to move, and the same stepLength
                        moveFigure(figureOnField(numberNew));
                    }
                }
            }
            else {
                moveInHouse(figureNumber);
            }
        }
        //if figure is in the base and the step-length is 6 move figure out of base
        else if (figures[figureNumber].isInBase() && randomNumber == 6) {
            moveOutOfBase(figureNumber);
        }
    }

    //move figure in the house by the given value
    private void moveInHouse(int figureNumber){
        //if-loop preventing false moves by mistake
        if (figures[figureNumber].isInHouse()){
            //store the figure-field and the figure-color as local variable
            int figureField = figures[figureNumber].field;
            int figureColor = figures[figureNumber].color;
            //be safe that the move doesn't go over the available fields in the house
            if (figureField + randomNumber < figureColor * 4 + 4){
                //be safe that the figure doesn't jump over other figures
                boolean movePossible = true;
                for (int i = figureField; i < 4; i++){
                    if (figureOnField(i) != 99){
                        movePossible = false;
                        break;
                    }
                }
                //perform the move
                if (movePossible){
                    figures[figureNumber].field = figureField + randomNumber;
                }
            }
        }
    }

    //check if a beat is possible
    private boolean beatPossible(int figureNumber){
        if (figures[figureNumber].isOnField()) {
            //store some useful variables
            int numberOld = figures[figureNumber].field;
            int cache = numberOld + randomNumber;
            int numberNew = cache;
            if (numberNew > 39){
                numberNew -= 40;
            }
            int figureColor = figures[figureNumber].color;

            if (numberOld < figureColor * 10 && cache >= figureColor * 10){
                return false;
            }
            if (figureColor == 0 && numberOld > 34 && numberNew >= 0){
                return false;
            }

            //real check, if a figure of another color is standing on the field
            if (figureOnField(numberNew) == 99){
                return false;
            } else if (figures[figureOnField(numberNew)].color != figureColor) {
                return true;
            }
        }
        return false;
    }

    //move the given figure to the base
    public void moveToBase(int figureNumber){
        figures[figureNumber].setInBase();
        figures[figureNumber].field = figureNumber;
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
        if (figures[0].isFinished() && figures[1].isFinished()&& figures[2].isFinished() && figures[3].isFinished()){
            return 0;            
        } else if (figures[4].isFinished() && figures[5].isFinished() && figures[6].isFinished() && figures[7].isFinished()) {
            return 1;
        } else if (figures[8].isFinished() && figures[9].isFinished()  && figures[10].isFinished() && figures[11].isFinished()) {
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
                    figures[cache].setFinished();
                } else {
                    int figureDeeper = figureOnHouseField(i + 1);
                    if (figureDeeper != 99) {
                        if (figures[figureDeeper].isFinished()) {
                            figures[cache].setFinished();
                        }
                    }
                }
            }
        }
    }

    //check which figure is on the normal field
    public int figureOnField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].field == fieldNumber && figures[i].isOnField()) {
                return i;
            }
        }
        return 99;
    }

    //check which figure is on the house field
    public int figureOnHouseField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].isInHouse()){
                if (figures[i].field == fieldNumber){
                    return i;
                }
            }
        }
        return 99;
    }

    public int figureOnBaseField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].isInBase()) {
                if (figures[i].field == fieldNumber){
                    return i;
                }
            }
        }
        return 99;
    }

    //check if base is empty (argument between 0 & 3)
    private boolean isBaseEmpty(int playerNumber){
        for(int i = playerNumber*4; i<playerNumber*4+4; i++){
            if (figures[i].isInBase()) {
		return false;
            }
        }
        return true;
    }
    
    //check if all figures are in the base
    private boolean isBaseFull(int playerNumber){
        for(int i = playerNumber*4; i<playerNumber*4+4; i++){
            if (!figures[i].isInBase()) {
		return false;
            }
        }
        return true;
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
        return cache == 4;
    }

    //move a figure out of base
    public void moveOutOfBase(int figureNumber){
        int figureColor = giveColor(figureNumber);
        int figureOnFirstField = figureOnField(10 * figureColor);
        if (figureOnFirstField != 99){
            moveToBase(figureOnFirstField);
        }
        figures[figureNumber].field = 10 * figureColor;
        figures[figureNumber].setOnField();
    }

    //next player
    private void nextPlayer(){
        if (randomNumber != 6){
            if (bots){
                if (activePlayer < 3){
                    activePlayer++;
                } else {
                    activePlayer = 0;
                }
            }else {
                if (activePlayer < playerNumber){
                    activePlayer++;
                } else {
                    activePlayer = 0;
                }
            }
        }
    }
}
