package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Rules extends JFrame implements ActionListener{
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
    private JButton done;
    private String sr1;
    private String sr2;
    private String sr3;
    private String sr4;
    private String sr5;
    public JFrame executingClass;
    private final Font jetBrainsMonoSemiBold;
    private final static Color defaultForegroundColor = Color.decode("#f3f5f9");
    private final static Color defaultBackgroundColor = Color.decode("#6c6f85");
    Logger logger = LoggerFactory.getLoggerInstance();

    Rules(JFrame JFrameN){
        executingClass = JFrameN;
        //Configure Font
        try {
            jetBrainsMonoSemiBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-SemiBold.ttf")).deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(jetBrainsMonoSemiBold);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //Declaration of Strings
        sr1 = "1. When a 6 is rolled, you have to move a figure out of base!";
        sr2 = "2. you have to keep your start-field free, as long as there are figures in your Base";
        sr3 = "3. If possible, you must kick another person! If you didn't your figure, that was able to kick someone, will get moved to your Base.";
        sr4 = "4. In order to get into your house you have to get the exact number, you can't go in with a number, that is to high.";
        sr5 = "5. You cannot jump over your figures in your House.";

        //declaration of the JLabels & the JCheckBox
        header = new JLabel("Info");
        goalHeader = new JLabel("Goal of the Game:");
        goal1 = new JLabel("The Goal is to get all of your 4 figures into your House.");
        goal2 = new JLabel("In order to get there you have to kick your opponents figures.");
        rules = new JLabel("Rules: ");
        r1 = new JLabel(sr1);
        r2 = new JLabel(sr2);
        r3 = new JLabel(sr3);
        r4 = new JLabel(sr4);
        r5 = new JLabel(sr5);
        done = new JButton("close");

        //settings of headers
        Font fontHeading = new Font(header.getFont().getName(), Font.PLAIN, 40);
        header.setFont(fontHeading);
        header.setBounds(13, 5, 150, 70);

        Font fontGoalheading = new Font(goalHeader.getFont().getName(), Font.PLAIN, 20);
        goalHeader.setFont(fontGoalheading);
        goalHeader.setBounds(40, 80, 300, 50);

        Font fontRules = new Font(rules.getFont().getName(), Font.PLAIN, 20);
        rules.setFont(fontRules);
        rules.setBounds(40, 200, 300, 50);

        //position of all j-components
        goal1.setBounds(40, 130, 700, 32);
        goal2.setBounds(40, 160, 700, 32);
        r1.setBounds(40, 250, 700, 32);
        r2.setBounds(40, 280, 700, 32);
        r3.setBounds(40, 310, 900, 32);
        r4.setBounds(40, 340, 900, 32);
        r5.setBounds(40, 370, 700, 32);

        done.setBounds(40, 420, 80, 32);
        done.addActionListener(this);
        done.setBackground(Color.green);

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
        add(done);

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

    public void actionPerformed(ActionEvent e){
        logger.info("Closing Rules and setting previous window to visible again.");
        setVisible(false);
        executingClass.setVisible(true);
    }
}
