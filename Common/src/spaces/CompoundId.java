package spaces;

import java.io.Serializable;

public class CompoundId implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2694896831015258139L;
    public String key1;
    public String key2;

    public CompoundId(int key1, int key2) {
        this.key1 = String.valueOf(key1);
        this.key2 = String.valueOf(key2);
    }

    public String toString() {
        return key1 + "_" + key2;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
        result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CompoundId other = (CompoundId) obj;
        if (key1 == null) {
            if (other.key1 != null)
                return false;
        } else if (!key1.equals(other.key1))
            return false;
        if (key2 == null) {
            if (other.key2 != null)
                return false;
        } else if (!key2.equals(other.key2))
            return false;
        return true;
    }

}