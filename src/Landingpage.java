package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Landingpage extends JFrame {
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private JLabel[] colorMarker;
    private JLabel userNameAdvice;
    private JTextField[] userNames;
    private JCheckBox understood;
    private JLabel notChecked;
    private ImageTextPanel rulesButton;
    private ImageTextPanel startGame;

    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public Landingpage() {
        colorMarker = new JLabel[4];
        //Insert image-files to the color markers
        colorMarker[0] = new JLabel(readImg("figure-orange"));
        colorMarker[1] = new JLabel(readImg("figure-green"));
        colorMarker[2] = new JLabel(readImg("figure-blue"));
        colorMarker[3] = new JLabel(readImg("figure-red"));

        //Set positions of the color markers
        colorMarker[0].setBounds(40, 281, 39, 56);
        colorMarker[1].setBounds(40, 343, 39, 56);
        colorMarker[2].setBounds(40, 405, 39, 56);
        colorMarker[3].setBounds(40, 467, 39, 56);

        //add circles to jframe
        add(colorMarker[0]);
        add(colorMarker[1]);
        add(colorMarker[2]);
        add(colorMarker[3]);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Theme.SEMI_BOLD);

        // Initialize UI Elements
        head = new JLabel(readScaledImg("title", 250, 179));
        labelPlayerNumber = new JLabel("Please enter the number of players:");
        userNameAdvice = new JLabel("Enter names for all the players:");

        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        playerNumber = createCustomizedSpinner(playerNumber);
        bots = new JCheckBox("Fill the game with bots", false);

        userNames = new JTextField[4];
        userNames[0] = new JTextField("yellow");
        userNames[1] = new JTextField("green");
        userNames[2] = new JTextField("blue");
        userNames[3] = new JTextField("red");

        understood = new JCheckBox("I read and understood the rules of the game", false);
        notChecked = new JLabel("<html> <body> You have to read the rules and accept <br> them first! </body> </html>");
        rulesButton = new ImageTextPanel("button-idle", "rules");
        startGame = new ImageTextPanel("button-idle", "start game");

        // Small adjustments for notChecked	
        notChecked.setFont(Theme.SEMI_BOLD.deriveFont(15f));
        notChecked.setForeground(Color.RED);

        // Set bounds
        labelPlayerNumber.setBounds(40, 190, 300, 32);
        playerNumber.setBounds(325, 190, 90, 32);
        bots.setBounds(35, 220, 300, 20);
        userNameAdvice.setBounds(40, 250, 400, 32);
        head.setBounds(10, 5, 360, 179);

        userNames[0].setBounds(100, 295, 230, 32);
        userNames[1].setBounds(100, 357, 230, 32);
        userNames[2].setBounds(100, 419, 230, 32);
        userNames[3].setBounds(100, 481, 230, 32);

        understood.setBounds(20, 535, 370, 32);
        notChecked.setBounds(20, 570, 350, 50);
        rulesButton.setBounds(390, 535, 100, 32);
        startGame.setBounds(390, 585, 100, 32);

        // Add listeners
        playerNumber.addChangeListener(new MyChangeListener());
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

        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getUnderstoodStatus()) {
                    setVisible(false);

                    String[] names = getNames();
                    int numberOfHumanPlayers = getNumberOfHumanPlayers();
                    boolean fillWithBots = getBotsSelection();

                    LOGGER.info("Displaying GameBoardGui.");
                    new GameBoardGui(names, numberOfHumanPlayers, fillWithBots);
                } else {
                    add(notChecked);
                    repaint();
                    LOGGER.warn("User tried to start the game without accepting the rules.");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (getUnderstoodStatus()){
                    startGame.setImage("button-hovered-green");
                }else {
                    startGame.setImage("button-hovered-red");
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startGame.setImage("button-idle");
                repaint();
            }
        });

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
        setSize(520, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);
        setResizable(true);
        setVisible(true);
        LOGGER.info("Displaying Landingpage.");
    }

    private boolean getUnderstoodStatus(){
        return understood.isSelected();
    }

    private void openRules(){
        setVisible(false);
        LOGGER.info("Displaying rules.");
        new Rules(this);
    }

    private String[] getNames() {
        String[] defaultNames = {"yellow", "green", "blue", "red"};
        String[] playerNames = new String[4];

        for (int i = 0; i < 4; i++) {
            String currentPlayerName = userNames[i].getText();

            playerNames[i] = "" == currentPlayerName ? defaultNames[i] : currentPlayerName;
        }

        return playerNames;
    }

    private int getNumberOfHumanPlayers() {
        return (int)playerNumber.getValue();
    }

    private boolean getBotsSelection() {
        return bots.isSelected();
    }

    //Two methods used to change the stile of the JSpinner
    private static JSpinner createCustomizedSpinner(JSpinner spinnerInput){
        SpinnerModel spinnerModel = spinnerInput.getModel();

        JSpinner cache = new JSpinner(spinnerModel);

        JComponent spinnerEditor = cache.getEditor();
        if (spinnerEditor instanceof JSpinner.DefaultEditor){
            JFormattedTextField textField = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
            textField.setForeground(Theme.FOREGROUND_COLOR);
            textField.setBackground(Theme.BACKGROUND_COLOR);
        }

        JButton incrementButton = getSpinnerButton(cache, "Spinner.nextButton");
        JButton decrementButton = getSpinnerButton(cache, "Spinner.previousButton");

        if (incrementButton != null){
            incrementButton.setForeground(Theme.FOREGROUND_COLOR);
            incrementButton.setBackground(Theme.BACKGROUND_COLOR);
        }
        if (decrementButton != null){
            decrementButton.setForeground(Theme.FOREGROUND_COLOR);
            decrementButton.setBackground(Theme.BACKGROUND_COLOR);
        }

        return cache;
    }

    private static JButton getSpinnerButton(JSpinner spinner, String name){
        Component[] components = spinner.getComponents();
        for (Component component : components){
            if (component instanceof JButton && name.equals(component.getName())){
                return (JButton) component;
            }
        }
        return null;
    }

    //methods to read images from the resources folder
    private ImageIcon readImg (String imageName){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("res/"+imageName+".png"));
        }catch (IOException e){
            LOGGER.error("Failed to load img " + imageName);
        }
        return new ImageIcon(Objects.requireNonNull(img));
    }

    private ImageIcon readScaledImg (String imageName, int width, int height){
        BufferedImage unscaledImg = null;
        try {
            unscaledImg = ImageIO.read(new File("res/"+imageName+".png"));
            if (unscaledImg != null){
                Image scaledImg = unscaledImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        }catch (IOException e){
            return null;
        }
        return null;
    }

    class MyChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            for (int i = 0; i < 4; i++) {
                if (i < getNumberOfHumanPlayers()) {
                    add(userNames[i]);
                    add(colorMarker[i]);
                } else {
                    remove(userNames[i]);
                    remove(colorMarker[i]);
                }
            }
            if (getNumberOfHumanPlayers() == 1){
                bots.setSelected(true);
                userNameAdvice.setText("Enter your name:");
            } else {
                if (getNumberOfHumanPlayers() == 4){
                    bots.setSelected(false);
                }
                userNameAdvice.setText("Enter names for all the players:");
            }
            repaint();
        }
    }
}