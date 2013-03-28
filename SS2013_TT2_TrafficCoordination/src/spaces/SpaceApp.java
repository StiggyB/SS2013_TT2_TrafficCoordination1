package spaces;

import org.openspaces.core.GigaSpace;

public class SpaceApp {


    public static void main(String[] args) {
        GigaSpace space = DataGridConnectionUtility.getSpace("myGrid");
        System.out.println("ss");
        /**
         * XXX muss SpaceObject sein
         */
        Object o = new Object();
        space.write(o);
        Object o1 = space.read(o);
        System.out.println(o.equals(o1));
    }

    public SpaceApp() {
        // TODO Auto-generated constructor stub
    }

}
