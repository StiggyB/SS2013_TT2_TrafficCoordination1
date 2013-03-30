package game;

import org.openspaces.core.GigaSpace;

import spaces.CompoundId;
import spaces.DataGridConnectionUtility;
import spaces.Roxel;
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
    private static int CROSSINGS_X = 6;
    /**
     * Y Size of Crossing Grid
     */
    private static int CROSSINGS_Y = 4;
    /**
     * side ratio for window sizing
     */
    private static double ratio = (double) CROSSINGS_Y / (double) CROSSINGS_X;
    /**
     * Crossing array
     */
    static Crossing[][] crossings;

    GigaSpace tuplespace;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Init Game
        new CarGame(new JGPoint(1000, (int) (1000 * ratio)));

    }

    public CarGame() {
        tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);

        initCrossings();

        initEngineApplet();
    }

    public CarGame(JGPoint size) {
        tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);

        initCrossings();

        initEngine(size.x, size.y);
    }

    /**
     * Initializes the graphical representation of the crossing grid
     */
    private void initCrossings() {
        crossings = new Crossing[CROSSINGS_X][CROSSINGS_Y];
        // Initialize Crossings
        for (int i = 0; i < CROSSINGS_Y; i++) {
            for (int j = 0; j < CROSSINGS_X; j++) {
                crossings[j][i] = new Crossing(j, i, tuplespace);
            }
        }
    }

    @Override
    public void initCanvas() {
        setCanvasSettings(Crossing.TILEWIDTH * CROSSINGS_X, Crossing.TILEWIDTH
                * CROSSINGS_Y, TILE_SIZE, TILE_SIZE, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        setFrameRate(30, 2);
        defineMedia("mediatable.tbl");
        setBGImage("background");
        // set crossing tiles
        for (int i = 0; i < CROSSINGS_Y; i++) {
            for (int j = 0; j < CROSSINGS_X; j++) {
                setTiles(crossings[j][i].x, crossings[j][i].y,
                        crossings[j][i].tileIds);
            }
        }
        // // create cars
        // for (int i = 0; i < 1; i++) {
        // createCar(i);
        // }
    }

    private void createCar(int id) {
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
        new Car(id, pfWidth(), pfHeight(), xPos, yPos, horizontal);
    }

    int carcnt = 0;

    public void doFrame() {
        if (getKey('N')) {
            createCar(carcnt);
            carcnt++;
            clearKey('N');
        }

        moveObjects(null,// object name prefix of objects to move (null means
                         // any)
                0 // object collision ID of objects to move (0 means any)
        );
        checkCollision(1, // cids of objects that our objects should collide
                          // with
                1 // cids of the objects whose hit() should be called
        );
    }

    public void paintFrame() {
        setColor(JGColor.black);
        for (int i = 0; i < CROSSINGS_Y * Crossing.TILEWIDTH; i++) {
            drawLine(0, i * TILE_SIZE, pfWidth(), i * TILE_SIZE);
            for (int j = 0; j < CROSSINGS_X * Crossing.TILEWIDTH; j++) {
                drawLine(j * TILE_SIZE, 0, j * TILE_SIZE, pfHeight());
            }
        }
    }

    class Car extends JGObject {
        int id;
        int screenWidth;
        int screenHeight;
        int currentRoxelX;
        int currentRoxelY;
        boolean horizontal;
        int waitcounter = 0;
        public double maxspeed = 2;

        JGColor textcolor = JGColor.white;

        CompoundId actRoxelID;

        Car(int id, int screenWidth, int screenHeight, int startposx,
                int startposy, boolean horizontal) {
            super("car", true, startposx, startposy, 1, null);
            this.id = id;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.currentRoxelX = roxelX(x);
            this.currentRoxelY = roxelY(y);
            this.horizontal = horizontal;
            // Give the object an initial speed in a random direction.
            if (horizontal) {
                xspeed = random(0.2, maxspeed);
                setGraphic("ocar");
            } else {
                yspeed = random(0.2, maxspeed);
                setGraphic("scar");
            }
            enterRoxel(currentRoxelX, currentRoxelY);
        }

        /** Update the object. This method is called by moveObjects. */
        public void move() {
            if (waitcounter == 0) {
                textcolor = JGColor.white;
                if (horizontal) {
                    setGraphic("ocar");
                } else {
                    setGraphic("scar");
                }
                // check if next step will enter new voxel
                double nextX = x + 5 * xspeed;
                double nextY = y + 5 * yspeed;
                if ((roxelX(nextX) != currentRoxelX || roxelY(nextY) != currentRoxelY)
                        && !isNextRoxelFree(nextX, nextY)) {
                    xspeed = 0;
                    yspeed = 0;
                    waitcounter = 10;
                } else {
                    accelerate();
                }

                if (roxelX(x) != currentRoxelX) {
                    System.out.println(getName() + " has entered roxel "
                            + roxelX(x) + "," + roxelY(y));
                    textcolor = JGColor.yellow;
                    goToRoxel(roxelX(x), roxelY(y));
                }
                if (roxelY(y) != currentRoxelY) {
                    System.out.println(getName() + " has entered roxel "
                            + roxelX(x) + "," + roxelY(y));
                    textcolor = JGColor.yellow;
                    goToRoxel(roxelX(x), roxelY(y));
                }

                // wrap position if overflow
                if (x > screenWidth && xspeed > 0)
                    x = 0;
                if (x < 0 && xspeed < 0)
                    x = screenWidth;
                if (y > screenHeight && yspeed > 0)
                    y = 0;
                if (y < 0 && yspeed < 0)
                    y = screenHeight;

            } else if (waitcounter < 10) {
                accelerate();

                waitcounter--;
            } else {
                waitcounter--;
            }
            currentRoxelX = roxelX(x);
            currentRoxelY = roxelY(y);
        }

        /** Draw the object. */
        public void paint() {
            setColor(textcolor);

            drawString(getName() + ": " + currentRoxelX + "_" + currentRoxelY,
                    x + TILE_SIZE / 2, y + TILE_SIZE / 10, 0);
            drawRect(x, y, 5, 5, true, false);
        }

        public void hit(JGObject obj) {
            if (waitcounter == 0 && (xspeed != 0 || yspeed != 0)) {
                textcolor = JGColor.red;
                // playAudio("crash");
            }
            Car opponent = (Car) obj;
            if ((xspeed != 0 && opponent.currentRoxelX > currentRoxelX && opponent.currentRoxelY >= currentRoxelY)
                    || (yspeed != 0 && opponent.currentRoxelY > currentRoxelY && opponent.currentRoxelX <= currentRoxelX)) {
                waitcounter = (int) random(50, 200);
                if (horizontal) {
                    setGraphic("ocarcrash");
                } else {
                    setGraphic("scarcrash");
                }
                xspeed = 0;
                yspeed = 0;
            } else {
                if (waitcounter == 0) {
                    waitcounter = 10;
                }
                // else {
                // accelerate();
                // }
            }
        }

        /**
         * 
         */
        private void accelerate() {
            if (horizontal) {
                if (xspeed < 1)
                    xspeed += random(0, maxspeed);
            } else {
                if (yspeed < 1)
                    yspeed += random(0, maxspeed);
            }
        }

        private boolean isNextRoxelFree(double nextX, double nextY) {
            CompoundId nextId = new CompoundId(roxelX(nextX), roxelY(nextY));
            Roxel next = tuplespace.readById(Roxel.class, nextId);
            if (next != null && next.getState().equals("NOCAR")) {
                return true;
            }
            return false;
        }

        /** calculate current roxel x coordinate from pixel x */
        private int roxelX(double x) {
            return (int) (x + TILE_SIZE - 1) / TILE_SIZE
                    % (CROSSINGS_X * Crossing.TILEWIDTH);
        }

        /** calculate current roxel y coordinate from pixel y */
        private int roxelY(double y) {
            return (int) (y + TILE_SIZE - 1) / TILE_SIZE
                    % (CROSSINGS_Y * Crossing.TILEWIDTH);
        }

        private void enterRoxel(int x, int y) {
            CompoundId nextId = new CompoundId(x, y);
            // Create Matching Template
            Roxel template = new Roxel();
            template.setId(nextId);
            template.setState("NOCAR");

            // Aquire next Roxel
            Roxel next = tuplespace.take(template);
            System.out.println(getName() + " InitialRoxel: " + nextId + " - "
                    + next);
            if (next != null) {
                if (next.getState().equals("NOCAR")) {
                    next.setState("CAR");
                }
                tuplespace.write(next);
            }
        }

        private void goToRoxel(int x, int y) {
            CompoundId actId = new CompoundId(currentRoxelX, currentRoxelY);
            CompoundId nextId = new CompoundId(x, y);

            System.out.println(getName() + " trying to get to Roxel " + nextId);
            // Create Matching Template
            Roxel template = new Roxel();
            template.setId(nextId);
            template.setState("NOCAR");

            // Aquire next Roxel
            Roxel next = tuplespace.take(template);
            if (next != null) {
                if (next.getState().equals("NOCAR")) {
                    next.setState("CAR");
                }
                System.out.println(getName() + " NextRoxel: " + nextId + " - "
                        + next);
                tuplespace.write(next);
            }

            // Reset current Roxels state
            Roxel act = tuplespace.takeById(Roxel.class, actId);
            if (act != null) {
                if (act.getState().equals("CAR")) {
                    act.setState("NOCAR");
                }
                System.out.println(getName() + " ActRoxel: " + actId + " - "
                        + act);
                tuplespace.write(act);
            }
        }
    }

}
