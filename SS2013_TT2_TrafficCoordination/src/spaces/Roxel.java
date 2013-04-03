package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.metadata.index.SpaceIndexType;

@SpaceClass
public class Roxel {
    private CompoundId id;
    private Integer x;
    private Integer y;
    private String direction;
    private String state;
    private CompoundId tileId;

    public Roxel() {
    }

    public Roxel(Integer x, Integer y, String direction, String state, Integer tileX, Integer tileY) {
        this.setX(x);
        this.setY(y);
        if (direction.equals("SOUTH") || direction.equals("EAST")
                || direction.equals("TODECIDE"))
            this.setDirection(direction);
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;
        tileId = new CompoundId(tileX, tileY);
        id = new CompoundId(x, y);
    }

    @SpaceId(autoGenerate = false)
    public CompoundId getId() {
        return id;
    }
    
    public void setId(CompoundId id) {
        this.id = id;
    }

    @SpaceIndex(type=SpaceIndexType.EXTENDED)
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @SpaceIndex(type=SpaceIndexType.EXTENDED)
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @SpaceIndex(type=SpaceIndexType.BASIC)
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        if (direction.equals("SOUTH") || direction.equals("EAST")
                || direction.equals("TODECIDE"))
            this.direction = direction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;
    }

    @SpaceRouting
    public CompoundId getTileId() {
    	return tileId;
    }
    
    public void setTileId(CompoundId tileId) {
    	this.tileId = tileId;
    }
    
    public String toString() {
        return id + "_" + x + "_" + y + "_" + direction + "_" + state;
    }


}
