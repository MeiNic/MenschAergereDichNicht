package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Landingpage extends JFrame implements ActionListener, ChangeListener {
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private JLabel[] colorMarker;
    private JLabel userNameAdvice;
    private JTextField[] userNames;
    private JCheckBox understood;
    private JLabel notChecked;
    private JButton rulesButton;
    private JButton startGame;
    private Font customFont;
    Logger logger = LoggerFactory.getLoggerInstance();

    public Landingpage() {
        colorMarker = new JLabel[4];
        //Insert image-files to the color markers
        colorMarker[0] = new JLabel(readImg("figure-orange"));
        colorMarker[1] = new JLabel(readImg("figure-green"));
        colorMarker[2] = new JLabel(readImg("figure-blue"));
        colorMarker[3] = new JLabel(readImg("figure-red"));

        //Set positions of the color markers
        colorMarker[0].setBounds(40, 171, 39, 56);
        colorMarker[1].setBounds(40, 233, 39, 56);
        colorMarker[2].setBounds(40, 295, 39, 56);
        colorMarker[3].setBounds(40, 357, 39, 56);

        //add circles to jframe
        add(colorMarker[0]);
        add(colorMarker[1]);
        add(colorMarker[2]);
        add(colorMarker[3]);

        //Configure Font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-SemiBold.ttf")).deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
        notChecked = new JLabel("<html> <body> You have to read the rules and accept <br> them first! </body> </html>");
        rulesButton = new JButton("rules");
        startGame = new JButton("start game");

        // Font settings
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);
        Font fontNotChecked = new Font(notChecked.getFont().getName(), Font.PLAIN, 15);
        notChecked.setFont(fontNotChecked);
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
        notChecked.setBounds(20, 450, 300, 50);
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
        setSize(500, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#6c6f85"));
        setResizable(true);
        setVisible(true);
        logger.info("Displaying Landingpage.");
    }
    
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

    private boolean getUnderstoodStatus(){
        return understood.isSelected();
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGame) {
            if (getUnderstoodStatus()) {
                setVisible(false);

                String[] names = getNames();
                int numberOfHumanPlayers = getNumberOfHumanPlayers();
                boolean fillWithBots = getBotsSelection();

                logger.info("Displaying GameBoardGui.");
                new GameBoardGui(names, numberOfHumanPlayers, fillWithBots);
            } else {
                add(notChecked);
                repaint();
                logger.warn("User tried to start the game without accepting the rules.");
            }

        } else if (e.getSource() == rulesButton) {
            setVisible(false);
            logger.info("Displaying rules.");
            new Rules(this);
        }
    }

    public String[] getNames() {
        String[] defaultNames = {"yellow", "green", "blue", "red"};
        String[] playerNames = new String[4];

        for (int i = 0; i < 4; i++) {
            String currentPlayerName = userNames[i].getText();

            playerNames[i] = "" == currentPlayerName ? defaultNames[i] : currentPlayerName;
        }

        return playerNames;
    }

    public int getNumberOfHumanPlayers() {
        return (int)playerNumber.getValue();
    }

    public boolean getBotsSelection() {
        return bots.isSelected();
    }

    private ImageIcon readImg (String imageName){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("res/"+imageName+".png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ImageIcon(img);
    }
}
