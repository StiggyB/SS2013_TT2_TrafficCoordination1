package game;

import org.openspaces.core.GigaSpace;

import spaces.CompoundId;
import spaces.Configuration;
import spaces.DataGridConnectionUtility;

public class Configurator {
    static GigaSpace tuplespace;

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Connecting to data grid.");
        tuplespace = DataGridConnectionUtility.getSpace("streetGrid", 1, 1);
        
        Configuration c = new Configuration("game1", 10, 8, 4, 3);
        
        System.out.println("Check for existing configuration.");
        Configuration existingConf = tuplespace.readById(Configuration.class, "game1");
        
        if(existingConf == null) {
            System.out.println("No Configuration existing, creating.");
            tuplespace.write(c);
        } else {
            System.out.println("Configuration existing: " + existingConf.toString());
        }
        
        System.exit(0);
    }

}
