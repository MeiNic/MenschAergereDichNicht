class Human implements Player {
    private String name;

    public Human(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public int getPlayerState() {
	return 0;
    }
}
