import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Landingpage extends JFrame implements ActionListener {
    private final String[] colors = {"#ffc957", "#2a914e", "#1e32ff", "#cc0000", "#cccccc"};
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private Circle[] colorMarker;
    private JTextField[] userNames;

    Landingpage(){
        //Declaration of all the J-components
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        head = new JLabel("Mensch Ärgere Dich Nicht");
        labelPlayerNumber = new JLabel("Please enter the number of players:");
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        bots = new JCheckBox("Fill the round with bots" ,false);
        userNames = new JTextField[4];

        //Declaration of the colored circles
        colorMarker = new Circle[4];
        colorMarker[0] = new Circle(40, 179, 43, "#ffc957");
        colorMarker[1] = new Circle(40, 242, 43, "#2a914e");
        colorMarker[2] = new Circle(40, 305, 43, "#1e32ff");
        colorMarker[3] = new Circle(40, 368, 43, "#cc0000");

        //Apply all setting for the head
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);
        head.setBounds(13, 5, 480, 70);
        contentPanel.add(head);

        //Position the other j-components
        labelPlayerNumber.setBounds(40, 80, 250, 32);
        contentPanel.add(labelPlayerNumber);

        playerNumber.setBounds(300, 80, 90, 32);
        contentPanel.add(playerNumber);

        bots.setBounds(40, 130, 180, 20);
        contentPanel.add(bots);

        userNames[0].setBounds(100, 185, 180, 32);
        userNames[1].setBounds(100, 247, 180, 32);
        userNames[2].setBounds(100, 309, 180, 32);
        userNames[3].setBounds(100, 371, 180, 32);

        //Add all the j-components to the contentPanel


        //Set the JScrollPane
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(300, 400));

        //Resets the values of all input-options for the user
        resetUseroptions();
        //Apply all needed values for the JFrame
        adjustJFrameSetting();
    }
    public void actionPerformed(ActionEvent e) {

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
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(Color.BLACK);
        setResizable(true);
        getContentPane().add(scrollPane);
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
