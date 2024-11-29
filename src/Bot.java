package src;

class Bot implements Player {
    private final String name;
    private final int playerIndex;

    public Bot(String name, int playerIndex) {
        this.name = name;
        this.playerIndex = playerIndex;
    }

    public String getName() {
        return name;
    }

    public int getPlayerState() {
        return 1;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
