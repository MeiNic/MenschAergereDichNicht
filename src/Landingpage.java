package src;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Landingpage extends JFrame implements ActionListener, ChangeListener {
    private final int[] yCoordinatesCircles = {211, 273, 335, 397};
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private Circle[] colorMarker;
    private JLabel userNameAdvice;
    private JTextField[] userNames;
    private JButton startGame;

    public Landingpage() {
        colorMarker = new Circle[4];
        colorMarker[0] = new Circle(40, 211, 43, "#ffc957");
        colorMarker[1] = new Circle(40, 273, 43, "#2a914e");
        colorMarker[2] = new Circle(40, 335, 43, "#1e32ff");
        colorMarker[3] = new Circle(40, 397, 43, "#cc0000");
	
        // Initialize UI Elements
        head = new JLabel("Mensch Ärgere Dich Nicht");
        labelPlayerNumber = new JLabel("Please enter the number of players:");
        userNameAdvice = new JLabel("Enter names for all the players:");
	
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 2, 4, 1));
        bots = new JCheckBox("Fill the game with bots", false);

	    userNames = new JTextField[4];
        userNames[0] = new JTextField("yellow");
        userNames[1] = new JTextField("green");
        userNames[2] = new JTextField("blue");
        userNames[3] = new JTextField("red");
	
        startGame = new JButton("start game");

        // Font settings
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);

        // Set bounds
        labelPlayerNumber.setBounds(40, 80, 250, 32);
        playerNumber.setBounds(250, 80, 90, 32);
        bots.setBounds(35, 110, 180, 20);
        userNameAdvice.setBounds(40, 140, 350, 32);
        head.setBounds(13, 5, 480, 70);

        userNames[0].setBounds(100, 185, 180, 32);
        userNames[1].setBounds(100, 247, 180, 32);
        userNames[2].setBounds(100, 309, 180, 32);
        userNames[3].setBounds(100, 371, 180, 32);

        startGame.setBounds(345, 425, 120, 32);
        startGame.setBackground(Color.green);

	    // Add listeners
        startGame.addActionListener(this);
        playerNumber.addChangeListener(this);

        // Add UI Elements
        add(head);
        add(labelPlayerNumber);
        add(playerNumber);
        add(bots);
        add(userNameAdvice);
        add(userNames[0]);
        add(userNames[1]);
        add(userNames[2]);
        add(userNames[3]);
        add(startGame);

        // Display UI
        setTitle("landingpage");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(Color.BLACK);
        setResizable(true);
        setVisible(true);
    }
    
    public void stateChanged(ChangeEvent e) {
        for (int i = 0; i < 4; i++) {
            if (i < getNumberOfHumanPlayers()) {
                add(userNames[i]);
                colorMarker[i].setY(yCoordinatesCircles[i]);
            } else {
                remove(userNames[i]);
		// HACK: Someone using a monitor taller than one
		// million pixels would be able to see the hidden
		// color marker! Because no one will ever play our
		// game on such a gigantic monitor, I think we do not
		// need to rush for potentially fixing this "issue",
		// however, there might be a better way to do
		// this. @guemax on 2023/08/16.
                colorMarker[i].setY(100000);
            }
        }
        repaint();
    }
    
    public void actionPerformed(ActionEvent e) {
        setVisible(false);

	    String[] names = getNames();
	    // TODO: Remove this `-1` by passing the "real" number of
	    // human players to `BackEnd` and switching a `<=` to a `<` in
	    // a loop of its constructor. This has to be done right after
	    // the pull request for refactoring this file has been fully
	    // merged with master. @guemax on 2023/08/16.
	    int numberOfPlayers = getNumberOfHumanPlayers() - 1;
	    boolean fillWithBots = getBotsSelection();
	
	    GameBoardGui game = new GameBoardGui(names, numberOfPlayers, fillWithBots);

	    // Providing a help button during the game might be less
        // annoying playing, especially during development when you
        // often have to start the game again and again.

	    // Rules overview = new Rules(game);
	    // game.gui.setVisible(false);
    }

    public String[] getNames() {
	    String[] defaultNames = {"yellow", "green", "blue", "red"};
	    String[] playerNames = new String[4];

        for (int i = 0; i < 4; i++) {
	    String currentPlayerName = userNames[i].getText();

	    playerNames[i] = "" == currentPlayerName
		? defaultNames[i] : currentPlayerName;
        }
	
        return playerNames;
    }

    public int getNumberOfHumanPlayers() {
	    return (int)playerNumber.getValue();
    }

    public boolean getBotsSelection() {
        return bots.isSelected();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        paintColorMarkers(g, colorMarker);
    }

    private void paintColorMarkers(Graphics g, Circle[] colorMarkers) {
        for (Circle marker : colorMarkers) {
            int x = marker.getX();
            int y = marker.getY();
            int diameter = marker.getDiameter();
            Color color = marker.getColor();

            g.setColor(color);
            g.fillOval(x, y, diameter, diameter);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, diameter, diameter);
        }
    }
}
