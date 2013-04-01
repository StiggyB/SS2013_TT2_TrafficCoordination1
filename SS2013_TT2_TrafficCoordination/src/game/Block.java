package game;

import org.openspaces.core.GigaSpace;

import spaces.Roxel;

/**
 * One Crossing
 */
public class Block {
    /* Position on field in tiles */
    int x;
    int y;

    /** Tiles per Crossing */
    /** Center Tile */
    private int blockCenter;

    /** TilesNames for Graphics */
    String[] tileIds;

    /** RoxelSpace TODO: Has to be global */
    // Roxel[][] roxels;

    public Block(int x, int y, int roxelWidth, GigaSpace tuplespace) {
        this.blockCenter = roxelWidth / 2;
        this.x = x * roxelWidth;
        this.y = y * roxelWidth;
        this.tileIds = new String[roxelWidth];
        // roxels = new Roxel[TILEWIDTH][TILEWIDTH];

        for (int i = 0; i < roxelWidth; i++) {
            tileIds[i] = new String("");
            for (int j = 0; j < roxelWidth; j++) {
                if ((i < blockCenter && j < blockCenter) || (i > blockCenter && j > blockCenter)
                        || (i < blockCenter && j > blockCenter)
                        || (i > blockCenter && j < blockCenter)) {
                    tileIds[i] += "."; // no graphics
                } else if (i == blockCenter && j == blockCenter) {
                    tileIds[i] += "#"; // crossing
                    // roxels[j][i]
//                    Roxel tmp = new Roxel((x * roxelWidth) + j, (y * roxelWidth)
//                            + i, new String("TODECIDE"), new String("NOCAR"));
//                    tuplespace.write(tmp);
                } else if (i == blockCenter) {
                    tileIds[i] += "_"; // street left-right
//                    Roxel tmp = new Roxel((x * roxelWidth) + j, (y * roxelWidth)
//                            + i, new String("EAST"), new String("NOCAR"));
//                    tuplespace.write(tmp);
                } else if (j == blockCenter) {
                    tileIds[i] += "|"; // street top-bottom
//                    Roxel tmp = new Roxel((x * roxelWidth) + j, (y * roxelWidth)
//                            + i, new String("SOUTH"), new String("NOCAR"));
//                    tuplespace.write(tmp);
                }
            }
        }
    }

}
