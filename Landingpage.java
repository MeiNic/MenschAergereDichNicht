import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Landingpage extends JFrame implements ActionListener {
    private final String[] colors = {"#ffc957", "#2a914e", "#1e32ff", "#cc0000", "#cccccc"};
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private Circle[] colorMarker;
    private JLabel userNameAdvice;
    private JTextField[] userNames;
    private JButton startGame;



    Landingpage(){
        //Declaration of all the J-components
        head = new JLabel("Mensch Ärgere Dich Nicht");
        labelPlayerNumber = new JLabel("Please enter the number of players:");
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        bots = new JCheckBox("Fill the round with bots" ,false);
        userNameAdvice = new JLabel("Enter how each player wants to be called during the game:");
        userNames = new JTextField[4];
        userNames[0] = new JTextField("yellow");
        userNames[1] = new JTextField("green");
        userNames[2] = new JTextField("blue");
        userNames[3] = new JTextField("red");
        startGame = new JButton("start game");

        //Declaration of the colored circles
        colorMarker = new Circle[4];
        colorMarker[0] = new Circle(40, 211, 43, "#ffc957");
        colorMarker[1] = new Circle(40, 273, 43, "#2a914e");
        colorMarker[2] = new Circle(40, 335, 43, "#1e32ff");
        colorMarker[3] = new Circle(40, 397, 43, "#cc0000");

        //Apply all setting for the head
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);
        head.setBounds(13, 5, 480, 70);
        add(head);

        //Position the other j-components
        labelPlayerNumber.setBounds(40, 80, 250, 32);
        playerNumber.setBounds(250, 80, 90, 32);
        bots.setBounds(35, 110, 180, 20);
        userNameAdvice.setBounds(40, 140, 350, 32);

        userNames[0].setBounds(100, 185, 180, 32);
        userNames[1].setBounds(100, 247, 180, 32);
        userNames[2].setBounds(100, 309, 180, 32);
        userNames[3].setBounds(100, 371, 180, 32);

        startGame.setBounds(345, 425, 120, 32);
        startGame.addActionListener(this);
        startGame.setBackground(Color.green);

        //Add all the j-components to the contentPanel
        add(labelPlayerNumber);
        add(playerNumber);
        add(bots);
        add(userNameAdvice);
        add(userNames[0]);
        add(userNames[1]);
        add(userNames[2]);
        add(userNames[3]);
        add(startGame);

        //Resets the values of all input-options for the user
        resetUseroptions();
        //Apply all needed values for the JFrame
        adjustJFrameSetting();
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        BackEnd game = new BackEnd(this);

    }

    public String[] getNames(){
        String[] name = new String[4];
        for(int i = 0; i <=3; i++){
            if (userNames[i].getText() != ""){
                name[i] = userNames[i].getText();
            }else {
                if (i == 0){
                    name[i] = "yellow";
                } else if (i == 1) {
                    name[i] = "green";
                } else if (i == 2) {
                    name[i] = "blue";
                }else {
                    name[i] = "red";
                }
            }
            
        }
        return name;
    }

    private void resetUseroptions(){
        for (JTextField jTextField : userNames){
            jTextField.setText("");
        }
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        bots = new JCheckBox("Fill the round with bots" ,false);
    }
    private void adjustJFrameSetting() {
        setTitle("landingpage");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(Color.BLACK);
        setResizable(true);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        forEachloopPaintFields(g, colorMarker);
    }

    private void forEachloopPaintFields(Graphics g, Circle[] array) {
        for (Circle circle : array) {
            int x = circle.getX();
            int y = circle.getY();
            int radius = circle.getRadius();
            Color color = circle.getColor();

            g.setColor(color);
            g.fillOval(x, y, radius, radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - 1, y - 1, radius + 1, radius + 1);
        }
    }
}
