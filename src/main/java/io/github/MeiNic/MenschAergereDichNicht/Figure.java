package io.github.MeiNic.MenschAergereDichNicht;

import static io.github.MeiNic.MenschAergereDichNicht.FigureState.IN_BASE;
import static io.github.MeiNic.MenschAergereDichNicht.FigureState.ON_FIELD;
import static io.github.MeiNic.MenschAergereDichNicht.FigureState.IN_HOUSE;
import static io.github.MeiNic.MenschAergereDichNicht.FigureState.FINISHED;

public class Figure {
    private boolean placeable;
    private FigureState state;
    private int field;
    public final int color;
    public final String owner;
    private int progress;
    final Logger LOGGER = LoggerFactory.getLoggerInstance();

    Figure(int fieldNew, int colorNew, String owner){
        state = IN_BASE;
        placeable = false;
        field = fieldNew;
        color = colorNew;
        this.owner = owner;
        progress = 0;
    }

    public int getField() {
        return this.field;
    }

    public void setField(int newField, int randomNumber) {
        switch (state){
            case IN_BASE, IN_HOUSE -> {
                if (newField >= color * 4 && newField <= color * 4 + 4) this.field = newField;
                else LOGGER.error("Tried to set figure on invalid field. \n Current Field: " + this.field + " New Field: " + newField + " Color " + this.color + " State: " + this.state);
            }
            case ON_FIELD -> {
                if (newField < 40) {
                    progress += randomNumber;
                    this.field = newField;
                }
                else LOGGER.error("Tried to set figure on invalid field. \n Current Field: " + this.field + " New Field: " + newField + " Color " + this.color + " State: " + this.state);
            }
            case FINISHED -> LOGGER.error("Tried to replace finished Figure");
        }
    }

    public int getProgress() {
        return progress;
    }

    public void moveByValue(int value) {
        field += value;
        if (field > 39) field -= 40;
        progress += value;
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
        progress = 0;
        state = IN_BASE;
    }

    public void setOnField() {
        progress = 0;
        state = ON_FIELD;
    }

    public void setInHouse() {
        progress = 0;
        state = IN_HOUSE;
    }

    public void setFinished() {
        progress = 0;
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
