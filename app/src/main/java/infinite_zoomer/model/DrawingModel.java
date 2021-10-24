/**
 * Renders all the shape datas
 * - Performance
 */

package infinite_zoomer.model;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Rectangle;

import java.util.List;


/**
 * Internal representation of the drawing (data on the canvas).
 */
public class DrawingModel {
    private ContainerNode mRoot;

    public DrawingModel() {
        mRoot = new ContainerNode();
    }

    /**
     * Get the container node that should be used for scene object lookup/storage
     * in the given region.
     *
     * @param region Target area.
     * @return ContainerNode that encompasses the area.
     */
    public ContainerNode getContainerForRegion(Circle region) {
        ContainerNode current = mRoot;

        while (region.extendsOutside(current.getBoundingCircle()) && current.getParent() != null) {
            // The current circle has no children; it's the prefect candidate.
            if (current.getBoundingCircle() == null) {
                return current;
            }

            current = current.getParent();
        }

        // We're at the point where a parent circle would be beneficial.
        ContainerNode parent = new ContainerNode();
        parent.addChild(current);
        current = parent;

        return current;
    }

    public ContainerNode getContainerForRegion(Rectangle region) {
        return getContainerForRegion(region.getBoundingCircle());
    }
}
