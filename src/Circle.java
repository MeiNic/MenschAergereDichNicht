import java.awt.*;

public class Circle {
    private int x;
    private int y;
    private int diameter;
    private Color color;

    public Circle(int x, int y, int diameter, String hexCode){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = Color.decode(hexCode);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDiameter() {
        return diameter;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(String hexCode){
        this.color = Color.decode(hexCode);
    }
}
