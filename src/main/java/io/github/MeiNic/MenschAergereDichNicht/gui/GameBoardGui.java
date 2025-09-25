// This file is part of MenschAergereDichNicht.
// Copyright (C) 2023-2025 MeiNic, TastingComb and contributors.

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.MeiNic.MenschAergereDichNicht.gui;

import io.github.MeiNic.MenschAergereDichNicht.BackEnd;
import io.github.MeiNic.MenschAergereDichNicht.figure.Figure;
import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;
import io.github.MeiNic.MenschAergereDichNicht.stateMashine.Event;
import io.github.MeiNic.MenschAergereDichNicht.stateMashine.State;
import io.github.MeiNic.MenschAergereDichNicht.stateMashine.StateMashine;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class GameBoardGui {

    private final JLabel[] figures;
    private final JLabel[] houses;
    private final JLabel[] bases;
    private final JLabel[] fields;

    private static final int[] HOUSE_POSITIONS_X = {
        433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433, 433, 753, 673, 593, 513
    };
    private static final int[] HOUSE_POSITIONS_Y = {
        753, 673, 593, 513, 433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433, 433
    };

    private static final int[] BASE_POSITIONS_X = {
        28, 93, 28, 93, 28, 93, 28, 93, 763, 828, 763, 828, 763, 828, 763, 828
    };
    private static final int[] BASE_POSITIONS_Y = {
        828, 828, 763, 763, 93, 93, 28, 28, 93, 93, 28, 28, 828, 828, 763, 763
    };

    private static final int[] FIELD_POSITIONS_X = {
        348, 348, 348, 348, 348, 268, 188, 108, 28, 28, 28, 108, 188, 268, 348, 348, 348, 348, 348,
        428, 508, 508, 508, 508, 508, 588, 668, 748, 828, 828, 828, 748, 668, 588, 508, 508, 508,
        508, 508, 428
    };
    private static final int[] FIELD_POSITIONS_Y = {
        828, 748, 668, 588, 508, 508, 508, 508, 508, 428, 348, 348, 348, 348, 348, 268, 188, 108,
        28, 28, 28, 108, 188, 268, 348, 348, 348, 348, 348, 428, 508, 508, 508, 508, 508, 588, 668,
        748, 828, 828
    };

    private static final int[] FIGURE_POSITIONS_HOUSE_X = {
        433, 433, 433, 433, 113, 193, 273, 353, 433, 433, 433, 433, 753, 673, 593, 513
    };
    private static final int[] FIGURE_POSITIONS_HOUSE_Y = {
        728, 648, 568, 488, 408, 408, 408, 408, 88, 168, 248, 328, 408, 408, 408, 408
    };

    private static final int[] FIGURE_POSITIONS_BASE_X = {
        33, 98, 33, 98, 33, 98, 33, 98, 768, 833, 768, 833, 768, 833, 768, 833
    };
    private static final int[] FIGURE_POSITIONS_BASE_Y = {
        815, 815, 750, 750, 80, 80, 15, 15, 80, 80, 15, 15, 815, 815, 750, 750
    };

    private static final int[] FIGURE_POSITIONS_FIELD_X = {
        353, 353, 353, 353, 353, 273, 193, 113, 33, 33, 33, 113, 193, 273, 353, 353, 353, 353, 353,
        433, 513, 513, 513, 513, 513, 593, 673, 753, 833, 833, 833, 753, 673, 593, 513, 513, 513,
        513, 513, 433
    };
    private static final int[] FIGURE_POSITIONS_FIELD_Y = {
        813, 733, 653, 573, 493, 493, 493, 493, 493, 413, 333, 333, 333, 333, 333, 253, 173, 93, 13,
        13, 13, 93, 173, 253, 333, 333, 333, 333, 333, 413, 493, 493, 493, 493, 493, 573, 653, 733,
        813, 813
    };

    private final JLabel gameBoardBackground;
    private final JLabel rollDiceAdvice;
    private final JLabel rollDice;
    private JLabel result;
    private final JLabel moveFigureAdvice1;
    private final JLabel moveFigureAdvice2;
    private final JLabel rulesAdvice;
    private final ImageTextPanel rulesButton;
    private final JLabel noSixAdvice;
    private final ImageTextPanel noSixNextPlayerButton;
    private final JLabel botsMoveAdvice;
    private final ImageTextPanel botsMoveButton;
    private final JLabel wrongFigureMoved;

    private final BackEnd backend;

    private final JPanel rollDicePanel;
    private final JPanel moveFigurePanel;
    private final JPanel noSixPanel;
    private final JPanel botsMovePanel;

    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    protected JFrame frame;

    private StateMashine stateMashine;

    public GameBoardGui(BackEnd backendInput) {
        this.backend = backendInput;
        this.stateMashine = backendInput.stateMashine;
        frame = new JFrame();

        figures = new JLabel[16];
        houses = new JLabel[16];
        bases = new JLabel[16];
        fields = new JLabel[40];

        // insert images to the new graphics elements
        for (int i = 0; i < figures.length; i++) {
            switch (i) {
                case 0, 1, 2, 3 ->
                        figures[i] = new JLabel(Resources.loadImageIcon("figure-orange"));
                case 4, 5, 6, 7 -> figures[i] = new JLabel(Resources.loadImageIcon("figure-green"));
                case 8, 9, 10, 11 ->
                        figures[i] = new JLabel(Resources.loadImageIcon("figure-blue"));
                case 12, 13, 14, 15 ->
                        figures[i] = new JLabel(Resources.loadImageIcon("figure-red"));
            }
            figures[i].addMouseListener(new MyMouseListener());
            frame.add(figures[i]);
        }
        for (int i = 0; i < houses.length; i++) {
            int x = HOUSE_POSITIONS_X[i];
            int y = HOUSE_POSITIONS_Y[i];
            switch (i) {
                case 0, 1, 2, 3 ->
                        houses[i] = new JLabel(Resources.loadImageIcon("field-orange-inner"));
                case 4, 5, 6, 7 ->
                        houses[i] = new JLabel(Resources.loadImageIcon("field-green-inner"));
                case 8, 9, 10, 11 ->
                        houses[i] = new JLabel(Resources.loadImageIcon("field-blue-inner"));
                case 12, 13, 14, 15 ->
                        houses[i] = new JLabel(Resources.loadImageIcon("field-red-inner"));
            }
            houses[i].setBounds(x, y, 40, 40);
            frame.add(houses[i]);
        }
        for (int i = 0; i < bases.length; i++) {
            int x = BASE_POSITIONS_X[i];
            int y = BASE_POSITIONS_Y[i];
            switch (i) {
                case 0, 1, 2, 3 -> bases[i] = new JLabel(Resources.loadImageIcon("field-orange"));
                case 4, 5, 6, 7 -> bases[i] = new JLabel(Resources.loadImageIcon("field-green"));
                case 8, 9, 10, 11 -> bases[i] = new JLabel(Resources.loadImageIcon("field-blue"));
                case 12, 13, 14, 15 -> bases[i] = new JLabel(Resources.loadImageIcon("field-red"));
            }
            bases[i].setBounds(x, y, 50, 50);
            frame.add(bases[i]);
        }
        for (int i = 0; i < fields.length; i++) {
            int x = FIELD_POSITIONS_X[i];
            int y = FIELD_POSITIONS_Y[i];
            switch (i) {
                case 0 -> fields[i] = new JLabel(Resources.loadImageIcon("field-orange"));
                case 10 -> fields[i] = new JLabel(Resources.loadImageIcon("field-green"));
                case 20 -> fields[i] = new JLabel(Resources.loadImageIcon("field-blue"));
                case 30 -> fields[i] = new JLabel(Resources.loadImageIcon("field-red"));
                default -> fields[i] = new JLabel(Resources.loadImageIcon("field-white"));
            }
            fields[i].setBounds(x, y, 50, 50);
            frame.add(fields[i]);
        }

        // configure background
        gameBoardBackground = new JLabel(Resources.loadImageIcon("board"));
        gameBoardBackground.setBounds(0, 0, 906, 906);

        // Initialize UI Elements
        rollDiceAdvice = new JLabel();
        rollDice = new JLabel(Resources.loadImageIcon("dice-unknown"));
        result = new JLabel();
        moveFigureAdvice1 = new JLabel();
        moveFigureAdvice2 = new JLabel();
        rulesAdvice = new JLabel();
        rulesButton = new ImageTextPanel("button-idle", "rules");
        noSixAdvice = new JLabel();
        noSixNextPlayerButton = new ImageTextPanel("button-idle", "next player");
        botsMoveAdvice = new JLabel();
        botsMoveButton = new ImageTextPanel("button-idle", "let bots move");
        wrongFigureMoved = new JLabel();

        // Set text
        rollDiceAdvice.setText(
                "<html> <body> It's "
                        + backend.getNameOfCurrentPlayer()
                        + "s turn, click this <br> "
                        + "button to roll the dice </body> </html>");
        rulesAdvice.setText(
                "<html> <body> Click this button, to view <br> the rules again </body> </html>");
        moveFigureAdvice1.setText(
                "<html> <body> It's "
                        + backend.getNameOfCurrentPlayer()
                        + "s turn </body> </html>");
        moveFigureAdvice2.setText(
                "<html> <body> Choose the figure you want to move! </body> </html>");
        noSixAdvice.setText(
                "<html> <body> You didn't get a six. Press this button to <br> move on to the next"
                        + " player </body> </html>");
        botsMoveAdvice.setText(
                "<html> <body> </body> Click the button to let one bot make his move. </body>"
                        + " </html>");
        noSixNextPlayerButton.setText(
                "<html> <body> You have to move an other figure. Please try again. </body>"
                        + " </html>");

        // Set bounds
        rollDiceAdvice.setBounds(0, 0, 450, 64);
        rollDice.setBounds(0, 58, 75, 75);
        rulesAdvice.setBounds(930, 790, 260, 32);
        rulesButton.setBounds(930, 830, 100, 32);
        moveFigureAdvice1.setBounds(0, 0, 450, 64);
        moveFigureAdvice2.setBounds(0, 138, 350, 32);
        noSixAdvice.setBounds(0, 0, 450, 32);
        noSixNextPlayerButton.setBounds(0, 58, 140, 32);
        botsMoveAdvice.setBounds(0, 0, 450, 32);
        botsMoveButton.setBounds(0, 58, 160, 32);
        wrongFigureMoved.setBounds(930, 160, 350, 32);

        // Add listeners
        rollDice.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        buttonActionMouseKey();
                    }
                });
        rulesButton.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openRules();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        rulesButton.setImage("button-hovered");
                        frame.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        rulesButton.setImage("button-idle");
                        frame.repaint();
                    }
                });
        noSixNextPlayerButton.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        buttonActionMouseKey();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        noSixNextPlayerButton.setImage("button-hovered");
                        frame.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        noSixNextPlayerButton.setImage("button-idle");
                        frame.repaint();
                    }
                });

        botsMoveButton.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        buttonActionMouseKey();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        botsMoveButton.setImage("button-hovered");
                        frame.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        botsMoveButton.setImage("button-idle");
                        frame.repaint();
                    }
                });

        // Initialize Panels
        rollDicePanel = new JPanel();
        rollDicePanel.setLayout(null);
        rollDicePanel.setBounds(930, 22, 450, 120);
        rollDicePanel.add(rollDiceAdvice);
        rollDicePanel.add(rollDice);

        moveFigurePanel = new JPanel();
        moveFigurePanel.setLayout(null);
        moveFigurePanel.setBounds(930, 22, 450, 320);
        moveFigurePanel.add(moveFigureAdvice1);
        moveFigurePanel.add(moveFigureAdvice2);

        noSixPanel = new JPanel();
        noSixPanel.setLayout(null);
        noSixPanel.setBounds(930, 22, 450, 120);
        noSixPanel.add(noSixAdvice);
        noSixPanel.add(noSixNextPlayerButton);

        botsMovePanel = new JPanel();
        botsMovePanel.setLayout(null);
        botsMovePanel.setBounds(930, 22, 450, 120);
        botsMovePanel.add(botsMoveAdvice);
        botsMovePanel.add(botsMoveButton);

        replaceFigures();
        // Add UI Elements
        frame.add(gameBoardBackground);
        frame.add(rulesAdvice);
        frame.add(rulesButton);
        changeUserAdvice();

        // Display UI
        frame.setTitle("game field");
        frame.setSize(1300, 945);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                            buttonActionMouseKey();
                        }
                    }
                });
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#6c6f85"));
        frame.setResizable(true);
        frame.setVisible(true);
        LOGGER.info("Displaying Gui for game board");
    }

    protected void changeUserAdvice() {
        frame.remove(rollDicePanel);
        frame.remove(result);
        frame.remove(moveFigurePanel);
        frame.remove(noSixPanel);
        frame.remove(botsMovePanel);
        frame.remove(wrongFigureMoved);
        switch (stateMashine.getCurrentState()) {
            case WAITING_TO_ROLL_DICE -> {
                rollDiceAdvice.setText(
                        "<html> <body> It's "
                                + backend.getNameOfCurrentPlayer()
                                + "s turn, click this <br> button to roll the dice </body>"
                                + " </html>");
                frame.add(rollDicePanel);
            }
            case NO_MOVES_AVAILABLE -> frame.add(noSixPanel);
            case BOTS_TURN -> frame.add(botsMovePanel);
            case MOVED_WRONG_PIECE -> {
                frame.add(result);
                frame.add(moveFigurePanel);
                frame.add(wrongFigureMoved);
            }
            case MOVING_PIECE -> {
                frame.add(result);
                moveFigureAdvice1.setText(
                        "<html> <body> It's "
                                + backend.getNameOfCurrentPlayer()
                                + "s turn </body> </html>");
                frame.add(moveFigurePanel);
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    protected void afterHumanmoveLivecycle() {
        backend.disablePlacementForAllFigures();
        frame.revalidate();
        replaceFigures();
        executeNextMove();
    }

    private void displayWinWindow() {
        new WinWindow(backend.getNameOfWinner().get());
        frame.setVisible(false);
    }

    protected void executeNextMove() {
        backend.setNewCurrentPlayerIfNecessary();
        switch (backend.getPlayerStateOfCurrentPlayer()) {
            case 0 -> setActivePlayer();
            case 1 -> {
                setBotAdvice();
                backend.botMove();
                replaceFigures();
                if (backend.getNameOfWinner().isPresent()) {
                    displayWinWindow();
                    return;
                }
                executeNextMove();
            }
            default -> executeNextMove();
        }
    }

    protected void replaceFigures() {
        Figure[] input = backend.figures;
        int dimensionX = 39;
        int dimensionY = 56;

        for (int i = 0; i < input.length && i < figures.length; i++) {
            int x;
            int y;

            if (input[i].isInBase()) {
                x = FIGURE_POSITIONS_BASE_X[input[i].getField()];
                y = FIGURE_POSITIONS_BASE_Y[input[i].getField()];
            } else if (input[i].isInHouse()) {
                x = FIGURE_POSITIONS_HOUSE_X[input[i].getField()];
                y = FIGURE_POSITIONS_HOUSE_Y[input[i].getField()];
            } else {
                x = FIGURE_POSITIONS_FIELD_X[input[i].getField()];
                y = FIGURE_POSITIONS_FIELD_Y[input[i].getField()];
            }
            figures[i].setBounds(x, y, dimensionX, dimensionY);
        }
        frame.repaint();
    }

    void displayResult(int randomNumber) {
        switch (randomNumber) {
            case 1 -> result = new JLabel(Resources.loadImageIcon("dice-1"));
            case 2 -> result = new JLabel(Resources.loadImageIcon("dice-2"));
            case 3 -> result = new JLabel(Resources.loadImageIcon("dice-3"));
            case 4 -> result = new JLabel(Resources.loadImageIcon("dice-4"));
            case 5 -> result = new JLabel(Resources.loadImageIcon("dice-5"));
            case 6 -> result = new JLabel(Resources.loadImageIcon("dice-6"));
            default -> result = new JLabel(Resources.loadImageIcon("dice-unknown"));
        }
    }

    protected void setActivePlayer() {
        frame.add(rollDice);
        rollDiceAdvice.setText(
                "<html> <body> It's "
                        + backend.getNameOfCurrentPlayer()
                        + "s turn, click this <br> "
                        + "button to roll the dice </body> </html>");
        frame.remove(result);
        frame.repaint();
    }

    void setPromptValues() {
        // move to other jlabel
        rollDiceAdvice.setText("It's " + backend.getNameOfCurrentPlayer() + "s turn");
        moveFigureAdvice2.setText(
                "<html> <body> Choose the figure you want to move! </body> </html>");
        moveFigureAdvice2.setBounds(930, 160, 350, 32);
        frame.add(moveFigureAdvice2);
    }

    private void removePrompt() {
        frame.remove(moveFigureAdvice2);
    }

    private void setBotAdvice() {
        frame.remove(rollDice);
        rollDiceAdvice.setText(
                "The bots are moving... Please wait, it will be the next players turn in a few"
                        + " seconds!");
        result.setText("");
        frame.repaint();
    }

    private void openRules() {
        frame.setVisible(false);
        new Rules(frame);
    }

    protected void buttonActionMouseKey() {
        LOGGER.debug("Current state:" + stateMashine.getCurrentState());
        switch (stateMashine.getCurrentState()) {
            case WAITING_TO_ROLL_DICE -> {
                boolean humanCanMoveTheirFigures = backend.playerMove();
                if (humanCanMoveTheirFigures) {
                    LOGGER.debug("Human can move their figures");
                    stateMashine.handleEvent(Event.ROLL_DICE_CORRECT);
                    displayResult(backend.randomNumber);
                } else {
                    LOGGER.debug("Human can't move their figures");
                    stateMashine.handleEvent(Event.ROLL_DICE_INCORRECT);
                }
            }
            case NO_MOVES_AVAILABLE -> backend.setNewCurrentPlayerIfNecessary();
            case BOTS_TURN ->  {
                backend.botMove();
                replaceFigures();
                if (backend.getNameOfWinner().isPresent()) {
                    displayWinWindow();
                    return;
                }
                backend.setNewCurrentPlayerIfNecessary();
            }
        }
        changeUserAdvice();
    }

    private class MyMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (stateMashine.getCurrentState() != State.MOVING_PIECE
                    && stateMashine.getCurrentState() != State.MOVED_WRONG_PIECE) {
                LOGGER.info("Figure movement aborted - not in correct state");
                return;
            }
            int clickedFigureIndex = -1;
            for (int i = 0; i < figures.length && clickedFigureIndex == -1; i++) {
                if (e.getSource() == figures[i]) {
                    clickedFigureIndex = i;
                }
            }
            LOGGER.info("Clicked Figure " + clickedFigureIndex);
            if (clickedFigureIndex == -1) {
                LOGGER.info("Figure movement aborted - no figure clicked");
                return;
            }
            Figure clickedFigure = backend.figures[clickedFigureIndex];
            if (!clickedFigure.getOwner().equals(backend.getNameOfCurrentPlayer())) {
                LOGGER.info("Figure movement aborted - false color selected");
                return;
            }
            if (!clickedFigure.isPlaceable()) {
                backend.moveToBase(clickedFigureIndex);
                LOGGER.info(
                        "Figure movement aborted - Wrong figure moved (Moving figure to base...)");
                stateMashine.handleEvent(Event.MOVED_WRONG_PIECE);
                afterHumanmoveLivecycle();
                return;
            }
            if (clickedFigure.isInBase()) {
                backend.moveOutOfBase(clickedFigureIndex);
            } else {
                backend.moveFigure(clickedFigureIndex);
            }
            afterHumanmoveLivecycle();
        }
    }
}
