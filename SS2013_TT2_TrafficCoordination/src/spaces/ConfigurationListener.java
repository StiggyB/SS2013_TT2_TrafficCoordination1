package spaces;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.SpaceDataEventListener;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;

@EventDriven
@Notify
public class ConfigurationListener {

	private GigaSpace tuplespace;

	public ConfigurationListener(GigaSpace tuplespace) {
		this.tuplespace = tuplespace;
	}

	@EventTemplate
	Configuration unprocessedData() {
		Configuration template = new Configuration();
		template.setId("game1");
		return template;
	}

	@SpaceDataEvent
	public Configuration eventListener(Configuration c) {
		createGrid(c);
		return c;
	}

	private void createGrid(Configuration c) {
		for (int y = 0; y < c.getBlocksY(); y++) {
			for (int x = 0; x < c.getBlocksX(); x++) {
				fillBlock(x, y, c);
			}
		}
	}

	private void fillBlock(int x, int y, Configuration c) {
		int blockCenter = c.getBlockRoxelLength() / 2;
		for (int i = 0; i < c.getBlockRoxelLength(); i++) {
			for (int j = 0; j < c.getBlockRoxelLength(); j++) {
				if (i == blockCenter && j == blockCenter) {
					createRoxel(x, y, c, i, j, "TODECIDE");
				} else if (i == blockCenter) {
					createRoxel(x, y, c, i, j, "EAST");
				} else if (j == blockCenter) {
					createRoxel(x, y, c, i, j, "SOUTH");
				}
			}

		}
	}

	private void createRoxel(int x, int y, Configuration c, int i, int j,
			String string) {
		Roxel tmp = new Roxel((x * c.getBlockRoxelLength()) + j,
				(y * c.getBlockRoxelLength()) + i, string, new String("NOCAR"),
				x, y);
		tuplespace.write(tmp);
	}
}
