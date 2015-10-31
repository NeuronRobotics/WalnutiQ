package model.MARK_II.region;

/**
 * Provides the minimal behaviors for a Cell.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @author Michael Cogswell (cogswell@vt.edu)
 * @version July 22, 2013
 */
public class Cell {
    protected boolean isActive; // biologically equivalent to generating 1 or
    // more spikes
    protected boolean wasActive;

    public Cell() {
        this.isActive = false;
        this.wasActive = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isActive ? 1231 : 1237);
        result = prime * result + (wasActive ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        if (isActive != other.isActive)
            return false;
        if (wasActive != other.wasActive)
            return false;
        return true;
    }

    public boolean getActiveState() {
        return this.isActive;
    }

    public void setActiveState(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getPreviousActiveState() {
        return this.wasActive;
    }

    public void setPreviousActiveState(boolean wasActive) {
        this.wasActive = wasActive;
    }
}
