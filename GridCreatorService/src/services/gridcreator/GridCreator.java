package services.gridcreator;

import java.util.logging.Logger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;

import spaces.CompressedRoxelGrid;
import spaces.Configuration;
import spaces.Roxel;

@EventDriven
@Polling
@TransactionalEvent
public class GridCreator {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext(name = "gigaSpace")
    private GigaSpace gigaSpace;

    @EventTemplate
    public Configuration unprocessedDataTemplate() {
        Configuration data = new Configuration();
        data.setId("game1");
        data.setProcessed(false);
        return data;
    }

    @SpaceDataEvent
    public Configuration processMessage(Configuration msg) {
        logger.info("GridCreator PROCESSING : " + msg);
        createCompressedGrid(msg);
        createGrid(msg);
        msg.setProcessed(true);
        return msg;
    }

    public GridCreator() {
        logger.info("GridCreator instantiated...");
    }

    private void createCompressedGrid(Configuration c) {
        CompressedRoxelGrid crg = new CompressedRoxelGrid("game1_consolidated_grid",
                c.getBlockRoxelLength() * c.getBlocksX(),
                c.getBlockRoxelLength() * c.getBlocksY());
        logger.info("Created CompressedRoxelGrid: " + crg.toString());
        gigaSpace.write(crg);
    }

    private void createGrid(Configuration c) {
        for (int y = 0; y < c.getBlocksY(); y++) {
            for (int x = 0; x < c.getBlocksX(); x++) {
                createBlock(c, y, x);
            }
        }
    }

    private void createBlock(Configuration c, int y, int x) {
        int blockCenter = c.getBlockRoxelLength() / 2;
        for (int i = 0; i < c.getBlockRoxelLength(); i++) {
            for (int j = 0; j < c.getBlockRoxelLength(); j++) {
                if (i == blockCenter && j == blockCenter) {
                    createRoxel(c, y, x, i, j, new String("TODECIDE"));
                } else if (i == blockCenter) {
                    createRoxel(c, y, x, i, j, new String("EAST"));
                } else if (j == blockCenter) {
                    createRoxel(c, y, x, i, j, new String("SOUTH"));
                }
            }
        }
    }

    private void createRoxel(Configuration c, int y, int x, int i, int j,
            String direction) {
        Roxel tmp = new Roxel((x * c.getBlockRoxelLength()) + j,
                (y * c.getBlockRoxelLength()) + i, direction, new String(
                        "NOCAR"));
        gigaSpace.write(tmp);
    }
}
