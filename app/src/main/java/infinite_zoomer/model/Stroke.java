package infinite_zoomer.model;

import infinite_zoomer.model.geometry.Circle;

import java.util.List;

/**
 * Represents a stroke with pressure.
 */

public class Stroke extends SceneObject {
    private List<Line> mLines;

    /**
     * Create an empty stroke.
     */
    public Stroke() {

    }

    /**
     * Create a stroke from serialized text data.
     * @param serializedData Serialized data. Resembles .csv file data. Each line should
     *                         contain data for one point.
     */
    public Stroke(String serializedData) {

    }

    @Override
    boolean isWithin(Circle r) {
        return false;
    }

    @Override
    List<SceneObject> getChildrenInRegion(Circle r) {
        return null;
    }
}
