package spaces;

import java.util.ArrayList;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class CompressedRoxelGrid {
    String id;
    
    ArrayList<Boolean> occupiedRoxels;
    Integer xSize;
    Integer ySize;

    public CompressedRoxelGrid() {
        this.xSize = 0;
        this.ySize = 0;
        this.occupiedRoxels = new ArrayList<Boolean>(0);
    }

    public CompressedRoxelGrid(String id, int xSize, int ySize) {
        this.id = id;
        this.xSize = xSize;
        this.ySize = ySize;
        this.occupiedRoxels = new ArrayList<Boolean>(xSize * ySize);
        for (int i = 0; i < occupiedRoxels.size(); i++) {
            this.occupiedRoxels.set(i, new Boolean(false));
        }
    }

    public String getId() {
        return id;
    }

    @SpaceId(autoGenerate = false)
    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Boolean> getOccupiedRoxels() {
        return occupiedRoxels;
    }

    public void setOccupiedRoxels(ArrayList<Boolean> occupiedRoxels) {
        this.occupiedRoxels = occupiedRoxels;
    }

    public void setOccupiedRoxels(Integer x, Integer y, Boolean state) {
        this.occupiedRoxels.set(y * xSize + x, state);
    }

    public Integer getxSize() {
        return xSize;
    }

    public void setxSize(Integer xSize) {
        this.xSize = xSize;
    }

    public Integer getySize() {
        return ySize;
    }

    public void setySize(Integer ySize) {
        this.ySize = ySize;
    }

    public String toString() {
        String s = new String();
        for (Boolean b : occupiedRoxels) {
            s += b + "_";
        }
        return id + "_" + s;
    }

    public boolean getOccupiedRoxels(Integer x, Integer y) {
        return occupiedRoxels.get(y * xSize + x);
    }
}
