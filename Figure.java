public class Figure {
    enum State {
	IN_BASE,
	ON_FIELD,
	IN_HOUSE,
	FINISHED,
    }
    private State state;

    public int field;
    public int color;
    public boolean placable;

    Figure(int fieldNew, int colorNew){
	state = State.IN_BASE;
        field = fieldNew;
        color = colorNew;
        placable = false;
    }

    public void enablePlacement() {
	placable = true;
    }

    public void disablePlacement() {
	placable = false;
    }

    public void isPlacable() {
	return placable;
    }

    public void setInBase() {
	state = State.IN_BASE;
    }

    public void setOnField() {
	state = State.ON_FIELD;
    }

    public void setInHouse() {
	state = State.IN_HOUSE;
    }

    public void setFinished() {
	state = State.FINISHED;
    }

    public boolean isInBase() {
	return State.IN_BASE == state;
    }

    public boolean isOnField() {
	return State.ON_FIELD == state;
    }

    public boolean isInHouse() {
	return State.IN_HOUSE == state;
    }

    public boolean isFinished() {
	return State.FINISHED == state;
    }

    public boolean isMovable() {
	return State.ON_FIELD == state || State.IN_HOUSE == state;
    }
}
