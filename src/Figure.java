package src;

public class Figure {
    enum State {
        IN_BASE,
        ON_FIELD,
        IN_HOUSE,
        FINISHED,
    }
    private State state;
    private boolean placeable;

    public int field;
    public int color;
    public String owner;

    Figure(int fieldNew, int colorNew, String owner){
        state = State.IN_BASE;
        placeable = false;
        field = fieldNew;
        color = colorNew;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void enablePlacement() {
        placeable = true;
    }

    public void disablePlacement() {
        placeable = false;
    }

    public boolean isPlaceable() {
        return placeable;
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