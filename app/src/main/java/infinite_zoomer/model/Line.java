
package infinite_zoomer.model;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;

/**
 * File for representing a line.
 */

public class  Line extends SceneObject {
    public final Point2D start, end;
    public final Color color;
    public final double thickness;

    public Line(Point2D start, Point2D end, Color color, double thickness) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    boolean isWithin(Circle r) {
        if (r.contains(start) || r.contains(end)) {
            return true;
        }

        // TODO Check interior points of line.

        return false;
    }
}
