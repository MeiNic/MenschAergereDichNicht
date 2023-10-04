package src;

class Human implements Player {
    private String name;
    private int playerIndex;

    public Human(String name, int playerIndex) {
	this.name = name;
	this.playerIndex = playerIndex;
    }

    public String getName() {
	return name;
    }

    public int getPlayerState() {
	return 0;
    }

    public int getPlayerIndex() {
	return playerIndex;
    }

    public int getIndexOfFirstFigure() {
	return 4 * getPlayerIndex();
    }

    public int getIndexOfLastFigure() {
	return 4 + getIndexOfFirstFigure();
    }

    public int getIndexOfStartField() {
	return 10 * getPlayerIndex();
    }
}
