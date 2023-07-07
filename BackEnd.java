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
        int figureColor = figures[figureNumber].getColor();
        if (numberOld < figureColor && numberNew >= figureColor * 10) {
            figures[figureNumber].setInHouse(true);
            numberNew -= figureColor * 10;
            numberNew += figureColor * 4;
        }
        figures[figureNumber].setField(numberNew);
    }

    //move the given figure to the base

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

    //generate random number (copy from frontend)

    //check all figures if they are finished
    private void checkFiguresIfFinished() {
        for (int i = figures.length - 1; i >= 0; i--) {
            int cache = figureOnHouseField(i);
            if (cache != 99) {
                if (i == 15 || i == 11 || i == 7 || i == 3) {
                    figures[cache].setFinished(true);
                }
            }
            else {
                int figureDeeper = figureOnHouseField(i + 1);
                if (figureDeeper != 99){
                    if (figures[figureDeeper].isFinished()){
                        figures[cache].setFinished(true);
                    }
                }
            }
        }
    }


    //check which figure is on the house field
    private int figureOnHouseField(int fieldNummber){
        for (int i = 0; i < figures.length; i++){
            if (figures[i].isInHouse()){
                if (figures[i].getField() == fieldNummber){
                    return i;
                }
            }
        }
        return 99;
    }
}