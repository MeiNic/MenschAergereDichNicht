class Bot implements Player {
    private String name;

    public Bot(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public int getPlayerState() {
	return 1;
    }
}
