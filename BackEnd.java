import java.util.Random;
public class BackEnd {
    Figure[] figures;
    Landingpage startpage;
    String[] usernames;

    BackEnd(Landingpage landingpage){
        figures = new Figure[16];
        for (int i = 0; i < figures.length; i++){
            figures[i] = new Figure(i, 0);
        }
        for (int i = 0; i < 4; i++){
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
    private void moveFigure(int figureNumber, int stepLength){
        int numberOld = figures[figureNumber].getField();
        int numberNew = numberOld + stepLength;
        int figureColor = figures[figureNumber].getColor();
        if (numberOld < figureColor && numberNew >= figureColor * 10){
            figures[figureNumber].setInHouse(true);
            numberNew -= figureColor * 10;
            numberNew += figureColor * 4;
        }
        figures[figureNumber].setField(numberNew);
    }

    //move the given figure to the base

    //check if a player has won (return true/false)
    private boolean finished(){
        for (int i = 0; i < 4; i++){
            if (figures[i].isFinished() && figures[i + 1].isFinished() && figures[i + 2].isFinished() && figures[i + 3].isFinished()){
                return true;
            }
        }
        return false;
    }

    //check which player has won

    //generate random number (copy from frontend)

    //check all figures if they are finished
}
