package src;

import java.util.Random;

class LaPlaceDice implements Dice {
    private static Random rand = new Random();

    public int roll() {
        return 1 + rand.nextInt(6);
    }
}
