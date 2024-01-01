package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class GameBoardGui extends JFrame implements MouseListener {

    private JLabel[] figures;
    private JLabel[] houses;
    private JLabel[] bases;
    private JLabel[] fields;

    private final int[] housePositionsX = {433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433, 433, 753, 673, 593,
            513};
    private final int[] housePositionsY = {753, 673, 593, 513, 433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433,
            433};

    private final int[] basePositionsX = {28, 93, 28, 93, 28, 93, 28, 93, 763, 828, 763, 828, 763, 828, 763, 828};
    private final int[] basePositionsY = {828, 828, 763, 763, 93, 93, 28, 28, 93, 93, 28, 28, 828, 828, 763, 763};

    private final int[] fieldPositionsX = {348, 348, 348, 348, 348, 268, 188, 108, 28, 28, 28, 108, 188, 268, 348, 348,
            348, 348, 348, 428, 508, 508, 508, 508, 508, 588, 668, 748, 828, 828, 828, 748, 668, 588, 508, 508, 508,
            508, 508, 428};
    private final int[] fieldPositionsY = {828, 748, 668, 588, 508, 508, 508, 508, 508, 428, 348, 348, 348, 348, 348,
            268, 188, 108, 28, 28, 28, 108, 188, 268, 348, 348, 348, 348, 348, 428, 508, 508, 508, 508, 508, 588, 668,
            748, 828, 828};

    private final int[] figurePositionsHouseX = {433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433, 433, 753, 673,
            593, 513};
    private final int[] figurePositionsHouseY = {728, 648, 568, 488, 408, 408, 408, 408, 88, 168, 248, 328, 408, 408,
            408, 408};

    private final int[] figurePositionsBaseX = {33, 98, 33, 98, 33, 98, 33, 98, 768, 833, 768, 833, 768, 833, 768,
            833};
    private final int[] figurePositionsBaseY = {815, 815, 750, 750, 80, 80, 15, 15, 80, 80, 15, 15, 815, 815, 750, 750};

    private final int[] figurePositionsFieldX = {353, 353, 353, 353, 353, 273, 193, 113, 33, 33, 33, 113, 193, 273,
            353, 353, 353, 353, 353, 433, 513, 513, 513, 513, 513, 593, 673, 753, 833, 833, 833, 753, 673, 593, 513,
            513, 513, 513, 513, 433};
    private final int[] figurePositionsFieldY = {813, 733, 653, 573, 493, 493, 493, 493, 493, 413, 333, 333, 333, 333
            , 333, 253, 173, 93, 13, 13, 13, 93, 173, 253, 333, 333, 333, 333, 333, 413, 493, 493, 493, 493, 493, 573
            , 653, 733, 813, 813};

    private JLabel gameBoardBackground;
    private JLabel userAdvice;
    private JLabel rollDice;
    private JLabel result;
    private JLabel figureChooserPrompt;
    private JLabel rulesAdvice;
    private ImageTextPanel rulesButton;
    private JLabel noSix;
    private ImageTextPanel nextPlayer;
    private BackEnd backend;

    private static final Theme theme = new Theme();
    Logger logger = LoggerFactory.getLoggerInstance();

    public GameBoardGui(String[] playerNames, int numberOfPlayers, boolean fillWithBots) {
        this.backend = new BackEnd(playerNames, numberOfPlayers, fillWithBots);

        figures = new JLabel[16];
        houses = new JLabel[16];
        bases = new JLabel[16];
        fields = new JLabel[40];

        //insert images to the new graphics elements
        for (int i = 0; i < figures.length; i++){
            switch (i){
                case 0, 1, 2, 3 -> figures[i] = new JLabel(readImg("figure-orange"));
                case 4, 5, 6, 7 -> figures[i] = new JLabel(readImg("figure-green"));
                case 8, 9, 10, 11 -> figures[i] = new JLabel(readImg("figure-blue"));
                case 12, 13, 14, 15 -> figures[i] = new JLabel(readImg("figure-red"));
            }
            figures[i].addMouseListener(this);
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

        //configure background
        gameBoardBackground = new JLabel(readImg("board"));
        gameBoardBackground.setBounds(0, 0, 906, 906);

        // Initialize UI Elements
        userAdvice = new JLabel();
        rollDice = new JLabel(readImg("dice-unknown"));
        result = new JLabel();
        figureChooserPrompt = new JLabel();
        rulesAdvice = new JLabel();
        rulesButton = new ImageTextPanel("button-idle", "rules");
        noSix = new JLabel();
        nextPlayer = new ImageTextPanel("button-idle", "next player");

        // Set text
        userAdvice.setText("<html> <body> It's " + backend.getNameOfCurrentPlayer() + "s turn, click this <br> " +
                "button to roll the dice </body> </html>");
        rulesAdvice.setText("<html> <body> Click this button, to view <br> the rules again </body> </html>");
        rulesButton.setText("rules");
        noSix.setText("<html> <body> You didn't get a six. Press this button to <br> move on to the next player " +
                "</body> </html>");

        // Set bounds
        userAdvice.setBounds(930, 22, 450, 64);
        rollDice.setBounds(930, 80, 75, 75);
        rulesAdvice.setBounds(930, 790, 260, 32);
        rulesButton.setBounds(930, 830, 100, 32);
        noSix.setBounds(930, 22, 450, 32);
        nextPlayer.setBounds(930, 80, 100, 32);

        // Add listeners
        rollDice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remove(rollDice);
                boolean humanCanMoveTheirFigures = backend.playerMove();

                if (humanCanMoveTheirFigures) {
                    displayResult(backend.randomNumber);
                    setPromptValues();
                } else {
                    remove(userAdvice);
                    add(noSix);
                    add(nextPlayer);
                    repaint();
                }
            }
        });
        rulesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRules();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                rulesButton.setImage("button-hovered");
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rulesButton.setImage("button-idle");
                repaint();
            }
        });
        nextPlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remove(noSix);
                remove(nextPlayer);
                add(userAdvice);
                repaint();
                executeNextMove();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nextPlayer.setImage("button-hovered");
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nextPlayer.setImage("button-idle");
                repaint();
            }
        });
        replaceFigures();
        // Add UI Elements
        add(gameBoardBackground);
        add(rollDice);
        add(userAdvice);
        add(rulesAdvice);
        add(rulesButton);

        // Display UI
        setTitle("game field");
        setSize(1300, 945);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#6c6f85"));
        setResizable(true);
        setVisible(true);
        logger.info("Displaying Landingpage.");
    }

    public void mouseClicked(MouseEvent e) {
        int clickedFigureIndex = -1;
        for(int i = 0; i < figures.length && clickedFigureIndex == -1; i++) {
            if (e.getSource() == figures[i]) {
                clickedFigureIndex = i;
            }
        }
        logger.info("Clicked Figure " + clickedFigureIndex);
        if (clickedFigureIndex == -1){
            logger.info("Figure movement aborted - no figure clicked");
            return;
        }
        Figure clickedFigure = backend.figures[clickedFigureIndex];
        if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()){
            logger.info("Figure movement aborted - false color selected");
            return;
        }
        if (!clickedFigure.isPlaceable()){
            backend.moveToBase(clickedFigureIndex);
            logger.info("Figure movement aborted - Wrong figure moved (Moving figure to basse...)");
            prepareNextMove();
            return;
        }
        if (clickedFigure.isInBase()){
            backend.moveOutOfBase(clickedFigureIndex);
        } else {
            backend.moveFigure(clickedFigureIndex);
        }
        prepareNextMove();
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
        repaint();
    }

    public void displayResult(int randomNumber){
        switch (randomNumber){
            case 1 -> result = new JLabel(readImg("dice-1"));
            case 2 -> result = new JLabel(readImg("dice-2"));
            case 3 -> result = new JLabel(readImg("dice-3"));
            case 4 -> result = new JLabel(readImg("dice-4"));
            case 5 -> result = new JLabel(readImg("dice-5"));
            case 6 -> result = new JLabel(readImg("dice-6"));
            default -> result = new JLabel(readImg("dice-unknown"));
        }
        result.setBounds(930, 80, 75, 75);
        add(result);
        repaint();
    }

    public void setActivePlayer(){
        add(rollDice);
        userAdvice.setText("<html> <body> It's " + backend.getNameOfCurrentPlayer() + "s turn, click this <br> " +
                "button to roll the dice </body> </html>");
        remove(result);
        repaint();
    }

    public void setPromptValues(){
        userAdvice.setText("It's " + backend.getNameOfCurrentPlayer() + "s turn");
        figureChooserPrompt.setText("Choose the figure you want to move!");
        figureChooserPrompt.setBounds(930, 160, 350, 32);
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

    private void openRules(){
        setVisible(false);
        new Rules(this);
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
