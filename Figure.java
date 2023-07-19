public class Figure {
    public int field;
    public boolean inBase;
    public boolean inHouse;
    public int color;
    public boolean finished;
    public boolean placeOption;

    Figure(int fieldNew, int colorNew){
        inBase = true;
        inHouse = false;
        field = fieldNew;
        color = colorNew;
        finished = false;
        placeOption = false;
    }
}
