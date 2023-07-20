import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;
    int activePlayer;
    GameBoardGui gui;
    int randomNumber;
    boolean noChooserSet;
    winWindow winner;
    boolean finishStatus;

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
        noChooserSet = true;
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
    public void playerMove() {
        noChooserSet = true;
        finishStatus = false;
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
        boolean ownFigureOnStartfield = false;
        if (figureOnStartfield != 99){
            if (figures[figureOnStartfield].color == activePlayer) {
                //own figure is on the startfield
                ownFigureOnStartfield = true;

            }
        }

        if (randomNumber == 6) {
            //check if an own figure is on the startfield
            if (ownFigureOnStartfield){
                moveFigure(figureOnStartfield, randomNumber);
            }

            //if base not empty move a player out of base
            else if (!isBaseEmpty(activePlayer)) {
                for (int i = activePlayer * 4; i < 16; i++){
                    if (figures[i].inBase){
                        moveOutOfBase(i);
                        break;
                    }
                }
            } else {
                playerMoveOnField();
            }

            //no figure chooser set -> display all changes in gui
            if (noChooserSet) {
                gui.replaceFigures(figures);
            }
            //figure chooser set -> end method
            else {
                return;
            }
        }
        else {

            //check if an own figure is on the startfield
            if (ownFigureOnStartfield) {
                moveFigure(figureOnStartfield, randomNumber);
            }else {
                playerMoveOnField();
            }

            //no figure chooser set -> display changes in gui & set activePlayer to next player
            if (noChooserSet){
                gui.replaceFigures(figures);
                if (activePlayer == 3) {
                    activePlayer = 0;
                } else {
                    activePlayer++;
                }
            }
            //figure chooser set -> end method
            else {
                return;
            }
        }
        //check if a player has won yet
        if(finished()){
            finishStatus = true;
            winner = new winWindow(usernames[whoFinished()]);
            gui.setVisible(false);
        }

        //trigger new move in fontEnd
        gui.setActivePlayer(usernames[activePlayer]);
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
                        break;
                    }
                }
            } else {
                //add the figure to the chooser
                for (int i = 0; i < 4; i++) {
                    int activeFigure = activePlayer * 4 + i;
                    if (beatPossible(activeFigure, randomNumber)) {
                        figures[activeFigure].placeOption = true;
                    }
                }
                noChooserSet = false;
                //perform the user choice of the frontEnd
                gui.setPromptValues();
            }
        }
        //make user figure chooser for all figures of the player
        else {
            for (int i = 0; i < 4; i++) {
                int activeFigure = activePlayer * 4 + i;
                if (!figures[activeFigure].finished && !figures[activeFigure].inBase) {
                    figures[activeFigure].placeOption = true;
                }
            }
            noChooserSet = false;
            //perform the user choice of the frontEnd
            gui.setPromptValues();
        }
    }

    public void performUserChoice(){
        //removing the place options on all figures
        for (Figure figure : figures) {
            figure.placeOption = false;
        }

        //rest of the normal playerMove-method
        gui.replaceFigures(figures);
        if (randomNumber != 6){
            if (activePlayer == 3){
                activePlayer = 0;
            } else {
                activePlayer++;
            }
        }

        //check if a player has won yet
        if(finished()){
            finishStatus = true;
            winner = new winWindow(usernames[whoFinished()]);
            gui.setVisible(false);
        }

        //trigger new move in fontEnd
        gui.setActivePlayer(usernames[activePlayer]);
    }

    //move the given figure by the given number
    public void moveFigure(int figureNumber, int stepLength) {
        //store the color of the figure in a local variable
        int figureColor = figures[figureNumber].color;

        //check if the figure isn't finished and not in the base
        if(!figures[figureNumber].finished && !figures[figureNumber].inBase){
            //store the old and new field-number in local variables
            int numberOld = figures[figureNumber].field;
            int cache = numberOld + stepLength;
            int numberNew = cache;
            if (numberNew > 39){
                numberNew -= 40;
            }

            //check if the figure is on the gamefield
            if (!figures[figureNumber].inBase){
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
                        figures[figureNumber].inHouse = true;
                        numberNew -= figureColor * 10;
                        figures[figureNumber].field = numberNew;
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
                        moveFigure(figureOnField(numberNew), stepLength);
                    }
                }
            }
            else if (numberNew < figures[figureNumber].color * 4 + 4){
                figures[figureNumber].field = numberNew;
            }
        }
        //if figure is in the base and the step-length is 6 move figure out of base
        else if (figures[figureNumber].inBase && stepLength == 6) {
            moveOutOfBase(figureNumber);
        }
    }

    //check if a beat is possible
    private boolean beatPossible(int figureNumber, int stepLength){
        if (!figures[figureNumber].inBase && !figures[figureNumber].inHouse){
            //store some useful variables
            int numberOld = figures[figureNumber].field;
            int numberNew = numberOld + stepLength;
            if (numberNew > 39){
                numberNew -= 40;
            }
            int figureColor = figures[figureNumber].color;

            //real check, if a figure of another color is standing on the field
            if (figureOnField(numberNew) == 99){
                return false;
            } else if (figures[figureOnField(numberNew)].color != figureColor) {
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
        int emptyField = 4 * figureColor;
        while (figureOnField(emptyField) != 99){
            emptyField++;
        }
        figures[figureNumber].inBase = true;
        figures[figureNumber].field = emptyField;
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
            if (figures[i].finished && figures[i + 1].finished && figures[i + 2].finished && figures[i + 3].finished) {
                return true;
            }
        }
        return false;
    }

    //check which player has won
    private int whoFinished(){
        if (figures[0].finished && figures[1].finished && figures[2].finished && figures[3].finished){
            return 0;            
        } else if (figures[4].finished && figures[5].finished && figures[6].finished && figures[7].finished) {
            return 1;
        } else if (figures[8].isFinished() && figures[9].isFinished() && figures[10].isFinished() && figures[11].isFinished()) {
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
                    figures[cache].finished = true;
                } else {
                    int figureDeeper = figureOnHouseField(i + 1);
                    if (figureDeeper != 99) {
                        if (figures[figureDeeper].finished) {
                            figures[cache].finished = true;
                        }
                    }
                }
            }
        }
    }

    //check which figure is on the normal field
    public int figureOnField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].field == fieldNumber && !figures[i].inBase && !figures[i].inHouse){
                return i;
            }
        }
        return 99;
    }

    //check which figure is on the house field
    public int figureOnHouseField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].inHouse){
                if (figures[i].field == fieldNumber){
                    return i;
                }
            }
        }
        return 99;
    }

    public int figureOnBaseField(int fieldNumber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].inBase) {
                if (figures[i].field == fieldNumber){
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
            if (figures[i].inBase) {
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
            if (!figures[i].inBase) {
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
            if (figures[i].inBase){
                inBase++;
            }
        }
        //count figures finished
        for (int i = playerNumber * 4; i < playerNumber * 4 + 4; i++){
            if (figures[i].finished){
                finished++;
            }
        }
        int cache = inBase + finished;
        return cache == 4;
    }

    //move a figure out of base
    public void moveOutOfBase(int figureNumber){
        int figureColor = giveColor(figureNumber);
        figures[figureNumber].field = 10*figureColor;
        figures[figureNumber].inBase = false;
    }
}
