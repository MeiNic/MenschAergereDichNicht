import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Landingpage extends JFrame implements ActionListener {
    private final String[] colors = {"#ffc957", "#2a914e", "#1e32ff", "#cc0000", "#cccccc"};

    private JScrollPane scrollPane;
    private JLabel head;
    private JLabel labelPlayerNumber;
    private JSpinner playerNumber;
    private JCheckBox bots;
    private Circle[] colorMarker;
    private JTextField[] userNames;

    Landingpage(){
        //Declaration of all the J-components
        scrollPane = new JScrollPane();
        head = new JLabel("Mensch Ärgere Dich Nicht");
        labelPlayerNumber = new JLabel();
        playerNumber = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
        bots = new JCheckBox();
        userNames = new JTextField[4];

        //Declaration of the colored circles
        colorMarker = new Circle[4];
        colorMarker[0] = new Circle(90, 183, 43, "#ffc957");
        colorMarker[1] = new Circle(90, 246, 43, "#2a914e");
        colorMarker[2] = new Circle(90, 309, 43, "#1e32ff");
        colorMarker[3] = new Circle(90, 372, 43, "#cc0000");

        //Apply all setting for the head
        Font fontHeading = new Font(head.getFont().getName(), Font.PLAIN, 40);
        head.setFont(fontHeading);
        head.setBounds(13, 5, 480, 70);
        scrollPane.add(head);

        //Apply all setting for the
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

    }
    private void adjustJFrameSetting() {
        setTitle("landingpage");
        setSize(1400, 940);
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