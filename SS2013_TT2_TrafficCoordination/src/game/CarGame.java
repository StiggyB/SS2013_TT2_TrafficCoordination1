package game;

import jgame.JGColor;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.StdGame;

public class CarGame extends StdGame {
    private static final long serialVersionUID = -6899853102902331390L;
    /**
     * Pixel length of textures
     */
    private static final int TILE_SIZE = 64;
    /**
     * X Size of Crossing Grid
     */
    private static int CROSSINGS_X = 8;
    /**
     * Y Size of Crossing Grid
     */
    private static int CROSSINGS_Y = 4;
    /**
     * side ratio for window sizing
     */
    private static double ratio = (double)CROSSINGS_Y/ (double)CROSSINGS_X;
    /**
     * Crossing array
     */
    static Crossing[][] crossings;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Init Game
        new CarGame(new JGPoint(1600, (int)(1600*ratio)));
        
        crossings = new Crossing[CROSSINGS_X][CROSSINGS_Y];
        // Initialize Crossings
        for (int i = 0; i < CROSSINGS_Y; i++) {
            for (int j = 0; j < CROSSINGS_X; j++) {
                crossings[j][i] = new Crossing(j, i);
            }
        }

    }

    public CarGame() {
        initEngineApplet();
    }

    public CarGame(JGPoint size) {
        initEngine(size.x, size.y);
    }

    @Override
    public void initCanvas() {
        setCanvasSettings(Crossing.TILEWIDTH * CROSSINGS_X, Crossing.TILEWIDTH
                * CROSSINGS_Y, TILE_SIZE, TILE_SIZE, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        setFrameRate(35, 2);
        defineMedia("mediatable.tbl");
        setBGImage("background");
        // set crossing tiles
        for (int i = 0; i < CROSSINGS_Y; i++) {
            for (int j = 0; j < CROSSINGS_X; j++) {
                setTiles(crossings[j][i].x, crossings[j][i].y,
                        crossings[j][i].tileIds);
            }
        }
        // create cars
        for (int i = 0; i < 5; i++) {
            createCar();
        }
    }

    private void createCar() {
        boolean horizontal = false;
        int xPos, yPos;

        // select crossing coordinates
        int startCrossingX = (int) random(0, CROSSINGS_X);
        int startCrossingY = (int) random(0, CROSSINGS_Y);
        double direction = random(0, 1);
        if (direction <= 0.5)
            horizontal = true;

        // in pixels
        xPos = startCrossingX * Crossing.TILEWIDTH * TILE_SIZE;
        yPos = startCrossingY * Crossing.TILEWIDTH * TILE_SIZE;
        // onto the road
        if (horizontal) {
            yPos += (TILE_SIZE * (int) (Crossing.TILEWIDTH / 2));
        } else {
            xPos += (TILE_SIZE * (int) (Crossing.TILEWIDTH / 2));
        }
        // create car
        new Car(xPos, yPos, horizontal);
    }

    public void doFrame() {
        moveObjects(null,// object name prefix of objects to move (null means
                         // any)
                0 // object collision ID of objects to move (0 means any)
        );
        checkCollision(
                1, // cids of objects that our objects should collide with
                1  // cids of the objects whose hit() should be called
            );
    }

    public void paintFrame() {

    }

    class Car extends JGObject {
        int currentRoxelX;
        int currentRoxelY;

        Car(int startposx, int startposy, boolean horizontal) {
            super("car", true, startposx, startposy, 1, null);
            // Give the object an initial speed in a random direction.
            if (horizontal) {
                xspeed = random(0, 2);
            } else {
                yspeed = random(0, 2);
            }
            if (horizontal) {
                setGraphic("ocar");
            } else {
                setGraphic("scar");
            }
            this.currentRoxelX = roxelX(x);
            this.currentRoxelY = roxelY(y);
        }

        /** Update the object. This method is called by moveObjects. */
        public void move() {
            // wrap position if overflow
            if (x > pfWidth() + 8 && xspeed > 0)
                x = -8;
            if (x < -8 && xspeed < 0)
                x = pfWidth() + 8;
            if (y > pfHeight() + 8 && yspeed > 0)
                y = -8;
            if (y < -8 && yspeed < 0)
                y = pfHeight() + 8;
            // calculate current roxel
            currentRoxelX = roxelX(x);
            currentRoxelY = roxelY(y);

            // check if next step will enter new voxel
            double nextX = x + xspeed;
            double nextY = y + yspeed;
            if (roxelX(nextX) != currentRoxelX) {
                System.out.println("car will enter roxel " + roxelX(nextX)
                        + "," + roxelY(nextY));
            }
            if (roxelY(nextY) != currentRoxelY) {
                System.out.println("car will enter roxel " + roxelX(nextX)
                        + "," + roxelY(nextY));
            }
        }

        /** Draw the object. */
        public void paint() {
            setColor(JGColor.white);
            // Draw a text that moves around in a circle.
            // Note: viewWidth returns the width of the view;
            // viewHeight the height.
            drawString("Roxel:" + currentRoxelX + "," + currentRoxelY, x
                    + TILE_SIZE / 2, // xpos
                    y + TILE_SIZE / 10, // ypos
                    0 // the text alignment
                      // (-1 is left-aligned, 0 is centered, 1 is right-aligned)
            );
        }
        
        public void hit(JGObject obj) {
            playAudio("crash");
        }
        /** calculate current roxel x coordinate from pixel x */
        private int roxelX(double x) {
            return (int) x / TILE_SIZE % (CROSSINGS_X * Crossing.TILEWIDTH);
        }

        /** calculate current roxel y coordinate from pixel y */
        private int roxelY(double y) {
            return (int) y / TILE_SIZE % (CROSSINGS_Y * Crossing.TILEWIDTH);
        }
    }
}
