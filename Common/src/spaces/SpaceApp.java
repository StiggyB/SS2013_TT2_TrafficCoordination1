package spaces;

import org.openspaces.core.GigaSpace;

public class SpaceApp {

    public static void main(String[] args) {
        GigaSpace space = DataGridConnectionUtility.getSpace("myGrid", 1, 1);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Roxel o = new Roxel(j, i, new String("SOUTH"), new String(
                        "NOCAR"));
                space.write(o);
            }
        }

        // -----
        CompoundId id = new CompoundId(0, 0);
        Roxel o1 = space.readById(Roxel.class, id);
        System.out.println(o1.toString());
        id = new CompoundId(0, 1);
        o1 = space.readById(Roxel.class, id);
        System.out.println(o1.toString());

        return;
    }

    public SpaceApp() {
        // TODO Auto-generated constructor stub
    }

}
