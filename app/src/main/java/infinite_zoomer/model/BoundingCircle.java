package infinite_zoomer.model;

import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for child elements. Intended to allow better "infinity" zooming
 * by allowing zoom to be relative to a bounding circle (that may have a parent circle, etc.)
 */

public class BoundingCircle extends SceneObject {
    private List<SceneObject> mChildren;
    private BoundingCircle mParent = null;
    private Circle region;

    public BoundingCircle() {
        mChildren = new ArrayList<>();
    }

    public void addChild(SceneObject child) {
        child.setParent(this);
        mChildren.add(child);
    }

    @Override
    public List<SceneObject> getChildrenInRegion(Circle region) {
        List<SceneObject> result = new ArrayList<>();

        for (SceneObject child : this.mChildren) {
            if (child.isWithin(region)) {
                result.add(child);
            }
        }

        return result;
    }

    @Override
    List<SceneObject> getLeavesInRegion(Circle r) {
        List<SceneObject> result = new ArrayList<>();
        List<SceneObject> applicableChildren = getChildrenInRegion(r);
        
        for (SceneObject child : applicableChildren) {
            result.addAll(child.getLeavesInRegion(r));
        }
        
        return result;
    }

    @Override
    boolean isWithin(Circle c) {
        return c.intersects(region);
    }
}
