import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameBoardGui extends JFrame implements ActionListener, MouseListener {
    private enum FieldColor {
	YELLOW("#FFC957"),
	GREEN("#2A914E"),
	BLUE("#1E32FF"),
	RED("#CC0000"),
	GRAY("#CCCCCC");

	private final String html;

	private FieldColor(String html) {
	    this.html = html;
	}

	public String getHTML() {
	    return html;
	}
    }

    private enum FigureColor {
	YELLOW("#FFFF00"),
        GREEN("#00CC00"),
	BLUE("#3C93FF"),
	RED("#FF0000");

	private final String html;

	private FigureColor(String html) {
	    this.html = html;
	}

	public String getHTML() {
	    return html;
	}
    }

    private Circle[] figures;
    private Circle[] houses;
    private Circle[] bases;
    private Circle[] fields;
    
    private final int[] housePositionsX = {445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445, 765, 685, 605, 525};
    private final int[] housePositionsY = {765, 685, 605, 525, 445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445};

    private final int[] basePositionsX = {45, 110, 45, 110, 45, 110, 45, 110, 780, 845, 780, 845, 780, 845, 780, 845};
    private final int[] basePositionsY = {845, 845, 780, 780, 110, 110, 45, 45, 110, 110, 45, 45, 845, 845, 780, 780};

    private final int[] fieldPositionsX = {360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845, 840, 765, 685, 605, 525, 525, 525, 525, 525, 445};
    private final int[] fieldPositionsY = {840, 765, 685, 605, 525, 525, 525, 525, 525, 445, 360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845};

    //all JComponents
    JLabel userAdvice;
    JButton rollDice;
    JLabel result;
    JLabel figureChooserPrompt;

    //variable for the backend
    BackEnd backend;

    public GameBoardGui(String currentPlayer, BackEnd backendNew) {
        backend = backendNew;

        figures = new Circle[16];
        houses = new Circle[16];
        bases = new Circle[16];
        fields = new Circle[40];
	
        replaceFigures();

        setCircleValues(houses, housePositionsX, housePositionsY, 50, FieldColor.YELLOW.getHTML());
        setCircleValues(bases, basePositionsX, basePositionsY, 50, FieldColor.YELLOW.getHTML());
        setCircleValues(fields, fieldPositionsX, fieldPositionsY, 50, FieldColor.GRAY.getHTML());

        for (int i = 0; i < 4; i++) {
            houses[i + 4].setColor(FieldColor.GREEN.getHTML());
            houses[i + 8].setColor(FieldColor.BLUE.getHTML());
            houses[i + 12].setColor(FieldColor.RED.getHTML());

            bases[i + 4].setColor(FieldColor.GREEN.getHTML());
            bases[i + 8].setColor(FieldColor.BLUE.getHTML());
            bases[i + 12].setColor(FieldColor.RED.getHTML());
        }

	// Set color for start fields
	fields[0].setColor(FieldColor.YELLOW.getHTML());
	fields[10].setColor(FieldColor.GREEN.getHTML());
	fields[20].setColor(FieldColor.BLUE.getHTML());
	fields[30].setColor(FieldColor.RED.getHTML());

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

    public void mouseClicked(MouseEvent e) {
        int mousePositionX = e.getX();
        int mousePositionY = e.getY();

	int diameter = 50;

	for (int i = 0; i < fieldPositionsX.length; i++) {
	    int differenceX = mousePositionX - fieldPositionsX[i];
	    int differenceY = mousePositionY - fieldPositionsY[i];

	    if (differenceX <= 0 || diameter <= differenceX
		|| differenceY <= 0 || diameter <= differenceY) {
		continue;
	    }
	    
	    int clickedFigureIndex = backend.figureOnField(i);
	    if (clickedFigureIndex == 99) {
		continue;
	    }
		
	    Figure clickedFigure = backend.figures[clickedFigureIndex];
	    if (clickedFigure.color != backend.activePlayer) {
		continue;
	    }

	    if (clickedFigure.isPlaceable()) {
		backend.moveFigure(clickedFigureIndex);
	    } else {
		backend.moveToBase(clickedFigureIndex);
	    }
	    backend.performUserChoice();
	    return;
	}
	
	for (int i = 0; i < housePositionsX.length; i++) {
	    int differenceX = mousePositionX - housePositionsX[i];
	    int differenceY = mousePositionY - housePositionsY[i];

	    if (differenceX <= 0 || diameter <= differenceX
		|| differenceY <= 0 || diameter <= differenceY) {
		continue;
	    }
	    
	    int clickedFigureIndex = backend.figureOnHouseField(i);
	    if (clickedFigureIndex == 99) {
		continue;
	    }
		
	    Figure clickedFigure = backend.figures[clickedFigureIndex];
	    if (clickedFigure.color != backend.activePlayer) {
		continue;
	    }

	    if (clickedFigure.isPlaceable()) {
		backend.moveFigure(clickedFigureIndex);
	    } else {
		backend.moveToBase(clickedFigureIndex);
	    }
	    backend.performUserChoice();
	    return;
	}
	
	for (int i = 0; i < basePositionsX.length; i++) {
	    int differenceX = mousePositionX - basePositionsX[i];
	    int differenceY = mousePositionY - basePositionsY[i];

	    if (differenceX <= 0 || diameter <= differenceX
		|| differenceY <= 0 || diameter <= differenceY) {
		continue;
	    }
	    
	    int clickedFigureIndex = backend.figureOnBaseField(i);
	    if (clickedFigureIndex == 99) {
		continue;
	    }
		
	    Figure clickedFigure = backend.figures[clickedFigureIndex];
	    if (clickedFigure.color != backend.activePlayer) {
		continue;
	    }

	    if (clickedFigure.isPlaceable()) {
		backend.moveOutOfBase(clickedFigureIndex);
	    }
	    backend.performUserChoice();
	    return;
	}
    }

    // Even though we neither implement nor use these methods, they
    // are necessary for implementing `java.awt.event.MouseListener`.
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

     //Interface with BackEnd - 1. method: replaceFigures
    public void replaceFigures(){
        Figure[] input = backend.figures;
	int radius = 50;
	    
        for (int i = 0; i < input.length && i < figures.length; i++){
	    String color = FigureColor.YELLOW.getHTML();

	    switch (input[i].color) {
	    case 0: color = FigureColor.YELLOW.getHTML();
		break;
	    case 1: color = FigureColor.GREEN.getHTML();
		break;
	    case 2: color = FigureColor.BLUE.getHTML();
		break;
	    case 3: color = FigureColor.RED.getHTML();
		break;
	    }

	    int x;
	    int y;
	    
            if (input[i].isInBase()) {
		x = basePositionsX[input[i].field];
		y = basePositionsY[input[i].field];
            } else if (input[i].isInHouse()) {
		x = housePositionsX[input[i].field];
		y = housePositionsY[input[i].field];
            } else {
		x = fieldPositionsX[input[i].field];
		y = fieldPositionsY[input[i].field];
            }

	    figures[i] = new Circle(x, y, radius, color);
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
    public void setActivePlayer(){
        add(rollDice);
        userAdvice.setText("Player " + backend.players[backend.activePlayer].name() + " is on the turn, click this button");
        result.setText("");
        repaint();
    }

    /*
      -- DON'T use this method out of constructor - DON'T change any parameters --
      methods sets als parameters for the JLabels and the JButton and in the end adds them to the frame
      -- DON'T use this method out of constructor - DON'T change any parameters --
     */
    private void setJComponentValues(String currentPlayer){
        userAdvice.setText("It's  " + currentPlayer + "s turn, click this button to roll the dice");
        userAdvice.setBounds(970, 22, 550, 62);
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
        userAdvice.setText("It's " + backend.players[backend.activePlayer].name() + "s turn");
        figureChooserPrompt.setText("Choose the figure you want to move!");
        figureChooserPrompt.setBounds(970, 120, 250, 32);
        add(figureChooserPrompt);
    }

    //removing the figureChooserPrompt from the GameBoardGui
    public void removePrompt(){
        remove(figureChooserPrompt);
    }

    public void setBotAdvice(){
        remove(rollDice);
        userAdvice.setText("The bots are moving... Please wait, it will be the next players turn in a few seconds!");
        result.setText("");
        repaint();
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

        forEachloopPaintFields(g, houses);
        forEachloopPaintFields(g, bases);
        forEachloopPaintFields(g, fields);
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
