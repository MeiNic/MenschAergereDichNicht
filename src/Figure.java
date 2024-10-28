package src;

import static src.FigureState.IN_BASE;
import static src.FigureState.ON_FIELD;
import static src.FigureState.IN_HOUSE;
import static src.FigureState.FINISHED;

public class Figure {
    private boolean placeable;
    private FigureState state;
    private int field;
    public int color;
    public String owner;
    Logger LOGGER = LoggerFactory.getLoggerInstance();

    Figure(int fieldNew, int colorNew, String owner){
        state = IN_BASE;
        placeable = false;
        field = fieldNew;
        color = colorNew;
        this.owner = owner;
    }

    public int getField() {
        return this.field;
    }

    public void setField(int newField) {
        switch (state){
            case IN_BASE, IN_HOUSE -> {
                if (newField >= color * 4 && newField <= color * 4 + 4) this.field = newField;
                else LOGGER.error("Tried to set figure on invalid field. \n Current Field: " + this.field + " New Field: " + newField + " Color " + this.color + " State: " + this.state);
            }
            case ON_FIELD -> {
                if (newField < 40) this.field = newField;
                else LOGGER.error("Tried to set figure on invalid field. \n Current Field: " + this.field + " New Field: " + newField + " Color " + this.color + " State: " + this.state);
            }
            case FINISHED ->
                LOGGER.error("Tried to replace finished Figure");
        }
    }

    public void movebyValue(int value) {
        setField(this.field + value);
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
        state = IN_BASE;
    }

    public void setOnField() {
        state = ON_FIELD;
    }

    public void setInHouse() {
        state = IN_HOUSE;
    }

    public void setFinished() {
        state = FINISHED;
    }

    public FigureState getState() {
        return state;
    }

    public boolean isInBase() {
        return IN_BASE == state;
    }

    public boolean isOnField() {
        return ON_FIELD == state;
    }

    public boolean isInHouse() {
        return IN_HOUSE == state;
    }

    public boolean isFinished() {
        return FINISHED == state;
    }

    public boolean isMovable() {
        return ON_FIELD == state || IN_HOUSE == state;
    }
}