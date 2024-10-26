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
}
