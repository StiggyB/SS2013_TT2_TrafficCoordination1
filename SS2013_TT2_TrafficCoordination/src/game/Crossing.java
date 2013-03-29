package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * One Crossing
 * 
 * Contains 5x5 Tiles ..|.. ..|.. __#__ ..|.. ..|..
 * 
 */
public class Crossing {
    /* Position on field in tiles */
    int x;
    int y;

    static int TILEWIDTH = 5;
    private int CENTER = TILEWIDTH / 2;
    private int ROXELCNT = 2 * TILEWIDTH - 1;

    /* Contained Tiles */
    String[] tileIds;
    Vector<Integer> roxelCoords;
    Roxel[][] roxels;

    public Crossing(int x, int y) {
        this.x = x * TILEWIDTH;
        this.y = y * TILEWIDTH;
        this.tileIds = new String[TILEWIDTH];
        roxels = new Roxel[TILEWIDTH][TILEWIDTH];

        for (int i = 0; i < TILEWIDTH; i++) {
            tileIds[i] = new String("");
            for (int j = 0; j < TILEWIDTH; j++) {
                if ((i < CENTER && j < CENTER) || (i > CENTER && j > CENTER)
                        || (i < CENTER && j > CENTER)
                        || (i > CENTER && j < CENTER)) {
                    tileIds[i] += "."; // no graphics
                } else if (i == CENTER && j == CENTER) {
                    tileIds[i] += "#"; // crossing
                    roxels[j][i] = new Roxel(j,i);
                } else if (i == CENTER) {
                    tileIds[i] += "_"; // street left-right
                    roxels[j][i] = new Roxel(j,i);
                } else if (j == CENTER) {
                    tileIds[i] += "|"; // street top-bottom
                    roxels[j][i] = new Roxel(j,i);
                }
            }
        }
    }

}
