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

import static java.lang.Thread.sleep;

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
    private JLabel[] figures;
    private JLabel[] houses;
    private JLabel[] bases;
    private JLabel[] fields;

    private final int[] housePositionsX = {442, 442, 442, 442, 122, 202, 282, 362, 442, 442, 442, 442, 762, 682, 602, 522};
    private final int[] housePositionsY = {740, 660, 580, 500, 420, 420, 420, 420, 100, 180, 260, 340, 420, 420, 420, 420};

    private final int[] basePositionsX = {37, 102, 37, 102, 37, 102, 37, 102, 772, 837, 772, 837, 772, 837, 772, 837};
    private final int[] basePositionsY = {815, 815, 750, 750, 80, 80, 15, 15, 80, 80, 15, 15, 815, 815, 750, 750};

    private final int[] fieldPositionsX = {352, 357, 357, 357, 357, 277, 197, 117, 37, 37, 32, 117, 197, 277, 357, 357, 357, 357, 357, 437, 512, 517, 517, 517, 517, 597, 677, 757, 837, 837, 832, 757, 677, 597, 517, 517, 517, 517, 517, 437};
    private final int[] fieldPositionsY = {810, 735, 655, 575, 495, 495, 495, 495, 495, 415, 330, 335, 335, 335, 335, 255, 175, 95, 15, 15, 10, 95, 175, 255, 335, 335, 335, 335, 335, 415, 490, 495, 495, 495, 495, 575, 655, 735, 815, 815};

    private final int[] figurePositionsHouseX = {442, 442, 442, 442, 122, 202, 282, 362, 442, 442, 442, 442, 762, 682, 602, 522};
    private final int[] figurePositionsHouseY = {715, 635, 555, 475, 395, 395, 395, 395, 75, 155, 235, 315, 395, 395, 395, 395};

    private final int[] figurePositionsBaseX = {42, 107, 42, 107, 42, 107, 42, 107, 777, 842, 777, 842, 777, 842, 777, 842};
    private final int[] figurePositionsBaseY = {802, 802, 737, 737, 67, 67, 2, 2, 67, 67, 2, 2, 802, 802, 737, 737};

    private final int[] figurePositionsFieldX = {357, 362, 362, 362, 362, 282, 202, 122, 42, 42, 37, 122, 202, 282, 362, 362, 362, 362, 362, 442, 517, 522, 522, 522, 522, 602, 682, 762, 842, 842, 837, 762, 682, 602, 522, 522, 522, 522, 522, 442};
    private final int[] figurePositionsFieldY = {795, 720, 640, 560, 480, 480, 480, 480, 480, 400, 315, 320, 320, 320, 320, 240, 160, 80, 0, 0, -5, 80, 160, 240, 320, 320, 320, 320, 320, 400, 475, 480, 480, 480, 480, 560, 640, 720, 800, 800};

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
        figures = new JLabel[16];
        houses = new JLabel[16];
        bases = new JLabel[16];
        fields = new JLabel[40];

        int diameter = 50;

        //insert images to the new graphics elements
        for (int i = 0; i < figures.length; i++){
            switch (i){
                case 0, 1, 2, 3 -> figures[i] = new JLabel(readImg("figure-orange"));
                case 4, 5, 6, 7 -> figures[i] = new JLabel(readImg("figure-green"));
                case 8, 9, 10, 11 -> figures[i] = new JLabel(readImg("figure-blue"));
                case 12, 13, 14, 15 -> figures[i] = new JLabel(readImg("figure-red"));
            }
            add(figures[i]);
        }
        for (int i = 0; i < houses.length; i++){
            int x = housePositionsX[i];
            int y = housePositionsY[i];
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
            int x = basePositionsX[i];
            int y = basePositionsY[i];
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
            int x = fieldPositionsX[i];
            int y = fieldPositionsY[i];
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

        replaceFigures();

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
            int differenceX = mousePositionX - fieldPositionsX[i] - 3;
            int differenceY = mousePositionY - fieldPositionsY[i] - 20;

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
            int differenceX = mousePositionX - basePositionsX[i] - 5;
            int differenceY = mousePositionY - basePositionsY[i] - 30;

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
                sleep(1000);
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
        int dimensionX = 39;
        int dimensionY = 56;

        for (int i = 0; i < input.length && i < figures.length; i++){
            int x;
            int y;

            if (input[i].isInBase()) {
                x = figurePositionsBaseX[input[i].field];
                y = figurePositionsBaseY[input[i].field];
            } else if (input[i].isInHouse() || input[i].isFinished()) {
                x = figurePositionsHouseX[input[i].field];
                y = figurePositionsHouseY[input[i].field];
            } else {
                x = figurePositionsFieldX[input[i].field];
                y = figurePositionsFieldY[input[i].field];
            }
            figures[i].setBounds(x, y, dimensionX, dimensionY);
        }

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
