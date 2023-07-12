import com.sun.source.tree.BreakTree;

import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;
    int activePlayer;

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

        //progress input from landingpage
        startpage = landingpage;
        usernames[4] = startpage.getName();

        new GameBoardGui("test");
    }

    //progress a dice input
    private void playerMove(){
        int randomNumber = submitRandomNumber();
        if(randomNumber == 6){
            int figureOnStartfield = figureOnField(activePlayer * 10);
            if(figureOnStartfield != activePlayer * 4 && figureOnStartfield != activePlayer * 4 +1 && figureOnStartfield != activePlayer * 4 + 2 && figureOnStartfield != activePlayer * 4 +3){
                //startfield is empty
            }
            else{

            }
            //display all changes in the frontEnd
            //trigger new move in frontEnd
        }
        else{
            //display all changes in the frontEnd
            //same code as before the comment in the "if"-block
            if(activePlayer == 3){
                activePlayer = 0;
            }else{
                activePlayer++;
            }
            //trigger new move in fontEnd
        }
    }

    //move the given figure by the given number
    private void moveFigure(int figureNumber, int stepLength) {
        int numberOld = figures[figureNumber].getField();
        int numberNew = numberOld + stepLength;
        if (!figures[figureNumber].isFinished() && !figures[figureNumber].isInBase()){
            int figureColor = figures[figureNumber].getColor();
            if (numberOld < figureColor && numberNew >= figureColor * 10) {
                figures[figureNumber].setInHouse(true);
                numberNew -= figureColor * 10;
                numberNew += figureColor * 4;
            } else {
                figures[figureNumber].setField(numberNew);
            }
        }
        else if (figures[figureNumber].isFinished()){
            figures[figureNumber].setField(numberNew);
        }

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

    //check if a step is possible (not possible if the figure on the field to move is, belongs to the same player)
    private boolean isMovePossible(int figureNumber, int moveLenght){
        if (figureOnField(figures[figureNumber].getField() + moveLenght) != 99){
            if (giveColor(figureNumber) == giveColor(figureOnField(figures[figureNumber].getField() + moveLenght))){
                return false;
            } else {
                return true;
            }
        } else{
            return true;
        }
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

    //generate random number (copy from frontend)
    private int submitRandomNumber(){
        Random rand = new Random();
        return 1 + rand.nextInt(6);
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
            }
        }
        return BaseStatus;
    }

    //move a figure out of base
    private void moveOutOfBase(int figureNumber){
        int figureColor = giveColor(figureNumber);
        figures[figureNumber].setField(10*figureColor);
        figures[figureNumber].setInBase(false);
    }
} 