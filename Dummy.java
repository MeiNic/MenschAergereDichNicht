class Dummy implements Player {
    private String name;

    public Dummy(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public int getPlayerState() {
	return -1;
    }
}
