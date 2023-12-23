package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Rules extends JFrame{
    private JLabel header;
    private JLabel goalHeader;
    private JLabel goal1;
    private JLabel goal2;
    private JLabel rules;
    private JLabel r1;
    private JLabel r2;
    private JLabel r3;
    private JLabel r4;
    private JLabel r5;
    private ImageTextPanel close;
    public JFrame executingClass;
    private final Font jetBrainsMonoSemiBold;
    private final Font jetBrainsMonoBold;
    private final Font jetBrainsMonoExtraBold;
    private final static Color defaultForegroundColor = Color.decode("#f3f5f9");
    private final static Color defaultBackgroundColor = Color.decode("#6c6f85");
    Logger logger = LoggerFactory.getLoggerInstance();

    Rules(JFrame JFrameN){
        executingClass = JFrameN;
        //Configure Fonts
        try {
            jetBrainsMonoSemiBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-SemiBold.ttf")).deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(jetBrainsMonoSemiBold);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            jetBrainsMonoBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-Bold.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(jetBrainsMonoSemiBold);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            jetBrainsMonoExtraBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-ExtraBold.ttf")).deriveFont(17f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(jetBrainsMonoSemiBold);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //declaration of the JLabels & the JCheckBox
        header = new JLabel("Info");
        goalHeader = new JLabel("Goal of the Game:");
        goal1 = new JLabel("The Goal is to get all of your 4 figures into your House.");
        goal2 = new JLabel("In order to get there you have to kick your opponents figures.");
        rules = new JLabel("Rules: ");
        r1 = new JLabel("1. When a 6 is rolled, you have to move a figure out of base!");
        r2 = new JLabel("2. you have to keep your start-field free, as long as there are figures in your Base");
        r3 = new JLabel("3. If possible, you must kick another person! If you didn't your figure, that was " +
                "able to kick someone, will get moved to your Base.");
        r4 = new JLabel("4. In order to get into your house you have to get the exact number, you can't " +
                "go in with a number, that is to high.");
        r5 = new JLabel("5. You cannot jump over your figures in your House.");
        close = new ImageTextPanel("button-idle", "close");

        //position of all j-components
        header.setBounds(13, 5, 150, 70);
        goalHeader.setBounds(40, 80, 300, 50);
        rules.setBounds(40, 200, 300, 50);
        goal1.setBounds(40, 130, 700, 32);
        goal2.setBounds(40, 160, 700, 32);
        r1.setBounds(40, 250, 700, 32);
        r2.setBounds(40, 280, 700, 32);
        r3.setBounds(40, 310, 900, 32);
        r4.setBounds(40, 340, 900, 32);
        r5.setBounds(40, 370, 700, 32);
        close.setBounds(40, 420, 100, 32);

        //Set Font
        header.setFont(jetBrainsMonoExtraBold);
        goalHeader.setFont(jetBrainsMonoBold);
        rules.setFont(jetBrainsMonoBold);
        close.setFont(jetBrainsMonoSemiBold);

        //Set Background
        header.setBackground(defaultBackgroundColor);
        goalHeader.setBackground(defaultBackgroundColor);
        rules.setBackground(defaultBackgroundColor);
        close.setBackground(defaultBackgroundColor);

        //Set Foreground
        header.setForeground(defaultForegroundColor);
        goalHeader.setForeground(defaultForegroundColor);
        rules.setForeground(defaultForegroundColor);
        close.setForeground(defaultForegroundColor);

        //Button action for close
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setImage("button-hovered");
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setImage("button-idle");
                repaint();
            }
        });

        //Add UI elements
        add(header);
        add(goalHeader);
        add(goal1);
        add(goal2);
        add(rules);
        add(r1);
        add(r2);
        add(r3);
        add(r4);
        add(r5);
        add(close);

        //display UI
        setTitle("Overview Rules");
        setSize(785, 510);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(defaultBackgroundColor);
        setResizable(true);
        setVisible(true);
        logger.info("Displaying Landingpage.");
    }

    public void closeWindow(){
        logger.info("Closing Rules and setting previous window to visible again.");
        setVisible(false);
        executingClass.setVisible(true);
    }
}
