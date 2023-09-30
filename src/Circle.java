package src;

import java.awt.*;

public class Circle {
    private int x;
    private int y;
    private int diameter;
    private Color color;
    boolean visible;

    public Circle(int x, int y, int diameter, String hexCode){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = Color.decode(hexCode);
        this.visible = true;
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

    public boolean isVisible(){
        return visible;
    }
    public void disableVisible(){
        this.visible = false;
    }

    public void enableVisible(){
        this.visible = true;
    }
}
