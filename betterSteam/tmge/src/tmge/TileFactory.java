package tmge;

import java.util.Random;

public class TileFactory {
    Random rand;

    public TileFactory () {
        this.rand = new Random();
    }

    public int getNewTile(int low, int high) {
        return rand.nextInt(high-low) + low;
    }
}
