package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GameBoardGui extends JFrame implements ActionListener, MouseListener {
    private enum FieldColor {
	YELLOW("#FFC957"),
	GREEN("#2A914E"),
	BLUE("#1E32FF"),
	RED("#CC0000"),
	GRAY("#CCCCCC");

	private final String hexCode;

	FieldColor(String hexCode) {
	    this.hexCode = hexCode;
	}

	public String getHexCode() {
	    return hexCode;
	}
    }
    private enum FigureColor {
	YELLOW("#FFFF00"),
	GREEN("#00CC00"),
	BLUE("#3C93FF"),
	RED("#FF0000");

	private final String hexCode;
	FigureColor(String hexCode) {
	    this.hexCode = hexCode;
	}

	public String getHexCode() {
	    return hexCode;
	}
    }


    private Circle[] figuresOld;
    private Circle[] housesOld;
    private JLabel[] houses;
    private Circle[] basesOld;
    private JLabel[] bases;
    private Circle[] fieldsOld;
    private JLabel[] fields;

    private final int[] housePositionsX = {445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445, 765, 685, 605, 525};
    private final int[] housePositionsY = {765, 685, 605, 525, 445, 445, 445, 445, 125, 205, 285, 365, 445, 445, 445, 445};

    private final int[] basePositionsX = {45, 110, 45, 110, 45, 110, 45, 110, 780, 845, 780, 845, 780, 845, 780, 845};
    private final int[] basePositionsY = {845, 845, 780, 780, 110, 110, 45, 45, 110, 110, 45, 45, 845, 845, 780, 780};

    private final int[] fieldPositionsX = {360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845, 840, 765, 685, 605, 525, 525, 525, 525, 525, 445};
    private final int[] fieldPositionsY = {840, 765, 685, 605, 525, 525, 525, 525, 525, 445, 360, 365, 365, 365, 365, 285, 205, 125, 45, 45, 40, 125, 205, 285, 365, 365, 365, 365, 365, 445, 520, 525, 525, 525, 525, 605, 685, 765, 845, 845};

    JLabel userAdvice;
    JButton rollDice;
    JLabel result;
    JLabel figureChooserPrompt;
    JLabel rulesAdvice;
    JButton rulesButton;
    JLabel noSix;
    JButton nextPlayer;
    BackEnd backend;
    Logger logger = LoggerFactory.getLoggerInstance();

    public GameBoardGui(String[] playerNames, int numberOfPlayers, boolean fillWithBots) {
        this.backend = new BackEnd(playerNames, numberOfPlayers, fillWithBots);

        figuresOld = new Circle[16];
        housesOld = new Circle[16];
        houses = new JLabel[16];
        basesOld = new Circle[16];
        bases = new JLabel[16];
        fieldsOld = new Circle[40];
        fields = new JLabel[40];

        replaceFigures();
        int diameter = 50;

        for (int i = 0; i < housesOld.length; i++) {
            int x = housePositionsX[i];
            int y = housePositionsY[i];
            housesOld[i] = new Circle(x, y, diameter, FieldColor.YELLOW.getHexCode());
        }
        for (int i = 0; i < basesOld.length; i++) {
            int x = basePositionsX[i];
            int y = basePositionsY[i];
            basesOld[i] = new Circle(x, y, diameter, FieldColor.YELLOW.getHexCode());
        }
        for (int i = 0; i < fieldsOld.length; i++) {
            int x = fieldPositionsX[i];
            int y = fieldPositionsY[i];
            fieldsOld[i] = new Circle(x, y, diameter, FieldColor.GRAY.getHexCode());
        }

        //insert images to the new graphics elements
        for (int i = 0; i < houses.length; i++){
            int x = housePositionsX[i] -3;
            int y = housePositionsY[i] -25;
            switch (i){
                case 0, 1, 2, 3 -> houses[i] = new JLabel(readImg("field-orange-inner"));
                case 4, 5, 6, 7 -> houses[i] = new JLabel(readImg("field-green-inner"));
                case 8, 9, 10, 11 -> houses[i] = new JLabel(readImg("field-blue-inner"));
                case 12, 13, 14, 15 -> houses[i] = new JLabel(readImg("field-red-inner"));
            }
            houses[i].setBounds(x, y, 40, 40);
            add(houses[i]);
        }
        for (int i = 0; i < bases.length; i++){
            int x = basePositionsX[i]-8;
            int y = basePositionsY[i]-30;
            switch (i){
                case 0, 1, 2, 3 -> bases[i] = new JLabel(readImg("field-orange"));
                case 4, 5, 6, 7 -> bases[i] = new JLabel(readImg("field-green"));
                case 8, 9, 10, 11 -> bases[i] = new JLabel(readImg("field-blue"));
                case 12, 13, 14, 15 -> bases[i] = new JLabel(readImg("field-red"));
            }
            bases[i].setBounds(x, y, 50, 50);
            add(bases[i]);
        }
        for (int i = 0; i < fields.length; i++){
            int x = fieldPositionsX[i] -8;
            int y = fieldPositionsY[i] -30;
            switch (i){
                case 0 -> fields[i] = new JLabel(readImg("field-orange"));
                case 10 -> fields[i] = new JLabel(readImg("field-green"));
                case 20 -> fields[i] = new JLabel(readImg("field-blue"));
                case 30 -> fields[i] = new JLabel(readImg("field-red"));
                default -> fields[i] = new JLabel(readImg("field-white"));
            }
            fields[i].setBounds(x, y, 50, 50);
            add(fields[i]);
        }

        for (int i = 0; i < 4; i++) {
            housesOld[i + 4].setColor(FieldColor.GREEN.getHexCode());
            housesOld[i + 8].setColor(FieldColor.BLUE.getHexCode());
            housesOld[i + 12].setColor(FieldColor.RED.getHexCode());

            basesOld[i + 4].setColor(FieldColor.GREEN.getHexCode());
            basesOld[i + 8].setColor(FieldColor.BLUE.getHexCode());
            basesOld[i + 12].setColor(FieldColor.RED.getHexCode());
        }

        //Set color for start fields
        fieldsOld[0].setColor(FieldColor.YELLOW.getHexCode());
        fieldsOld[10].setColor(FieldColor.GREEN.getHexCode());
        fieldsOld[20].setColor(FieldColor.BLUE.getHexCode());
        fieldsOld[30].setColor(FieldColor.RED.getHexCode());

        // Initialize UI Elements
        userAdvice = new JLabel();
        rollDice = new JButton();
        result = new JLabel();
        figureChooserPrompt = new JLabel();
        rulesAdvice = new JLabel();
        rulesButton = new JButton();
        noSix = new JLabel();
        nextPlayer = new JButton();

        // Set text
        userAdvice.setText("It's " + backend.getNameOfCurrentPlayer() + "s turn, click this button to roll the dice");
        rollDice.setText("roll the dice");
        rulesAdvice.setText("Click this button, to view the rules again");
        rulesButton.setText("rules");
        noSix.setText("<html> <body> You didn't got a six. Press this button to move on to <br> the next player </body> " +
		      "</html>");
        nextPlayer.setText("next player");

        // Set bounds
        userAdvice.setBounds(970, 22, 450, 64);
        rollDice.setBounds(970, 80, 120, 32);
        result.setBounds(970, 80, 100, 32);
        rulesAdvice.setBounds(980, 820, 250, 32);
        rulesButton.setBounds(980, 860, 120, 32);
        noSix.setBounds(970, 22, 450, 32);
        nextPlayer.setBounds(970, 80, 120, 32);

        // Add listeners
        rollDice.addActionListener(this);
        rulesButton.addActionListener(this);
        nextPlayer.addActionListener(this);
        addMouseListener(this);

        // Add UI Elements
        add(rollDice);
        add(userAdvice);
        add(result);
        add(rulesAdvice);
        add(rulesButton);

        // Display UI
        setTitle("game field");
        setSize(1300, 940);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#6c6f85"));
        setResizable(true);
        setVisible(true);
        logger.info("Displaying Landingpage.");
    }

    public void actionPerformed(ActionEvent e) {
        if (rollDice == e.getSource()) {
            remove(rollDice);
            boolean humanCanMoveTheirFigures = backend.playerMove();

            if (humanCanMoveTheirFigures) {
                displayResult(backend.randomNumber);
                setPromptValues();
            } else {
                this.remove(userAdvice);
                this.add(noSix);
                this.add(nextPlayer);
                repaint();
            }
        } else if (rulesButton == e.getSource()) {
            setVisible(false);
            new Rules(this);
        } else if (nextPlayer == e.getSource()) {
            this.remove(noSix);
            this.remove(nextPlayer);
            this.add(userAdvice);
            repaint();
            executeNextMove();
        }
    }

    public void mouseClicked(MouseEvent e) {
        int mousePositionX = e.getX();
        int mousePositionY = e.getY();
        int diameter = 50;

	String message = String.format("Mouse clicked at { x: %3d, y: %3d, hit_figure: %b}", mousePositionX, mousePositionY, true);

        for (int i = 0; i < fieldPositionsX.length; i++) {
            int differenceX = mousePositionX - fieldPositionsX[i];
            int differenceY = mousePositionY - fieldPositionsY[i];

            if (differenceX <= 0 || diameter <= differenceX || differenceY <= 0 || diameter <= differenceY) {
                continue;
            }

            int clickedFigureIndex = backend.figureOnField(i);
            if (clickedFigureIndex == -1) {
                continue;
            }

            Figure clickedFigure = backend.figures[clickedFigureIndex];
            if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
                continue;
            }

            if (clickedFigure.isPlaceable()) {
                backend.moveFigure(clickedFigureIndex);
            } else {
                backend.moveToBase(clickedFigureIndex);
            }
            prepareNextMove();
	    logger.debug(String.format("Mouse clicked at { x: %3d, y: %3d, hit_figure: %b}", mousePositionX, mousePositionY, true));
            return;
        }

        for (int i = 0; i < housePositionsX.length; i++) {
            int differenceX = mousePositionX - housePositionsX[i];
            int differenceY = mousePositionY - housePositionsY[i];

            if (differenceX <= 0 || diameter <= differenceX || differenceY <= 0 || diameter <= differenceY) {
                continue;
            }

            int clickedFigureIndex = backend.figureOnHouseField(i);
            if (clickedFigureIndex == -1) {
                continue;
            }

            Figure clickedFigure = backend.figures[clickedFigureIndex];
            if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
                continue;
            }
            if (clickedFigure.isFinished()){
                continue;
            }
            if (clickedFigure.isPlaceable()) {
                backend.moveFigure(clickedFigureIndex);
            } else {
                backend.moveToBase(clickedFigureIndex);
            }
            prepareNextMove();
	    logger.debug(String.format("Mouse clicked at { x: %3d, y: %3d, hit_figure: %b}", mousePositionX, mousePositionY, true));
            return;
        }

        for (int i = 0; i < basePositionsX.length; i++) {
            int differenceX = mousePositionX - basePositionsX[i];
            int differenceY = mousePositionY - basePositionsY[i];

            if (differenceX <= 0 || diameter <= differenceX || differenceY <= 0 || diameter <= differenceY) {
                continue;
            }

            int clickedFigureIndex = backend.figureOnBaseField(i);
            if (clickedFigureIndex == -1) {
                continue;
            }

            Figure clickedFigure = backend.figures[clickedFigureIndex];
            if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
                continue;
            }

            if (clickedFigure.isPlaceable()) {
                backend.moveOutOfBase(clickedFigureIndex);
            }
            prepareNextMove();
	    logger.debug(String.format("Mouse clicked at { x: %3d, y: %3d, hit_figure: %b}", mousePositionX, mousePositionY, true));
            return;
        }
	logger.debug(String.format("Mouse clicked at { x: %3d, y: %3d, hit_figure: %b}", mousePositionX, mousePositionY, false));
    }

    private void prepareNextMove() {
        backend.disablePlacementForAllFigures();

        removePrompt();
        replaceFigures();
        displayWinWindowIfNecessary();
        executeNextMove();
    }

    public void displayWinWindowIfNecessary() {
        String nameOfWinner = backend.getNameOfWinner();

        if (nameOfWinner == null) {
            return;
        }

        new WinWindow(nameOfWinner);
        setVisible(false);
    }

    private void executeNextMove() {
        backend.setNewCurrentPlayerIfNecessary();
        int playerState = backend.getPlayerStateOfCurrentPlayer();

        if (playerState == 1) {
            setBotAdvice();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean botMovedItsFigures = backend.botMove();
            if (botMovedItsFigures) {
                replaceFigures();
                displayWinWindowIfNecessary();
            }
            executeNextMove();
        } else if (playerState == 0) {
            setActivePlayer();
        } else {
            executeNextMove();
        }
    }

    // Even though we neither implement nor use these methods, they
    // are necessary for implementing `java.awt.event.MouseListener`.
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void replaceFigures(){
        Figure[] input = backend.figures;
        int diameter = 50;

        for (int i = 0; i < input.length && i < figuresOld.length; i++){
            String color = switch (input[i].color) {
	    case 0 -> FigureColor.YELLOW.getHexCode();
	    case 1 -> FigureColor.GREEN.getHexCode();
	    case 2 -> FigureColor.BLUE.getHexCode();
	    case 3 -> FigureColor.RED.getHexCode();
	    default -> throw new IllegalStateException("Unexpected value: " + input[i].color);
            };

            int x;
            int y;

            if (input[i].isInBase()) {
                x = basePositionsX[input[i].field];
                y = basePositionsY[input[i].field];
            } else if (input[i].isInHouse() || input[i].isFinished()) {
                x = housePositionsX[input[i].field];
                y = housePositionsY[input[i].field];
            } else {
                x = fieldPositionsX[input[i].field];
                y = fieldPositionsY[input[i].field];
            }
            figuresOld[i] = new Circle(x, y, diameter, color);
        }
        repaint();
    }

    public void displayResult(int randomNumber){
        result.setText("Result: " + randomNumber);
        repaint();
    }

    public void setActivePlayer(){
        add(rollDice);
        userAdvice.setText("Player " + backend.getNameOfCurrentPlayer() + " is on the turn, click this button");
        result.setText("");
        repaint();
    }

    public void setPromptValues(){
        userAdvice.setText("It's " + backend.getNameOfCurrentPlayer() + "s turn");
        figureChooserPrompt.setText("Choose the figure you want to move!");
        figureChooserPrompt.setBounds(970, 120, 250, 32);
        add(figureChooserPrompt);
    }

    public void removePrompt(){
        remove(figureChooserPrompt);
    }

    public void setBotAdvice(){
        remove(rollDice);
        userAdvice.setText("The bots are moving... Please wait, it will be the next players turn in a few seconds!");
        result.setText("");
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        paintFields(g, housesOld);
        paintFields(g, basesOld);
        paintFields(g, fieldsOld);
        paintFigures(g, figuresOld);
    }

    private void paintFields(Graphics g, Circle[] circles) {
        for (Circle circle : circles) {
            int x = circle.getX();
            int y = circle.getY();
            int diameter = circle.getDiameter();
            Color color = circle.getColor();

            g.setColor(color);
            g.fillOval(x, y, diameter, diameter);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, diameter, diameter);
        }
    }

    private void paintFigures(Graphics g, Circle[] ovals){
        for (Circle oval : ovals){
            int x = oval.getX() + oval.getDiameter() / 4;
            int y = oval.getY();
            int diameter = oval.getDiameter();
            int radius = oval.getDiameter() / 2;
            Color color = oval.getColor();

            g.setColor(color);
            g.fillOval(x, y, radius, diameter);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, radius, diameter);
        }
    }

    private ImageIcon readImg (String imageName){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("res/"+imageName+".png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ImageIcon(Objects.requireNonNull(img));
    }
}
