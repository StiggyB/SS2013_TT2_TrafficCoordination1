package game;

import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import spaces.Configuration;
import spaces.DataGridConnectionUtility;
import spaces.Roxel;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.JGEngine;
import jgame.platform.StdGame;

public class DataGridState extends StdGame {

    private static final int TILESIZE = 256;

    /**
     * 
     */
    private static final long serialVersionUID = -7986404341357251285L;

    GigaSpace tuplespace;

    Configuration conf = null;

    SQLQuery<Roxel> carsQuery;
    SQLQuery<Roxel> noCarsQuery;
    SQLQuery<Roxel> southQuery;
    SQLQuery<Roxel> eastQuery;
    SQLQuery<Roxel> crossingQuery;
    SQLQuery<Roxel> southCarsQuery;
    SQLQuery<Roxel> eastCarsQuery;
    SQLQuery<Roxel> crossingCarsQuery;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new DataGridState(new JGPoint(TILESIZE, TILESIZE));

    }

    public DataGridState(JGPoint size) {
        connect();
        String carsQryStr = "state='CAR'";
        String noCarsQryStr = "state='NOCAR'";
        String southQryStr = "direction='SOUTH'";
        String eastQryStr = "direction='EAST'";
        String crossQryStr = "direction='TODECIDE'";
        String southCarsQryStr = "direction='SOUTH' AND state='CAR'";
        String eastCarsQryStr = "direction='EAST' AND state='CAR'";
        String crossCarsQryStr = "direction='TODECIDE' AND state='CAR'";
        carsQuery = new SQLQuery<Roxel>(Roxel.class, carsQryStr);
        noCarsQuery = new SQLQuery<Roxel>(Roxel.class, noCarsQryStr);
        southQuery = new SQLQuery<Roxel>(Roxel.class, southQryStr);
        eastQuery = new SQLQuery<Roxel>(Roxel.class, eastQryStr);
        crossingQuery = new SQLQuery<Roxel>(Roxel.class, crossQryStr);
        southCarsQuery = new SQLQuery<Roxel>(Roxel.class, southCarsQryStr);
        eastCarsQuery = new SQLQuery<Roxel>(Roxel.class, eastCarsQryStr);
        crossingCarsQuery = new SQLQuery<Roxel>(Roxel.class, crossCarsQryStr);

        initEngine(size.x * conf.getBlocksX(), size.y * conf.getBlocksY()+3);
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
                conf.getBlockRoxelLength() * conf.getBlocksY() + 3, TILESIZE,
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
    float south_util;
    float east_util;
    float crossing_util;

    public void doFrame() {
        if (framecnt > 2) {
            Roxel[] tmp;
            carRoxels = tuplespace.readMultiple(carsQuery);
            noCarRoxels = tuplespace.readMultiple(noCarsQuery);
            tmp = tuplespace.readMultiple(southQuery);
            float southRoxels = tmp.length;
            tmp = tuplespace.readMultiple(eastQuery);
            float eastRoxels = tmp.length;
            tmp = tuplespace.readMultiple(southCarsQuery);
            float southCarRoxels = tmp.length;
            tmp = tuplespace.readMultiple(eastCarsQuery);
            float eastCarsRoxels = tmp.length;
            tmp = tuplespace.readMultiple(crossingQuery);
            float crossRoxels = tmp.length;
            tmp = tuplespace.readMultiple(crossingCarsQuery);
            float crossCarsRoxels = tmp.length;

            south_util = southCarRoxels / southRoxels * 100;
            east_util = eastCarsRoxels / eastRoxels * 100;
            crossing_util = crossCarsRoxels / crossRoxels * 100;
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
        setFont(new JGFont(null, JGFont.PLAIN, 50));
        drawString("South street utilization: " + south_util + "%", 10,
                conf.getBlocksY() * conf.getBlockRoxelLength() * TILESIZE + 10,
                -1);
        drawString("East street utilization: " + east_util + "%", 10,
                conf.getBlocksY() * conf.getBlockRoxelLength() * TILESIZE + 70,
                -1);
        drawString(
                "Crossing utilization: " + crossing_util + "%",
                10,
                conf.getBlocksY() * conf.getBlockRoxelLength() * TILESIZE + 130,
                -1);
    }
}
