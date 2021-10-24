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

    public boolean extendsOutside(Circle other) {
        if (other == null) {
            return true;
        }

        // If we're bigger than the other, we definitely extend outside it.
        if (r > other.r) {
            return true;
        }

        //TODO: This math might not be correct.

        double dr = Math.max(r, other.r) - Math.min(r, other.r);
        Circle delta = new Circle(Point2D.midpoint(center, other.center), dr);

        return intersects(other) && !intersects(delta);
    }

    /**
     * @return A circle that contains this and other.
     */
    public Circle unite(Circle other) {
        if (other == null) {
            return this;
        }

        Point2D avgCenter = Point2D.midpoint(other.center, center);
        double fullRadius = this.r + other.r;

        return new Circle(avgCenter, fullRadius);
    }
}
