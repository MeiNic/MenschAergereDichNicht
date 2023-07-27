import java.awt.*;

public class Circle {
    private int x;
    private int y;
    private int radius;
    private Color color;

    public Circle(int xNew, int yNew, int radiusNew, String hexCode){
        this.x = xNew;
        this.y = yNew;
        this.radius = radiusNew;
        this.color = Color.decode(hexCode);
    }

    //Getter and Setter for every value
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(String hexCode){
        this.color = Color.decode(hexCode);
    }
}