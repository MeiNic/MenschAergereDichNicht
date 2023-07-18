import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameBoardGui extends JFrame implements ActionListener{
    //Store hex-codes for different colors - yellow, green, blue, red, normal
    private final String[] colors = {"#ffc957", "#2a914e", "#1e32ff", "#cc0000", "#cccccc"};
    private final String[] figureColors = {"#ffff00", "#00cc00", "#3c93ff", "#ff0000"};

    //Circles house - Yellow-Green-Blue-Red (a-b-c-d)
    private Circle[] house;
    private final int[] houseX = {445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445, 765, 685, 605, 525};
    private final int[] houseY = {765, 685, 605, 525, 445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445};

    //Circles base - Yellow-Green-Blue-Red (bottom left, bottom right, top left, top right)
    private Circle[] base;
    private final int[] baseX = {45, 110, 45, 110, 45, 110, 45, 110, 780, 845, 780, 845, 780, 845, 780, 845};
    private final int[] baseY = {845, 845, 780, 780, 110, 110, 45, 45, 110, 110, 45, 45, 845, 845, 780, 780};

    //Circles gamefield - start at yellow startfield
    private Circle[] gameField;
    private final int[] gameFieldX = {360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845, 840, 765, 685, 605, 525, 525, 525, 525, 525, 445};
    private final int[] gameFieldY = {840, 765, 685, 605, 525, 525, 525, 525, 525, 445, 360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845};

    //ovals figures
    private Circle[] figures;

    //all JComponents
    JLabel userAdvice;
    JButton rollDice;
    JButton[] inVisibleButtons;
    JLabel result;
    JLabel figureChooserPrompt;

    //variable for the backend
    BackEnd backend;

    public GameBoardGui(String currentPlayer, Figure[] standardFigures, BackEnd backendNew) {
        //initialization of arrays
        house = new Circle[16];
        base = new Circle[16];
        gameField = new Circle[40];
        figures = new Circle[16];
        replaceFigures(standardFigures);

        //set circles in arrays
        setCircleValues(house, houseX, houseY, 50, colors[0]);
        setCircleValues(base, baseX, baseY, 50, colors[0]);
        setCircleValues(gameField, gameFieldX, gameFieldY, 50, colors[4]);

        //set different colors in one array
        for (int i = 0; i < 4; i++) {
            house[i + 4].setColor(colors[1]);
            house[i + 8].setColor(colors[2]);
            house[i + 12].setColor(colors[3]);
            base[i + 4].setColor(colors[1]);
            base[i + 8].setColor(colors[2]);
            base[i + 12].setColor(colors[3]);
            gameField[i * 10].setColor(colors[i]);
        }

        //Implement JButton and JLabel
        userAdvice = new JLabel();
        rollDice = new JButton();
        inVisibleButtons = new JButton[4];
        result = new JLabel();
        figureChooserPrompt = new JLabel();

        //link backEnd
        backend = backendNew;

        //set parameters for JComponents
        setJComponentValues(currentPlayer);
        //display GUI
        adjustJFrameSetting();
    }

    //Button Action - Method
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rollDice){
            //remove the button from the JPanel
            remove(rollDice);

            //trigger new move in the backend
            backend.playerMove();
        }
        else if (e.getSource() == inVisibleButtons[0]){
            //perform method in BackEnd
            for (int i = 0; i < inVisibleButtons.length; i++){
                inVisibleButtons[i] = null;
            }
            backend.performUserChoice(0);
        }
        else if (e.getSource() == inVisibleButtons[1]){
            //perform method in BackEnd
            for (int i = 0; i < inVisibleButtons.length; i++){
                inVisibleButtons[i] = null;
            }
            backend.performUserChoice(1);
        }
        else if (e.getSource() == inVisibleButtons[2]){
            //perform method in BackEnd
            for (int i = 0; i < inVisibleButtons.length; i++){
                inVisibleButtons[i] = null;
            }
            backend.performUserChoice(2);
        }
        else if (e.getSource() == inVisibleButtons[3]){
            //perform method in BackEnd
            for (int i = 0; i < inVisibleButtons.length; i++){
                inVisibleButtons[i] = null;
            }
            backend.performUserChoice(3);
        }
    }

    /*
     Interface with BackEnd
     1. method: replaceFigures
     2. method: setUserFigureOption
     */
    public void replaceFigures(Figure[] input){
        for (int i = 0; i < input.length && i < figures.length; i++){
            if(input[i].isInBase()){
                figures[i] = new Circle(baseX[input[i].getField()], baseY[input[i].getField()], 50, figureColors[input[i].getColor()]);
            }else if (input[i].isInHouse()){
                figures[i] = new Circle(houseX[input[i].getField()], houseY[input[i].getField()], 50, figureColors[input[i].getColor()]);
            }else {
                figures[i] = new Circle(gameFieldX[input[i].getField()], gameFieldY[input[i].getField()], 50, figureColors[input[i].getColor()]);
            }
        }
        repaint();
    }

    public void setUserFigureOption(Figure[] input){
        for (int i = 0; i < input.length && i < figures.length; i++){
            if (input[i].isPlaceOption()){
                if (input[i].isInBase()){
                    placeInvisibleButton(baseX[input[i].getField()], baseY[input[i].getField()]);
                }
                else if (input[i].isInHouse()){
                    placeInvisibleButton(houseX[input[i].getField()], houseY[input[i].getField()]);
                }
                else {
                    placeInvisibleButton(gameFieldX[input[i].getField()], gameFieldY[input[i].getField()]);
                }
            }
        }
    }

    //method displays the given value as the result
    public void displayResult(int randomNumber){
        result.setText("Result: " + randomNumber);
        repaint();
    }

    /*
    method sets the given player-name to the userAdvice-JLabel and resets the result jLabel
     */
    public void setActivePlayer(String newPlayer){
        add(rollDice);
        userAdvice.setText("Player " + newPlayer + " is on the turn, click this button");
        result.setText("");
        repaint();
    }
    

    /*
      -- DON'T use this method out of constructor - DON'T change any parameters --
      methods sets als parameters for the JLabels and the JButton and in the end adds them to the frame
      -- DON'T use this method out of constructor - DON'T change any parameters --
     */
    private void setJComponentValues(String currentPlayer){
        userAdvice.setText("Player " + currentPlayer + " is on the turn, click this button");
        userAdvice.setBounds(970, 22, 370, 62);
        add(userAdvice);
        rollDice.setText("roll the dice");
        rollDice.setBounds(980, 90, 120, 32);
        rollDice.addActionListener(this);
        add(rollDice);
        result.setBounds(1150, 90, 100, 32);
        add(result);
    }

    public void setPromptValues(){
        figureChooserPrompt.setText("Choose the figure you want to move!");
        figureChooserPrompt.setBounds(970, 120, 200, 32);
        add(figureChooserPrompt);
    }

    /*
        this method, sets all needed parameters to the next free JButton in the invisibleButton-Array
        the method gets the coordinates for the button on its call
     */
    private void placeInvisibleButton(int x, int y){
        int i = 0;
        while (inVisibleButtons[i] != null){
            i++;
        }
        inVisibleButtons[i] = new JButton();
        inVisibleButtons[i].addActionListener(this);
        inVisibleButtons[i].setBounds(x, y, 50, 50);
        inVisibleButtons[i].setContentAreaFilled(false);
        inVisibleButtons[i].setBorderPainted(false);
        inVisibleButtons[i].setFocusPainted(false);
        add(inVisibleButtons[i]);
    }

    /*
      -- DON'T use this method out of constructor - DON'T change any parameters - ONLY call at the END of the constructor --
      sets all needed parameters for the frame and opens in the end
      -- DON'T use this method out of constructor - DON'T change any parameters - ONLY call at the END of the constructor --
     */
    private void adjustJFrameSetting() {
        setTitle("game field");
        setSize(1400, 940);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(Color.BLACK);
        setResizable(true);
        setVisible(true);
    }

    /*
     -- methods only used in constructor DON'T use out of the constructor! --
     Function sets in the give array new Circles with the given x and y value, radius and color
     als well as set the border array with fitting border circles
     -- methods only used in constructor DON'T use out of the constructor! --
     */
    private void setCircleValues(Circle[] array, int[] x, int[] y, int radius, String hexColor) {
        for (int i = 0; i < array.length; i++) {
            int xNew = x[i];
            int yNew = y[i];
            array[i] = new Circle(xNew, yNew, radius, hexColor);
        }
    }

    /*
     -- methods needed to paint circles in JFrame --
     "paint() is used to paint all graphical objects in the JFrame (circles + ovals)
     -- methods needed to paint circles in JFrame --
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        forEachloopPaintFields(g, house);
        forEachloopPaintFields(g, base);
        forEachloopPaintFields(g, gameField);
        forEachloopPaintFigures(g, figures);
    }

    private void forEachloopPaintFields(Graphics g, Circle[] array) {
        for (Circle circle : array) {
            int x = circle.getX();
            int y = circle.getY();
            int radius = circle.getRadius();
            Color color = circle.getColor();

            g.setColor(color);
            g.fillOval(x, y, radius, radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - 1, y - 1, radius + 1, radius + 1);
        }
    }

    private void forEachloopPaintFigures(Graphics g, Circle[] array){
        for (Circle oval : array){
            int x = oval.getX() + oval.getRadius() / 4;
            int y = oval.getY();
            int radius = oval.getRadius();
            int radiusHalf = oval.getRadius() / 2;
            Color color = oval.getColor();

            g.setColor(color);
            g.fillOval(x, y, radiusHalf, radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - 1, y - 1, radiusHalf + 1, radius + 1);
        }
    }
}