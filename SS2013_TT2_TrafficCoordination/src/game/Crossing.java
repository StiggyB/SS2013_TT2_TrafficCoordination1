package game;

import org.openspaces.core.GigaSpace;

import spaces.Roxel;

/**
 * One Crossing
 */
public class Crossing {
    /* Position on field in tiles */
    int x;
    int y;

    /** Tiles per Crossing */
    static int TILEWIDTH = 5;
    /** Center Tile */
    private int CENTER = TILEWIDTH / 2;

    /** TilesNames for Graphics */
    String[] tileIds;

    /** RoxelSpace TODO: Has to be global */
    // Roxel[][] roxels;

    public Crossing(int x, int y, GigaSpace tuplespace) {
        this.x = x * TILEWIDTH;
        this.y = y * TILEWIDTH;
        this.tileIds = new String[TILEWIDTH];
        // roxels = new Roxel[TILEWIDTH][TILEWIDTH];

        for (int i = 0; i < TILEWIDTH; i++) {
            tileIds[i] = new String("");
            for (int j = 0; j < TILEWIDTH; j++) {
                if ((i < CENTER && j < CENTER) || (i > CENTER && j > CENTER)
                        || (i < CENTER && j > CENTER)
                        || (i > CENTER && j < CENTER)) {
                    tileIds[i] += "."; // no graphics
                } else if (i == CENTER && j == CENTER) {
                    tileIds[i] += "#"; // crossing
                    // roxels[j][i]
                    Roxel tmp = new Roxel((x * TILEWIDTH) + j, (y * TILEWIDTH)
                            + i, new String("TODECIDE"), new String("NOCAR"));
                    tuplespace.write(tmp);
                } else if (i == CENTER) {
                    tileIds[i] += "_"; // street left-right
                    Roxel tmp = new Roxel((x * TILEWIDTH) + j, (y * TILEWIDTH)
                            + i, new String("EAST"), new String("NOCAR"));
                    tuplespace.write(tmp);
                } else if (j == CENTER) {
                    tileIds[i] += "|"; // street top-bottom
                    Roxel tmp = new Roxel((x * TILEWIDTH) + j, (y * TILEWIDTH)
                            + i, new String("SOUTH"), new String("NOCAR"));
                    tuplespace.write(tmp);
                }
            }
        }
    }

}
