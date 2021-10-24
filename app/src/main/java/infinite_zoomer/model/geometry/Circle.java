package infinite_zoomer.model.geometry;

/**
 * Represents a circle.
 * Should be immutable.
 */

public class Circle {
    public final Point2D center;
    public final double r;

    public Circle(Point2D center, double radius) {
        this.center = center;
        this.r = radius;
    }

    public double distanceSquared(Point2D p) {
        return center.distanceSquared(p);
    }

    public boolean intersects(Circle other) {
        double fullR = r + other.r;

        return distanceSquared(other.center) <= fullR * fullR;
    }

    public boolean contains(Point2D p) {
        return distanceSquared(p) <= r * r;
    }

    /**
     * @return A circle that contains this and other.
     */
    public Circle unite(Circle other) {
        if (other == null) {
            return this;
        }

        Point2D avgCenter = new Point2D((other.center.x + this.center.x) / 2, (other.center.y + this.center.y) / 2);
        double fullRadius = this.r + other.r;

        return new Circle(avgCenter, fullRadius);
    }
}
