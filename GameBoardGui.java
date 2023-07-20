import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class GameBoardGui extends JFrame implements ActionListener, MouseListener {
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
    JLabel result;
    JLabel figureChooserPrompt;

    //variable for the backend
    BackEnd backend;

    public GameBoardGui(String currentPlayer, BackEnd backendNew) {
        //initialization of arrays
        house = new Circle[16];
        base = new Circle[16];
        gameField = new Circle[40];
        figures = new Circle[16];
	backend = backendNew;
	
        replaceFigures();

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
        result = new JLabel();
        figureChooserPrompt = new JLabel();

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
    }

    //methods for the mouse listener
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        boolean moveFinished = false;
        //check if the figure is on the gamefield
        for (int i = 0; i < gameFieldX.length && !moveFinished; i++){
            int diffX = gameFieldX[i] - mouseX;
            int diffY = gameFieldY[i] - mouseY;
            if (-50 <= diffX && diffX <= 0 && -50 <= diffY && diffY <= 0){
                int cache = backend.figureOnField(i);
                if (cache != 99){
                    if(backend.figures[cache].placeOption){
                        backend.moveFigure(cache, backend.randomNumber);
                        moveFinished = true;
                        break;

                    }
                }
            }
        }
        //check if the figure is in the house or base
        for (int i = 0; i < houseX.length && !moveFinished; i++){
            int diffX = houseX[i] - mouseX;
            int diffY = houseY[i] - mouseY;
            if (-50 <= diffX && diffX <= 0 && -50 <= diffY && diffY <= 0){
                int house = backend.figureOnHouseField(i);
                if (house != 99){
                    if (backend.figures[house].placeOption){
                        backend.moveFigure(house, backend.randomNumber);
                        moveFinished = true;
                        break;
                    }
                }
                int base = backend.figureOnBaseField(i);
                if (base != 99){
                    if (backend.figures[base].placeOption){
                        backend.moveOutOfBase(base);
                        moveFinished = true;
                        break;
                    }
                }
            }
        }
        if (moveFinished){
            backend.performUserChoice();
        }
    }

    //methods below aren't used (but needed, otherwise causing errors)
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }

     //Interface with BackEnd - 1. method: replaceFigures
    public void replaceFigures(){
        Figure[] input = backend.figures;
        for (int i = 0; i < input.length && i < figures.length; i++){
            if(input[i].inBase){
                figures[i] = new Circle(baseX[input[i].field], baseY[input[i].field], 50, figureColors[input[i].color]);
            }else if (input[i].inHouse){
                figures[i] = new Circle(houseX[input[i].field], houseY[input[i].field], 50, figureColors[input[i].color]);
            }else {
                figures[i] = new Circle(gameFieldX[input[i].field], gameFieldY[input[i].field], 50, figureColors[input[i].color]);
            }
        }
        repaint();
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

    //setting the values for the figureChooserPrompt
    public void setPromptValues(){
        figureChooserPrompt.setText("Choose the figure you want to move!");
        figureChooserPrompt.setBounds(970, 120, 250, 32);
        add(figureChooserPrompt);
    }

    //removing the figureChooserPrompt from the GameBoardGui
    public void removePrompt(){
        remove(figureChooserPrompt);
    }

    /*
      -- DON'T use this method out of constructor - DON'T change any parameters - ONLY call at the END of the constructor --
      sets all needed parameters for the frame and opens in the end
      -- DON'T use this method out of constructor - DON'T change any parameters - ONLY call at the END of the constructor --
     */
    private void adjustJFrameSetting() {
        addMouseListener(this);
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
