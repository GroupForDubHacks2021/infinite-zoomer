
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

    private final Circle mBoundingCircle;

    public Line(Point2D start, Point2D end, Color color, double thickness) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.thickness = thickness;

        Point2D center = new Point2D((this.start.x + this.end.x) / 2, (this.start.y + this.end.y) / 2);
        double radius = Math.sqrt(this.start.distanceSquared(this.end));
        mBoundingCircle = new Circle(center, radius);
    }

    @Override
    public boolean isWithin(Circle r) {
        // TODO: Use a better method of checking this.
        return mBoundingCircle.intersects(r);
    }

    @Override
    public Circle getBoundingCircle() {
        return mBoundingCircle;
    }

    @Override
    protected void updateBoundingCircle() {
    }
}
