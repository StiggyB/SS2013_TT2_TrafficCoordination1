package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

//@SpaceClass
public class Configuration {
    String id;
    Float roxelLength;
    Integer blocksX;
    Integer blocksY;
    Integer blockRoxelLength;
    Boolean processed;

    public Configuration() {
    }

    public Configuration(String id, float roxelLength, int blocksX,
            int blocksY, int blockRoxelLength, boolean processed) {
        this.roxelLength = roxelLength;
        this.blocksX = blocksX;
        this.blocksY = blocksY;
        this.blockRoxelLength = blockRoxelLength;
        this.id = id;
        this.processed = processed;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }
    @SpaceRouting
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getRoxelLength() {
        return roxelLength;
    }

    public void setRoxelLength(Float roxelLength) {
        this.roxelLength = roxelLength;
    }

    public Integer getBlocksX() {
        return blocksX;
    }

    public void setBlocksX(Integer blocksX) {
        this.blocksX = blocksX;
    }

    public Integer getBlocksY() {
        return blocksY;
    }

    public void setBlocksY(Integer blocksY) {
        this.blocksY = blocksY;
    }

    public Integer getBlockRoxelLength() {
        return blockRoxelLength;
    }

    public void setBlockRoxelLength(Integer blockRoxelLength) {
        this.blockRoxelLength = blockRoxelLength;
    }

    public String toString() {
        return id + "_" + roxelLength + "_" + blocksX + "_" + blocksY + "_"
                + blockRoxelLength + "_"+processed;
    }

}
