package game;

import jgame.JGColor;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.StdGame;

public class CarGame extends StdGame {

    private static final int TILESIZE = 64;
    private static final long serialVersionUID = -6899853102902331390L;
    private static int CROSSINGX = 2;
    private static int CROSSINGY = 1;
    static Crossing[][] crossings;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new CarGame(new JGPoint(1600, 800));
        crossings = new Crossing[CROSSINGX][CROSSINGY];
        for (int i = 0; i < CROSSINGY; i++) {
            for (int j = 0; j < CROSSINGX; j++) {
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
        setCanvasSettings(Crossing.TILEWIDTH * CROSSINGX, Crossing.TILEWIDTH
                * CROSSINGY, TILESIZE, TILESIZE, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        setFrameRate(35, 2);
        defineMedia("mediatable.tbl");
        setBGImage("background");
        for (int i = 0; i < CROSSINGY; i++) {
            for (int j = 0; j < CROSSINGX; j++) {
                setTiles(crossings[j][i].x, crossings[j][i].y,
                        crossings[j][i].tileIds);
            }
        }
        for (int i = 0; i < 20; i++) {
            createCar();
        }
    }

    private void createCar() {
        boolean horizontal = false;
        int xPos, yPos;

        int startCrossingX = (int) random(0, CROSSINGX);
        int startCrossingY = (int) random(0, CROSSINGY);
        double direction = random(0, 1);
        if (direction <= 0.5)
            horizontal = true;

        xPos = startCrossingX * Crossing.TILEWIDTH * TILESIZE;
        yPos = startCrossingY * Crossing.TILEWIDTH * TILESIZE;

        if (horizontal) {
            yPos += (TILESIZE * Crossing.TILEWIDTH / 2);
        } else {
            xPos += (TILESIZE * Crossing.TILEWIDTH / 2);
        }

        new Car(xPos, yPos, horizontal);
    }

    public void doFrame() {
        moveObjects(null,// object name prefix of objects to move (null means
                         // any)
                0 // object collision ID of objects to move (0 means any)
        );
    }

    public void paintFrame() {

    }

    class Car extends JGObject {
        Car(int startposx, int startposy, boolean horizontal) {
            super("car", true, startposx, startposy, 1, null);

            // Give the object an initial speed in a random direction.
            if (horizontal) {
                xspeed = random(0, 2);
            } else {
                yspeed = random(0, 2);
            }
        }

        /** Update the object. This method is called by moveObjects. */
        public void move() {
            if (x > pfWidth() + 8 && xspeed > 0)
                x = -8;
            if (x < -8 && xspeed < 0)
                x = pfWidth() + 8;
            if (y > pfHeight() + 8 && yspeed > 0)
                y = -8;
            if (y < -8 && yspeed < 0)
                y = pfHeight() + 8;
        }

        /** Draw the object. */
        public void paint() {
            // Draw a yellow ball
            setColor(JGColor.yellow);
            
            drawRect(x, y, 16, 16, true, true);
        }
    }
}
