package services.gridcreator;

import java.util.logging.Logger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.openspaces.remoting.RemotingService;
import org.springframework.beans.factory.annotation.Autowired;

import spaces.Configuration;
import spaces.Roxel;

@EventDriven
@Polling
@TransactionalEvent
public class GridCreator {
    Logger logger=Logger.getLogger(this.getClass().getName());
    
    @GigaSpaceContext(name = "gigaSpace")
    private GigaSpace gigaSpace;
    
    @EventTemplate
    public Configuration unprocessedDataTemplate() {
        Configuration data = new Configuration();
        data.setProcessed(false);
        return data;
    }
    
    @SpaceDataEvent
    public Configuration processMessage(Configuration msg) {
        logger.info("GridCreator PROCESSING : " + msg);
        createRoxels(msg);
        msg.setProcessed(true);
        return msg;
    }

    public GridCreator(){
        logger.info("GridCreator instantiated...");
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
                            gigaSpace.write(tmp);
                        } else if (i == blockCenter) {
                            Roxel tmp = new Roxel((x * c.getBlockRoxelLength())
                                    + j, (y * c.getBlockRoxelLength()) + i,
                                    new String("EAST"), new String("NOCAR"));
                            gigaSpace.write(tmp);
                        } else if (j == blockCenter) {
                            Roxel tmp = new Roxel((x * c.getBlockRoxelLength())
                                    + j, (y * c.getBlockRoxelLength()) + i,
                                    new String("SOUTH"), new String("NOCAR"));
                            gigaSpace.write(tmp);
                        }
                    }
                }
            }
        }
    }
}
