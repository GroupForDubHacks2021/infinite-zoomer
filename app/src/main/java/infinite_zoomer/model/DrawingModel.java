/**
 * Renders all the shape datas
 * - Performance
 */

package infinite_zoomer.model;
import infinite_zoomer.gui.AbstractGUI;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * Internal representation of the drawing (data on the canvas).
 */
public class DrawingModel {
    private ContainerNode mRoot;
    private updateListener listener;

    public DrawingModel() {
        mRoot = new ContainerNode();
        listener = new updateListener();
    }

    //updates GUI's when called
    //TODO: resolve if it should be static or not (?)
    public class updateListener {
        private List<AbstractGUI> listeners;

        public updateListener() {
            this.listeners = new ArrayList<AbstractGUI>();
        }

        public void addListener(AbstractGUI listener) {
            this.listeners.add(listener);
        }

        public void removeListener(AbstractGUI listener) {
            this.listeners.remove(listener);
        }

        public void update() {
            for (AbstractGUI listener: listeners) {
                listener.update();
            }
        }
    }

    /**
     * Get the container node that should be used for scene object lookup/storage
     * in the given region.
     *
     * @param region Target area.
     * @return ContainerNode that encompasses the area.
     */
    public ContainerNode getContainerForRegion(Circle region) {
        /*ContainerNode current = mRoot;

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

        return current;*/
        listener.update();
        return mRoot;
    }

    public ContainerNode getContainerForRegion(Rectangle region) {
        listener.update();
        return getContainerForRegion(region.getBoundingCircle());
    }

    public void addObject(SceneObject object) {
        listener.update();
        ContainerNode container = getContainerForRegion(object.getBoundingCircle());
        container.addChild(object);
    }

    public updateListener getListener() {
        return this.listener;
    }
}
