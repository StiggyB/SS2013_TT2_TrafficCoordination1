package game;

import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import spaces.Configuration;
import spaces.DataGridConnectionUtility;
import spaces.Roxel;
import jgame.JGColor;
import jgame.JGPoint;
import jgame.platform.StdGame;

public class TrafficCenter extends StdGame {

    private static final int TILESIZE = 128;

    /**
     * 
     */
    private static final long serialVersionUID = -7986404341357251285L;

    GigaSpace tuplespace;

    Configuration conf = null;

    SQLQuery<Roxel> carsQuery;
    SQLQuery<Roxel> noCarsQuery;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new TrafficCenter(new JGPoint(TILESIZE, TILESIZE));

    }

    public TrafficCenter(JGPoint size) {
        connect();
        String carsQryStr = "state='CAR'";
        String noCarsQryStr = "state='NOCAR'";
        carsQuery = new SQLQuery<Roxel>(Roxel.class, carsQryStr);
        noCarsQuery = new SQLQuery<Roxel>(Roxel.class, noCarsQryStr);

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

    @Override
    public void initCanvas() {
        setCanvasSettings(conf.getBlockRoxelLength() * conf.getBlocksX(),
                conf.getBlockRoxelLength() * conf.getBlocksY(), TILESIZE,
                TILESIZE, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        setFrameRate(30, 2);
        defineMedia("mediatable.tbl");
    }

    Roxel[] carRoxels;
    Roxel[] noCarRoxels;
    int framecnt;

    public void doFrame() {
        if (framecnt > 2) {
            carRoxels = tuplespace.readMultiple(carsQuery);
            noCarRoxels = tuplespace.readMultiple(noCarsQuery);
            framecnt = 0;
        } else {
            framecnt++;
        }

    }

    public void paintFrame() {
        if (carRoxels != null) {
            for (Roxel r : carRoxels) {
                setColor(JGColor.red);
                drawRect(r.getX() * TILESIZE, r.getY() * TILESIZE, TILESIZE,
                        TILESIZE, true, false);
            }
        }
        if (noCarRoxels != null) {
            for (Roxel r : noCarRoxels) {
                setColor(JGColor.white);
                drawRect(r.getX() * TILESIZE, r.getY() * TILESIZE, TILESIZE,
                        TILESIZE, true, false);
            }
        }
    }
}
