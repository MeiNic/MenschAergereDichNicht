import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;

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

        //progress input from landingpage
        startpage = landingpage;
        usernames[4] = startpage.getName();

        new GameBoardGui("test");
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
    private void moveToBase(int figureNummber){
        int figureColor = giveColor(figureNummber);
        int emptyField = 0 + 4 * figureColor;
        while (figureOnField(emptyField) != 99){
            emptyField++;
        }
        figures[figureNummber].setInBase(true);
        figures[figureNummber].setField(emptyField);
    }

    //return to which player the given figure belongs to
    private int giveColor(int figureNummber){
        if (figureNummber < 4){
            return 0;
        } else if (figureNummber < 8) {
            return 1;
        } else if (figureNummber < 12) {
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
}