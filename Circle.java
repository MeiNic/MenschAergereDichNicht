import java.awt.*;

public class Circle {
    private int x;
    private int y;
    private int radius;
    private Color color;
    private boolean button;

    public Circle(int xNew, int yNew, int radiusNew, String hexCode){
        this.x = xNew;
        this.y = yNew;
        this.radius = radiusNew;
        this.color = Color.decode(hexCode);
        this.button = false;
    }

    public Circle(int xNew, int yNew, int radiusNew){
        this.x = xNew;
        this.y = yNew;
        this.radius = radiusNew;
        this.color = Color.BLACK;
        this.button = false;
    }

    //Getter and Setter for every value
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
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

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(String hexCode){
        this.color = Color.decode(hexCode);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }
}