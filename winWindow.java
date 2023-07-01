import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class winWindow implements ActionListener {
    private JFrame frame;
    private JLabel message;
    private JButton close;

    public winWindow(String player){
        frame = new JFrame();
        message = new JLabel();
        close = new JButton();

        setMessage(player);
        setClose();

        frame.add(message);
        frame.add(close);

        adjustJFrameSetting();
    }

    public void actionPerformed(ActionEvent e) {
        //frame.setVisible(false);
    }

    /*
    method set all the needed parameters for the JButton
     */
    private void setClose(){
        close.setText("close");
        close.setBounds(95, 50, 100, 32);
        close.addActionListener(this);
    }

    /*
    method set all the needed parameters to the JLabel
     */
    private void setMessage(String player){
        message.setText("Congratulations! Player " + player + "has won this round.");
        message.setBounds(10, 10, 295, 32);
    }

    /*
    method sets all the needed parameters to the JFrame
     */
    private void adjustJFrameSetting() {
        frame.setTitle("congratulations");
        frame.setSize(315, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}