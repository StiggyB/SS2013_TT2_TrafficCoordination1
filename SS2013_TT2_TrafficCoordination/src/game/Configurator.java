package game;

import java.util.logging.Logger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import com.j_spaces.core.IJSpace;

import spaces.Configuration;
import spaces.DataGridConnectionUtility;
import spaces.Roxel;

public class Configurator {
    Logger logger = Logger.getLogger(this.getClass().getName());
    GigaSpace tuplespace;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);

        Configurator conf = new Configurator("jini://*/*/streetGrid");

        conf.createConfiguration();

        conf.readResults();

        System.exit(0);
    }

    public Configurator(String url) {
        System.out.println("Connecting to data grid.");
        // connect to the space using its URL
        IJSpace space = new UrlSpaceConfigurer(url).space();
        if (space == null) {
            System.out.println("IJSpace == null !!!");
        }
        // use gigaspace wrapper to for simpler API
        this.tuplespace = new GigaSpaceConfigurer(space).gigaSpace();
        if (tuplespace == null) {
            System.out.println("tuplespace == null !!!");
        }
    }

    private void createConfiguration() {
        Configuration c = new Configuration("game1", 10, 1, 1, 3, false);

        System.out.println("Check for existing configuration.");
        Configuration existingConf = tuplespace.readById(Configuration.class,
                "game1");

        if (existingConf == null) {
            System.out.println("No Configuration existing, creating.");
            tuplespace.write(c);
            // createRoxels(c);
        } else {
            System.out.println("Configuration existing: "
                    + existingConf.toString());
            existingConf = tuplespace.takeById(Configuration.class, "game1");
            existingConf.setProcessed(false);
            tuplespace.write(existingConf);
            // createRoxels(c);
        }
    }

    public void readResults() {

        Configuration template = new Configuration(); // Create a template to
                                                      // read a Message with
                                                      // info
        template.setProcessed(true); // attribute that equals "Hello World !!"
        template.setId("game1");
        // Read an object matching the template
        System.out.println("Here is one of them printed out: "
                + tuplespace.read(template));

        // wait 100 millis for all to be processed:
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) { /* do nothing */
        }

        // Count number of objects in the space matching the template
        int numInSpace = tuplespace.count(template);

        System.out.println("There are " + numInSpace
                + " processed Message objects in the space now.");
    }

    private void createRoxels(Configuration c) {
        int blockCenter = c.getBlockRoxelLength() / 2;
        for (int y = 0; y < c.getBlocksY(); y++) {
            for (int x = 0; x < c.getBlocksX(); x++) {
                for (int i = 0; i < c.getBlockRoxelLength(); i++) {
                    for (int j = 0; j < c.getBlockRoxelLength(); j++) {
                        if (i == blockCenter && j == blockCenter) {
                            Roxel tmp = new Roxel((x * c.getBlockRoxelLength())
                                    + j, (y * c.getBlockRoxelLength()) + i,
                                    new String("TODECIDE"), new String("NOCAR"));
                            tuplespace.write(tmp);
                        } else if (i == blockCenter) {
                            Roxel tmp = new Roxel((x * c.getBlockRoxelLength())
                                    + j, (y * c.getBlockRoxelLength()) + i,
                                    new String("EAST"), new String("NOCAR"));
                            tuplespace.write(tmp);
                        } else if (j == blockCenter) {
                            Roxel tmp = new Roxel((x * c.getBlockRoxelLength())
                                    + j, (y * c.getBlockRoxelLength()) + i,
                                    new String("SOUTH"), new String("NOCAR"));
                            tuplespace.write(tmp);
                        }
                    }
                }
            }
        }
    }
}
