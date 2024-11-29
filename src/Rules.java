package src;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Rules extends JFrame{
    private final JLabel header;
    private final JLabel goalHeader;
    private final JLabel goal1;
    private final JLabel goal2;
    private final JLabel ruleHeader;
    private final JLabel rule1;
    private final JLabel rule2;
    private final JLabel rule3;
    private final JLabel rule4;
    private final JLabel rule5;
    private final ImageTextPanel close;
    public final JFrame executingClass;

    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    Rules(JFrame JFrameN){
        executingClass = JFrameN;
	
        //declaration of the JLabels & the JCheckBox
        header = new JLabel("Info");
        goalHeader = new JLabel("Goal of the Game:");
        goal1 = new JLabel("The Goal is to get all of your 4 figures into your House.");
        goal2 = new JLabel("In order to get there you have to kick your opponents figures.");
        ruleHeader = new JLabel("Rules: ");
        rule1 = new JLabel("1. When a 6 is rolled, you have to move a figure out of base!");
        rule2 = new JLabel("2. you have to keep your start-field free, as long as there are figures in your Base");
        rule3 = new JLabel("<html><body> 3. If possible, you must kick another person! If you didn't your " +
                "figure, that was able to <br> kick someone, will get moved to your Base. </body></html>");
        rule4 = new JLabel("<html><body> 4. In order to get into your house you have to get the exact number, " +
                "you can't go in with <br> a number, that is to high. </body></html>");
        rule5 = new JLabel("5. You cannot jump over your figures in your House.");
        close = new ImageTextPanel("button-idle", "close");

        //position of all j-components
        header.setBounds(3, 0, 150, 35);
        goalHeader.setBounds(3, 40, 300, 35);
        ruleHeader.setBounds(3, 115, 300, 35);
        goal1.setBounds(6, 65, 700, 32);
        goal2.setBounds(6, 85, 700, 32);
        rule1.setBounds(6, 140, 700, 32);
        rule2.setBounds(6, 160, 700, 32);
        rule3.setBounds(6, 190, 900, 32);
        rule4.setBounds(6, 230, 900, 32);
        rule5.setBounds(6, 260, 700, 32);
        close.setBounds(6, 300, 100, 32);

        //Set Font
        header.setFont(Theme.EXTRA_BOLD);
        goalHeader.setFont(Theme.BOLD);
        ruleHeader.setFont(Theme.BOLD);

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
        add(ruleHeader);
        add(rule1);
        add(rule2);
        add(rule3);
        add(rule4);
        add(rule5);
        add(close);

        //display UI
        setTitle("Overview ruleHeader");
        setSize(750, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);
        setResizable(true);
        setVisible(true);
        LOGGER.info("Displaying Landingpage.");
    }

    private void closeWindow(){
        LOGGER.info("Closing ruleHeader and setting previous window to visible again.");
        setVisible(false);
        executingClass.setVisible(true);
    }
}
