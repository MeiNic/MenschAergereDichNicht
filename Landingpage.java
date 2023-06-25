import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Landingpage extends JFrame implements ActionListener {
    private final String[] colors = {"#ffc957", "#2a914e", "#1e32ff", "#cc0000", "#cccccc"};
    private Circle[] colorMarker;

    Landingpage(){
        colorMarker = new Circle[4];
        colorMarker[0] = new Circle(90, 183, 43, "#ffc957");
        colorMarker[1] = new Circle(90, 246, 43, "#2a914e");
        colorMarker[2] = new Circle(90, 309, 43, "#1e32ff");
        colorMarker[3] = new Circle(90, 372, 43, "#cc0000");
    }
    public void actionPerformed(ActionEvent e) {

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