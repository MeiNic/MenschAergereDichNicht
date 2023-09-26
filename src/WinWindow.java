package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinWindow implements ActionListener {
    private JFrame frame;
    private JLabel message;
    private JButton close;
    private Logger logger;

    public WinWindow(String player){
        frame = new JFrame();
        message = new JLabel();
        close = new JButton();
        logger = LoggerFactory.getLoggerInstance();

        //set component values
        message.setText("Congratulations! Player " + player + "has won this round.");
        message.setBounds(10, 10, 295, 32);
        close.setText("close");
        close.setBounds(95, 50, 100, 32);
        close.addActionListener(this);

        //add elements
        frame.add(message);
        frame.add(close);

        //set JFrame values
        frame.setTitle("congratulations");
        frame.setSize(315, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(true);
        frame.setVisible(true);
        logger.info("displaying winWindow.");
    }

    public void actionPerformed(ActionEvent e) {
        logger.info("closing game.");
        frame.setVisible(false);
    }
}