package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass
public class Roxel {
    CompoundId id;
    private Integer x;
    private Integer y;
    private String direction;
    private String state;
    private Boolean stateChanged;

    public Roxel() {
    }

    public Roxel(Integer x, Integer y, String direction, String state) {
        this.setX(x);
        this.setY(y);
        if (direction.equals("SOUTH") || direction.equals("EAST")
                || direction.equals("TODECIDE"))
            this.setDirection(direction);
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;

        this.stateChanged = true;
        id = new CompoundId(x, y);
    }

    @SpaceRouting
    @SpaceId(autoGenerate = false)
    public CompoundId getId() {
        return id;
    }

    public void setId(CompoundId id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

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
        if (state.equals("CAR") || state.equals("NOCAR")) {
            this.stateChanged = true;
            this.state = state;
        }
    }

    public Boolean getStateChanged() {
        return stateChanged;
    }

    public void setStateChanged(Boolean stateChanged) {
        this.stateChanged = stateChanged;
    }

    public String toString() {
        return id + "_" + x + "_" + y + "_" + direction + "_" + state + "_"
                + stateChanged;
    }

}
