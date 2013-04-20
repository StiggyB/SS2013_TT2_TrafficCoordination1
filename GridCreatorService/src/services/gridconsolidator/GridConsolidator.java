package services.gridconsolidator;

import java.util.ArrayList;
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

import spaces.CompressedRoxelGrid;
import spaces.Configuration;
import spaces.Roxel;

@EventDriven
@Polling
@TransactionalEvent
public class GridConsolidator {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext(name = "gigaSpace")
    private GigaSpace gigaSpace;

    @EventTemplate
    public Roxel unprocessedDataTemplate() {
        Roxel data = new Roxel();
        return data;
    }

    @SpaceDataEvent
    public Roxel processMessage(Roxel msg) {
        logger.info("GridConsolidator PROCESSING : " + msg);
        CompressedRoxelGrid template = new CompressedRoxelGrid();
        template.setId("game1");
        template.setOccupiedRoxels(new ArrayList<Boolean>(0));
        
        CompressedRoxelGrid crg = gigaSpace.take(template);
        // FIXME: ^^^ returns null, therefore crashes...
        if (msg.getState().equals("NOCAR")
                && crg.getOccupiedRoxels(msg.getX(), msg.getY()) == true) {
            crg.setOccupiedRoxels(msg.getX(), msg.getY(), false);
        } else if (msg.getState().equals("CAR")
                && crg.getOccupiedRoxels(msg.getX(), msg.getY()) == false) {
            crg.setOccupiedRoxels(msg.getX(), msg.getY(), true);
        }
        gigaSpace.write(crg);
        return msg;
    }

    public GridConsolidator() {
        logger.info("GridConsolidator instantiated...");
    }

}
