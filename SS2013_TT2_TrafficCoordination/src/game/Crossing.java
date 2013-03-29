package game;

/**
 * One Crossing
 */
public class Crossing {
    /* Position on field in tiles */
    int x;
    int y;

    /** Tiles per Crossing */
    static int TILEWIDTH = 3;
    /** Center Tile */
    private int CENTER = TILEWIDTH / 2;

    /** TilesNames for Graphics */
    String[] tileIds;
    /** RoxelSpace TODO: Has to be global*/
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
                    roxels[j][i] = new Roxel(x+j,y+i);
                } else if (i == CENTER) {
                    tileIds[i] += "_"; // street left-right
                    roxels[j][i] = new Roxel(x+j,y+i);
                } else if (j == CENTER) {
                    tileIds[i] += "|"; // street top-bottom
                    roxels[j][i] = new Roxel(x+j,y+i);
                }
            }
        }
    }

}
