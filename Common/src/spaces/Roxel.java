package spaces;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Roxel {
    CompoundId id;
    private Integer x;
    private Integer y;
    private String direction;
    private String state;

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

        id = new CompoundId(x, y);
    }

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
        if (state.equals("CAR") || state.equals("NOCAR"))
            this.state = state;
    }

    public String toString() {
        return id + "_" + x + "_" + y + "_" + direction + "_" + state;
    }

}
