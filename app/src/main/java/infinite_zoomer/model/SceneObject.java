package infinite_zoomer.model;

import infinite_zoomer.model.geometry.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract representation of an object in the scene.
 */

public abstract class SceneObject {
    // TODO: Abstract scene object properties (e.g. getBoundingCircle?, isWithinViewport?)
    public abstract boolean isWithin(Circle r);

    /**
     * Get all direct children of this within a given region.
     * @param r Region to check for children in.
     * @return All direct children of this within r.
     */
    public List<SceneObject> getChildrenInRegion(Circle r) {
        return new ArrayList<>();
    }

    /**
     * Gets all drawing elements at the bottom of the tree that are in the given region.
     * @param r Region to query
     * @return A list of all objects that can be rendered in the given region.
     *
     * Should be overridden if this has children or does not render.
     */
    public List<SceneObject> getLeavesInRegion(Circle r) {
        List<SceneObject> result = new ArrayList<>();

        if (isWithin(r)) {
            result.add(this);
        }

        return result;
    }

    public void setParent(SceneObject other) {
        // Override if this functionality is needed
    }

    public abstract Circle getBoundingCircle();
}
