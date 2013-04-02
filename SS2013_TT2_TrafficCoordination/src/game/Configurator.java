package game;

import org.openspaces.core.GigaSpace;

import spaces.Configuration;
import spaces.DataGridConnectionUtility;
import spaces.Roxel;

public class Configurator {
    static GigaSpace tuplespace;

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Connecting to data grid.");
        tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);

        Configuration c = new Configuration("game1", 10, 1, 1, 3);

        System.out.println("Check for existing configuration.");
        Configuration existingConf = tuplespace.readById(Configuration.class,
                "game1");

        if (existingConf == null) {
            System.out.println("No Configuration existing, creating.");
            tuplespace.write(c);
            createRoxels(c);
        } else {
            System.out.println("Configuration existing: "
                    + existingConf.toString());
            createRoxels(c);
        }

        System.exit(0);
    }

    private static void createRoxels(Configuration c) {
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
