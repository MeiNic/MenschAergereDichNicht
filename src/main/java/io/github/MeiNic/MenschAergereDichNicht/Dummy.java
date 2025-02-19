package io.github.MeiNic.MenschAergereDichNicht;

class Dummy implements Player {
    private final String name;
    private final int playerIndex;

    public Dummy(String name, int playerIndex) {
	this.name = name;
	this.playerIndex = playerIndex;
    }

    public String getName() {
        return name;
    }

    public int getPlayerState() {
        return -1;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
