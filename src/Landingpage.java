package src;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class Landingpage extends JFrame implements ActionListener, ChangeListener {
    private final int[] yCoordinatesCircles = {211, 273, 335, 397};
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private Circle[] colorMarker;
    private JLabel userNameAdvice;
    private JTextField[] userNames;
    private JCheckBox understood;
    private JLabel notChecked;
    private JButton rulesButton;
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
	
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        bots = new JCheckBox("Fill the game with bots", false);

	    userNames = new JTextField[4];
        userNames[0] = new JTextField("yellow");
        userNames[1] = new JTextField("green");
        userNames[2] = new JTextField("blue");
        userNames[3] = new JTextField("red");

        understood = new JCheckBox("I read and understood the rules of the game", false);
        notChecked = new JLabel("You have to read the rules and accept them first");
        rulesButton = new JButton("rules");
        startGame = new JButton("start game");

        // Font settings
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);
        Font fontNotCheckid = new Font(notChecked.getFont().getName(), Font.PLAIN, 15);
        notChecked.setFont(fontNotCheckid);
        notChecked.setForeground(Color.RED);

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

        understood.setBounds(20, 425, 300, 32);
        notChecked.setBounds(20, 450, 300, 32);
        rulesButton.setBounds(340, 425, 120, 32);

        startGame.setBounds(340, 475, 120, 32);
        startGame.setBackground(Color.red);

	    // Add listeners
        startGame.addActionListener(this);
        playerNumber.addChangeListener(this);
        understood.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED){
                startGame.setBackground(Color.GREEN);
                repaint();
            }else {
                startGame.setBackground(Color.RED);
                repaint();
            }
        });
        rulesButton.addActionListener(this);

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
        add(understood);
        add(rulesButton);
        add(startGame);

        // Display UI
        setTitle("landingpage");
        setSize(500, 550);
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
        if (getNumberOfHumanPlayers() == 0){
            bots.setSelected(true);
            userNameAdvice.setText("Enter your name:");
        } else {
            if (getNumberOfHumanPlayers() == 3){
                bots.setSelected(false);
            }
            userNameAdvice.setText("Enter names for all the players:");
        }
        repaint();
    }

    private boolean getUnderstoodStatus(){
        return understood.isSelected();
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGame){
            if (getUnderstoodStatus()){
                setVisible(false);
                String[] names = getNames();
                // TODO: Remove this `-1` by passing the "real" number of
                // human players to `BackEnd` and switching a `<=` to a `<` in
                // a loop of its constructor. This has to be done right after
                // the pull request for refactoring this file has been fully
                // merged with master. @guemax on 2023/08/16.
                int numberOfPlayers = getNumberOfHumanPlayers() - 1;
                boolean fillWithBots = getBotsSelection();

                new GameBoardGui(names, numberOfPlayers, fillWithBots);
            }else {
                add(notChecked);
                repaint();
            }

        }else if (e.getSource() == rulesButton){
            setVisible(false);
            new Rules(this);
        }
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
