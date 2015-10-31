package model.MARK_II;

import model.MARK_II.connectTypes.AbstractRegionToRegionConnect;
import model.MARK_II.region.Region;
import model.MARK_II.util.Rectangle;

/**
 * Neocortex is a directed graph of Regions. Each Region in the Neocortex
 * can have as many children Regions as necessary. Each Region can receive
 * input from input SensorCellLayers, a lower Region, or a higher Region.
 *
 * Input to Neocortex: activity of Cells within VisionCellLayer, AudioCellLayer,
 * etc.
 *
 * Output from Neocortex: activity of Cells/Columns within all Regions.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @author Michael Cogswell (cogswell@vt.edu)
 * @author Nathan Waggoner(nwagg14@vt.edu)
 * @version 12/18/2014
 */
public class Neocortex {
    private Region rootRegion;
    private Region currentRegion;
    private AbstractRegionToRegionConnect connectType;

    private int totalNumberOfRegions;

    public Neocortex(Region rootRegion, AbstractRegionToRegionConnect neocortexRegionToNeocortexRegion) {
        if (rootRegion == null) {
            throw new IllegalArgumentException(
                    "rootRegion in Neocortex constructor cannot be null");
        }
        this.rootRegion = rootRegion;
        this.currentRegion = this.rootRegion;

        if (neocortexRegionToNeocortexRegion == null) {
            throw new IllegalArgumentException(
                    "connectType in class Neocortex constructor cannot be null");
        }
        this.connectType = neocortexRegionToNeocortexRegion;
    }

    /**
     * Traverses the region within this neocortex to find the region the desired biological name
     * and changes the currentRegion to point to this region.
     *
     * @param newCurrentRegionBiologicalName name of Region in Neocortex
     */
    public void changeCurrentRegionTo(String newCurrentRegionBiologicalName) {
        if (newCurrentRegionBiologicalName == null) {
            throw new IllegalArgumentException(
                    "newCurrentRegionBiologicalName in class Neocortex method changeCurrentRegionTo() cannot be null");
        }
        Region newCurrentRegion = this.getRegion(newCurrentRegionBiologicalName);
        if (newCurrentRegion == null) {
            throw new IllegalArgumentException("newCurrentRegionBiologicalName = " + newCurrentRegionBiologicalName
                    + " in class Neocortex method changeCurrentRegionTo() does not exist in the Neocortex");
        }
        this.currentRegion = newCurrentRegion;
    }

    public Region getRegion(String regionBiologicalName) {
        if (regionBiologicalName == null) {
            throw new IllegalArgumentException(
                    "newCurrentRegionBiologicalName in class Neocortex method getRegion() cannot be null");
        }

        // search the neocortex for region
        return this.recursiveFind(regionBiologicalName, this.rootRegion, 0);
    }

    Region recursiveFind(String regionBiologicalName, Region current, int numberOfRegionsVisited) {
        if (numberOfRegionsVisited > this.totalNumberOfRegions) {
            return null;
        }
        else if(current.getBiologicalName().equals(regionBiologicalName)) {
            return current;
        }
        else{
            for(Region child : current.getChildRegions()){
                Region possible = recursiveFind(regionBiologicalName, child, numberOfRegionsVisited + 1);
                if(possible != null){
                    return possible;
                }
            }
        }
        return null;
    }

    public void addToCurrentRegion(Rectangle rectanglePartOfParentRegionToConnectTo, Region childRegion,
                                      int numberOfColumnsToOverlapAlongNumberOfRows,
                                      int numberOfColumnsToOverlapAlongNumberOfColumns) {
        if (childRegion == null) {
            throw new IllegalArgumentException(
                    "childRegion in class Neocortex method addToCurrentRegion cannot be null");
        }

        Region regionAlreadyInNeocortex = this.getRegion(childRegion.getBiologicalName());

        // NOTE: The first if and else if statement ARE necessary and
        //       it is important to understand why nothing should be done
        if (regionAlreadyInNeocortex == null) {
            // childRegion is new so we can add given childRegion to current
            // region. Note this is not an error.
        } else if (regionAlreadyInNeocortex.equals(childRegion)) {
            // the user is trying to make a cycle connection within region in
            // the Neocortex which is allowed
        } else if (regionAlreadyInNeocortex != null) {
            throw new IllegalArgumentException(
                    "childRegion in class Neocortex method addToCurrentRegion" +
                            " already exists within the Neocortex as another region " +
                            "with the same name");
        }

        this.currentRegion.addChildRegion(childRegion);
        this.totalNumberOfRegions++;
        // connect currentRegion to childRegion
        this.connectType.connect(childRegion.getColumns(),
                this.currentRegion.getColumns(rectanglePartOfParentRegionToConnectTo),
                numberOfColumnsToOverlapAlongNumberOfRows,
                numberOfColumnsToOverlapAlongNumberOfColumns);
    }

    public Region getCurrentRegion() {
        return this.currentRegion;
    }
}
