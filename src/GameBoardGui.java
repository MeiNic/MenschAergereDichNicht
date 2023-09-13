package src;

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

	JLabel userAdvice;
	JButton rollDice;
	JLabel result;
	JLabel figureChooserPrompt;
	JLabel rulesAdvice;
	JButton rulesButton;

	JLabel noSix;
	JButton nextPlayer;

	BackEnd backend;

	public GameBoardGui(String[] playerNames, int numberOfPlayers, boolean fillWithBots) {
		this.backend = new BackEnd(playerNames, numberOfPlayers, fillWithBots);

		figures = new Circle[16];
		houses = new Circle[16];
		bases = new Circle[16];
		fields = new Circle[40];

		replaceFigures();
		int diameter = 50;

		for (int i = 0; i < houses.length; i++) {
			int x = housePositionsX[i];
			int y = housePositionsY[i];
			houses[i] = new Circle(x, y, diameter, FieldColor.YELLOW.getHexCode());
		}
		for (int i = 0; i < bases.length; i++) {
			int x = basePositionsX[i];
			int y = basePositionsY[i];
			bases[i] = new Circle(x, y, diameter, FieldColor.YELLOW.getHexCode());
		}
		for (int i = 0; i < fields.length; i++) {
			int x = fieldPositionsX[i];
			int y = fieldPositionsY[i];
			fields[i] = new Circle(x, y, diameter, FieldColor.GRAY.getHexCode());
		}

		for (int i = 0; i < 4; i++) {
			houses[i + 4].setColor(FieldColor.GREEN.getHexCode());
			houses[i + 8].setColor(FieldColor.BLUE.getHexCode());
			houses[i + 12].setColor(FieldColor.RED.getHexCode());

			bases[i + 4].setColor(FieldColor.GREEN.getHexCode());
			bases[i + 8].setColor(FieldColor.BLUE.getHexCode());
			bases[i + 12].setColor(FieldColor.RED.getHexCode());
		}

		// Set color for start fields
		fields[0].setColor(FieldColor.YELLOW.getHexCode());
		fields[10].setColor(FieldColor.GREEN.getHexCode());
		fields[20].setColor(FieldColor.BLUE.getHexCode());
		fields[30].setColor(FieldColor.RED.getHexCode());

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
		noSix.setText("You didn't got a six. Press this button to move on to the next player");
		nextPlayer.setText("next player");

		// Set bounds
		userAdvice.setBounds(970, 22, 450, 64);
		rollDice.setBounds(970, 80, 120, 32);
		result.setBounds(970, 80, 100, 32);
		rulesAdvice.setBounds(980, 460, 200, 32);
		rulesButton.setBounds(980, 500, 100, 32);
		noSix.setBounds(970, 22, 450, 32);
		nextPlayer.setBounds(970, 80, 120, 32);

		// Add listeners
		rollDice.addActionListener(this);
		rulesButton.addActionListener(this);
		addMouseListener(this);

		// Add UI Elements
		add(rollDice);
		add(userAdvice);
		add(result);
		add(rulesAdvice);
		add(rulesButton);
	
		// Display UI
		setTitle("game field");
		setSize(1400, 940);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBackground(Color.BLACK);
		setResizable(true);
		setVisible(true);
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
			if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
				continue;
			}

			if (clickedFigure.isPlaceable()) {
				backend.moveFigure(clickedFigureIndex);
			} else {
				backend.moveToBase(clickedFigureIndex);
			}
			prepareNextMove();
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
			if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
				continue;
			}
			if (clickedFigure.isPlaceable()) {
				backend.moveFigure(clickedFigureIndex);
			} else {
				backend.moveToBase(clickedFigureIndex);
			}
			prepareNextMove();
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
			if (clickedFigure.getOwner() != backend.getNameOfCurrentPlayer()) {
				continue;
			}

			if (clickedFigure.isPlaceable()) {
				backend.moveOutOfBase(clickedFigureIndex);
			}
			prepareNextMove();
			return;
		}
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

		for (int i = 0; i < input.length && i < figures.length; i++){
			String color = FigureColor.YELLOW.getHexCode();

			switch (input[i].color) {
				case 0: color = FigureColor.YELLOW.getHexCode();
				break;
				case 1: color = FigureColor.GREEN.getHexCode();
				break;
				case 2: color = FigureColor.BLUE.getHexCode();
				break;
				case 3: color = FigureColor.RED.getHexCode();
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

			figures[i] = new Circle(x, y, diameter, color);
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

		paintFields(g, houses);
		paintFields(g, bases);
		paintFields(g, fields);
		paintFigures(g, figures);
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
}