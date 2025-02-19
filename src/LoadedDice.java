package src;

import java.util.Random;

class LoadedDice implements Dice {
    private static final Random rand = new Random();

    public int roll() {
	int[] sampleSpace = {1, 1, 2, 2, 3, 3, 4, 4, 5, 6};
	return sampleSpace[rand.nextInt(sampleSpace.length)];
    }
}
