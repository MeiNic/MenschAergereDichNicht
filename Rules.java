import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
public class Rules extends JFrame implements ActionListener{
    private JLabel header;
    private JLabel rules;
    private JLabel goal;
    private JCheckBox understood;
    private JButton done;
    private String r;
    public BackEnd game;

    Rules(BackEnd newBackEnd){
        game = newBackEnd;
        //declaration of the JLabels & the JCheckBox
        r = new String("1. When a 6 is rolled, you have to move a figure out of base! /n2. you have to keep your start-field free, as long as there are figures in your Base " +
                "/n3. If possible, you must kick another person! If you didn't your figure, that was able to kick someone, will get moved to your Base."
                 + "/n4. In order to get into your house you have to get the exact number, you can't go in with a number, that is to high."+"/n5. You cannot jump over your figures in your House.");
        header = new JLabel("Rules");
        goal = new JLabel("Goal of the Game: /nThe Goal is to get all of your 4 figures into your House. /nIn order to get there you have to kick your opponents figures.");
        rules = new JLabel(r);
        understood = new JCheckBox("I read and understood the rules of the game", false);
        done = new JButton("done");

        //settings of header
        Font fontHeading = new Font(header.getFont().getName(), Font.PLAIN, 40);
        header.setFont(fontHeading);
        header.setBounds(13, 5, 80, 70);

        //position of all j-components
        goal.setBounds(40, 80, 600, 32);
        rules.setBounds(40, 110, 600, 400);
        understood.setBounds(40, 600, 120, 32);

        done.setBounds(40, 635, 80, 32);
        done.addActionListener(this);
        done.setBackground(Color.green);

        //Add all the j-components to the content Panel
        add(header);
        add(goal);
        add(rules);
        add(understood);

        //apply all needed values for the JFrame
        adjustJFrameSetting();
    }

    public boolean getCheckboxSetting(){
        return understood.isSelected();
    }

    public void actionPerformed(ActionEvent e){
        setVisible(false);
        game.gui.setVisible(true);
    }


    private void adjustJFrameSetting(){
        setTitle("Overview Rules");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(Color.BLACK);
        setResizable(true);
        setVisible(true);
    }

}
