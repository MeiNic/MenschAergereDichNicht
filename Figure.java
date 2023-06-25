public class Figure {
    private int field;
    private boolean inBase;
    private boolean inHouse;
    private int color;
    private boolean finished;
    private boolean placeOption;

    Figure(int fieldNew, int colorNew){
        inBase = true;
        inHouse = false;
        field = fieldNew;
        color = colorNew;
        finished = false;
        placeOption = false;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public boolean isInBase() {
        return inBase;
    }

    public void setInBase(boolean inBase) {
        this.inBase = inBase;
    }

    public boolean isInHouse() {
        return inHouse;
    }

    public void setInHouse(boolean inHouse) {
        this.inHouse = inHouse;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isPlaceOption()   {
        return placeOption;
    }

    public void setPlaceOption(boolean placeOption) {
        this.placeOption = placeOption;
    }
}