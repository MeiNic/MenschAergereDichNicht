enum State {
    IN_BASE,
    ON_FIELD,
    IN_HOUSE,
    FINISHED,
}

public class Figure {
    public int field;
    public State state;
    public int color;
    public boolean placeOption;

    Figure(int fieldNew, int colorNew){
	state = State.IN_BASE;
        field = fieldNew;
        color = colorNew;
        placeOption = false;
    }
}
