/**
 * Renders all the shape datas
 * - Performance
 */

package infinite_zoomer.model;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;
import infinite_zoomer.model.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * Internal representation of the drawing (data on the canvas).
 */
public class DrawingModel {
    private BoundingCircle mRoot;

    public DrawingModel() {
        mRoot = new BoundingCircle();
    }

    public List<SceneObject> getObjectsInRegion(Rectangle region) {
        // Circles are easier to do collisions, etc. with:
        return getObjectsInRegion(region.getBoundingCircle());
    }

    public List<SceneObject> getObjectsInRegion(Circle region) {
        return mRoot.getLeavesInRegion(region);
    }
}
