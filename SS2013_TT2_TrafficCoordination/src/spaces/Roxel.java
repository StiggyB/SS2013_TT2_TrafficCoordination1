package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Roxel {
    CompoundId id;
    private String direction;
    private String state;

    public void setId(CompoundId id) {
        this.id = id;
    }

    public Roxel() {
    }

    public Roxel(int x, int y, String direction, String state) {
        if (direction.equals("SOUTH") || direction.equals("EAST") || direction.equals("TODECIDE"))
            this.setDirection(direction);
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;
        
        id = new CompoundId(x, y);
    }

    @SpaceId(autoGenerate = false)
    public CompoundId getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        if (direction.equals("SOUTH") || direction.equals("EAST") || direction.equals("TODECIDE"))
            this.direction = direction;
    }

    public String toString() {
        return id + "_" + direction + "_" + state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;
    }
}
