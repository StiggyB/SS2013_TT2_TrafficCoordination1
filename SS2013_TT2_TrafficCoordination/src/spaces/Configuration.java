package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Configuration {
    String id;
    float roxelLength;
    int blocksX;
    int blocksY;
    int blockRoxelLength;

    public Configuration() {
    }

    public Configuration(String id, float roxelLength, int blocksX, int blocksY,
            int blockRoxelLength) {
        this.roxelLength = roxelLength;
        this.blocksX = blocksX;
        this.blocksY = blocksY;
        this.blockRoxelLength = blockRoxelLength;
        this.id = id;
    }

    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRoxelLength() {
        return roxelLength;
    }

    public void setRoxelLength(float roxelLength) {
        this.roxelLength = roxelLength;
    }

    public int getBlocksX() {
        return blocksX;
    }

    public void setBlocksX(int blocksX) {
        this.blocksX = blocksX;
    }

    public int getBlocksY() {
        return blocksY;
    }

    public void setBlocksY(int blocksY) {
        this.blocksY = blocksY;
    }

    public int getBlockRoxelLength() {
        return blockRoxelLength;
    }

    public void setBlockRoxelLength(int blockRoxelLength) {
        this.blockRoxelLength = blockRoxelLength;
    }

    public String toString() {
        return id + "_" + roxelLength + "_" + blocksX + "_" + blocksY + "_"
                + blockRoxelLength;
    }
}
