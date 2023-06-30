import java.util.Random;
public class BackEnd {
    Figure[] figures;

    BackEnd(){
        figures = new Figure[16];
        for (int i = 0; i < figures.length; i++){
            figures[i] = new Figure(i, 0);
        }
        for (int i = 0; i < 4; i++){
            figures[i + 4].setColor(1);
            figures[i + 8].setColor(2);
            figures[i + 12].setColor(3);
        }

        //progress input from langpage
        
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

    //check which player has won

    //generate random number (copy from frontend)

    //check all figures if they are finished
}
