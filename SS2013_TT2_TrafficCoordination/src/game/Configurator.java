package game;

import org.openspaces.core.GigaSpace;

import spaces.Configuration;
import spaces.ConfigurationListener;
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

		ConfigurationListener configListener = new ConfigurationListener(tuplespace);
		
		Configuration c = new Configuration("game1", 10, 8, 4, 3);

		System.out.println("Check for existing configuration.");
		Configuration existingConf = tuplespace.readById(Configuration.class,
				"game1");

		if (existingConf == null) {
			System.out.println("No Configuration existing, creating.");
			tuplespace.write(c);
		} else {
			System.out.println("Configuration existing: "
					+ existingConf.toString());
		}
//			createGrid(c);

		System.exit(0);
	}

//	private static void createGrid(Configuration c) {
//		for (int y = 0; y < c.getBlocksY(); y++) {
//			for (int x = 0; x < c.getBlocksX(); x++) {
//				fillBlock(x, y, c);
//			}
//		}
//	}

//	private static void fillBlock(int x, int y, Configuration c) {
//		int blockCenter = c.getBlockRoxelLength() / 2;
//		for (int i = 0; i < c.getBlockRoxelLength(); i++) {
//			for (int j = 0; j < c.getBlockRoxelLength(); j++) {
//				if (i == blockCenter && j == blockCenter) {
//					createRoxel(x, y, c, i, j, "TODECIDE");
//				} else if (i == blockCenter) {
//					createRoxel(x, y, c, i, j, "EAST");
//				} else if (j == blockCenter) {
//					createRoxel(x, y, c, i, j, "SOUTH");
//				}
//			}
//
//		}
//	}
//
//	private static void createRoxel(int x, int y, Configuration c, int i,
//			int j, String string) {
//		Roxel tmp = new Roxel((x * c.getBlockRoxelLength()) + j,
//				(y * c.getBlockRoxelLength()) + i, string, new String("NOCAR"),
//				x, y);
//		tuplespace.write(tmp);
//	}
}
