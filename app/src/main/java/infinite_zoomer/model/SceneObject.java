package infinite_zoomer.model;

import java.util.List;

/**
 * Abstract representation of an object in the scene.
 */

public abstract class SceneObject {
    // TODO: Abstract scene object properties (e.g. getBoundingCircle?, isWithinViewport?)
    abstract boolean isWithin(Rectangle r);
    abstract List<SceneObject> getChildren();
}
