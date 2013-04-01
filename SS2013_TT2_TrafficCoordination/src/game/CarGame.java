package game;

import org.openspaces.core.GigaSpace;

import spaces.CompoundId;
import spaces.Configuration;
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
     * side ratio for window sizing
     */
    private static double ratio = 3;
    /**
     * Crossing array
     */
    static Block[][] blocks;

    GigaSpace tuplespace;

    Configuration conf = null;

    boolean showGrid = false;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Init Game
        new CarGame(new JGPoint((int) (TILE_SIZE * ratio),
                (int) (TILE_SIZE * ratio)));

    }

    public CarGame() {
        connect();

        initCrossings();

        initEngineApplet();
    }

    public CarGame(JGPoint size) {
        connect();

        initCrossings();

        initEngine(size.x * conf.getBlocksX(), size.y * conf.getBlocksY());
    }

    /**
     * Connects to the data grid and gets the configuration. Exits the
     * application if no Configuration was found after several retries.
     */
    private void connect() {
        tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);
        int retryCounter = 0;
        System.out.print("Getting Configuration");
        while (conf == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            conf = tuplespace.readById(Configuration.class, "game1");
            retryCounter++;
            System.out.print(".");
            if (retryCounter > 20) {
                System.out.println("\nNo Configuration found, exiting!");
                System.exit(-1);
            }
        }
        System.out.println("\nConfiguration: " + conf.toString());

    }

    /**
     * Initializes the graphical representation of the block grid
     */
    private void initCrossings() {
        blocks = new Block[conf.getBlocksX()][conf.getBlocksY()];
        // Initialize Crossings
        for (int i = 0; i < conf.getBlocksY(); i++) {
            for (int j = 0; j < conf.getBlocksX(); j++) {
                blocks[j][i] = new Block(j, i, conf.getBlockRoxelLength(),
                        tuplespace);
            }
        }
    }

    @Override
    public void initCanvas() {
        setCanvasSettings(conf.getBlockRoxelLength() * conf.getBlocksX(),
                conf.getBlockRoxelLength() * conf.getBlocksY(), TILE_SIZE,
                TILE_SIZE, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        setFrameRate(30, 2);
        defineMedia("mediatable.tbl");
        setBGImage("background");
        // set block tiles
        for (int i = 0; i < conf.getBlocksY(); i++) {
            for (int j = 0; j < conf.getBlocksX(); j++) {
                setTiles(blocks[j][i].x, blocks[j][i].y, blocks[j][i].tileIds);
            }
        }
        // // create cars
        // for (int i = 0; i < 2; i++) {
        // createCar(i);
        // }
    }

    private void createCar(int id) {
        boolean horizontal = false;
        int xPos, yPos;

        // select block coordinates
        int startCrossingX = (int) random(0, conf.getBlocksX());
        int startCrossingY = (int) random(0, conf.getBlocksY());
        double direction = random(0, 1);
        if (direction <= 0.5)
            horizontal = true;

        // in pixels
        xPos = startCrossingX * conf.getBlockRoxelLength() * TILE_SIZE;
        yPos = startCrossingY * conf.getBlockRoxelLength() * TILE_SIZE;
        // onto the road
        if (horizontal) {
            yPos += (TILE_SIZE * (int) (conf.getBlockRoxelLength() / 2));
        } else {
            xPos += (TILE_SIZE * (int) (conf.getBlockRoxelLength() / 2));
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
        if (getKey('G')) {
            showGrid = !showGrid;
            clearKey('G');
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
        if (showGrid) {
            setColor(JGColor.black);
            for (int i = 0; i < conf.getBlocksY() * conf.getBlockRoxelLength(); i++) {
                drawLine(0, i * TILE_SIZE, pfWidth(), i * TILE_SIZE);
                for (int j = 0; j < conf.getBlocksX()
                        * conf.getBlockRoxelLength(); j++) {
                    drawLine(j * TILE_SIZE, 0, j * TILE_SIZE, pfHeight());
                }
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
        public double maxspeed = 3;
        public double minspeed = 0.5;

        JGColor textcolor = JGColor.white;

        CompoundId actRoxelID;

        Car(int id, int screenWidth, int screenHeight, int startposx,
                int startposy, boolean horizontal) {
            super("car", true, startposx, (horizontal ? startposy + TILE_SIZE
                    / 2 : startposy), 1, null);
            this.id = id;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.currentRoxelX = roxelX(x);
            this.currentRoxelY = roxelY((horizontal ? startposy + TILE_SIZE / 2
                    : startposy));
            this.horizontal = horizontal;
            // Give the object an initial speed in a random direction.
            if (horizontal) {
                xspeed = random(minspeed, maxspeed);
                setGraphic("ocar");
            } else {
                yspeed = random(minspeed, maxspeed);
                setGraphic("scar");
            }
            enterRoxel(currentRoxelX, currentRoxelY);
        }

        /** Update the object. This method is called by moveObjects. */
        public void move() {
            textcolor = JGColor.white;
            int nextRoxelX, nextRoxelY;

            // wrap position if overflow
            if (x > screenWidth && xspeed > 0)
                x = 0;
            if (x < 0 && xspeed < 0)
                x = screenWidth;
            if (y > screenHeight && yspeed > 0)
                y = 0;
            if (y < 0 && yspeed < 0)
                y = screenHeight;

            int lastRoxelX = currentRoxelX;
            int lastRoxelY = currentRoxelY;
            currentRoxelX = roxelX(x);
            currentRoxelY = roxelY(y);

            if (horizontal) {
                setGraphic("ocar");
                nextRoxelX = (currentRoxelX + 1)
                        % (conf.getBlocksX() * conf.getBlockRoxelLength());
                nextRoxelY = currentRoxelY;
            } else {
                setGraphic("scar");
                nextRoxelX = currentRoxelX;
                nextRoxelY = (currentRoxelY + 1)
                        % (conf.getBlocksY() * conf.getBlockRoxelLength());
            }

            if (xspeed == 0 && yspeed == 0) {
                accelerate();
            }

            if ((roxelX(x + TILE_SIZE / 2 + 5 * xspeed) >= nextRoxelX)
                    || (roxelY(y + TILE_SIZE / 2 + 5 * yspeed) >= nextRoxelY)) {
                if (!isNextRoxelFree(nextRoxelX, nextRoxelY)) {
                    decelerate();
                }
            }

            if ((roxelX(x + TILE_SIZE / 2 + xspeed) == nextRoxelX)
                    && (roxelX(x + TILE_SIZE / 2) != nextRoxelX)
                    || (roxelY(y + TILE_SIZE / 2 + yspeed) == nextRoxelY)
                    && (roxelY(y + TILE_SIZE / 2) != nextRoxelY)) {
                goToRoxel(nextRoxelX, nextRoxelY);
            }

            if ((roxelX(x + xspeed) > currentRoxelX)
                    || ((roxelX(x + xspeed) == 0) && (currentRoxelX != 0))
                    || (roxelY(y + yspeed) > currentRoxelY)
                    || ((roxelY(y + yspeed) == 0) && (currentRoxelY != 0))) {
                leaveRoxel(currentRoxelX, currentRoxelY);
            }

        }

        /** Draw the object. */
        public void paint() {
            setColor(textcolor);

            drawString(getName() + ": " + currentRoxelX + "_" + currentRoxelY,
                    x + TILE_SIZE / 2, y + TILE_SIZE / 10, 0);
            setColor(JGColor.blue);

            drawRect(x, y, 5, 5, true, false);
        }

        public void hit(JGObject obj) {
            Car opponent = (Car) obj;
            // // check if we have just hit somebody
            // if (waitcounter == 0 && (xspeed != 0 || yspeed != 0)) {
            // // check if we're actually hitting somebody
            // if (currentRoxelX == opponent.curr)
            textcolor = JGColor.red;
            playAudio("crash");
            // }
            //
            if ((xspeed != 0 && opponent.currentRoxelX > currentRoxelX && opponent.currentRoxelY >= currentRoxelY)
                    || (yspeed != 0 && opponent.currentRoxelY > currentRoxelY && opponent.currentRoxelX <= currentRoxelX)) {
                waitcounter = (int) random(50, 200);
                if (horizontal) {
                    setGraphic("ocarcrash");
                } else {
                    setGraphic("scarcrash");
                }
            }
        }

        private boolean isNextRoxelFree(double nextX, double nextY) {
            CompoundId nextId = new CompoundId(roxelX(nextX), roxelY(nextY));
            Roxel template = new Roxel();
            template.setId(nextId);

            Roxel next = tuplespace.read(template);
            

            if (next != null) {
                System.out.println(getName() + " has to brake");
                return false;
            } else {
                System.out.println("Tuple was NULL");
            }
            return true;
        }

        /**
         * 
         */
        private void accelerate() {
            if (horizontal) {
                if (xspeed < maxspeed)
                    xspeed += random(minspeed, maxspeed);
            } else {
                if (yspeed < maxspeed)
                    yspeed += random(minspeed, maxspeed);
            }
        }

        /**
         * 
         */
        private void decelerate() {
            if (horizontal) {
                if (xspeed > 0.5)
                    xspeed -= random(0.01, 0.5);
            } else {
                if (yspeed > 0.5)
                    yspeed -= random(0.01, 0.5);
            }
        }

        /** calculate current roxel x coordinate from pixel x */
        private int roxelX(double x) {
            return (int) (x) / TILE_SIZE
                    % (conf.getBlocksX() * conf.getBlockRoxelLength());
        }

        /** calculate current roxel y coordinate from pixel y */
        private int roxelY(double y) {
            return (int) (y) / TILE_SIZE
                    % (conf.getBlocksY() * conf.getBlockRoxelLength());
        }

        private void enterRoxel(int x, int y) {
            CompoundId nextId = new CompoundId(x, y);
            // Create Matching Template
            Roxel template = new Roxel();
            template.setId(nextId);
            template.setX(x);
            template.setY(y);
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
            CompoundId nextId = new CompoundId(x, y);

            // System.out.println(getName() + " trying to get to Roxel " +
            // nextId);
            // Create Matching Template
            Roxel template = new Roxel();
            template.setId(nextId);
            template.setX(x);
            template.setY(y);
            template.setState("NOCAR");

            // Aquire next Roxel
            Roxel next = tuplespace.take(template);
            if (next != null) {
                if (next.getState().equals("NOCAR")) {
                    next.setState("CAR");
                }
                // System.out.println(getName() + " NextRoxel: " + nextId +
                // " - "
                // + next);
                tuplespace.write(next);
                // Reset current Roxels state

            } else {
                xspeed = 0;
                yspeed = 0;
            }

        }

        private void leaveRoxel(int x, int y) {
            CompoundId actId = new CompoundId(x, y);
            Roxel template = new Roxel();
            template.setId(actId);
            template.setX(currentRoxelX);
            template.setY(currentRoxelY);
            template.setState("CAR");
            Roxel act = tuplespace.take(template);

            if (act != null) {
                if (act.getState().equals("CAR")) {
                    act.setState("NOCAR");
                }
                // System.out.println(getName() + " ActRoxel: " + actId + " - "
                // + act);
                tuplespace.write(act);
            }
        }
    }

}
