package infinite_zoomer.model.geometry;

public class Circle {
    public Point2D center;
    public double r;

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
}
