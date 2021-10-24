package infinite_zoomer.model.geometry;

public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceSquared(Point2D other) {
        double dx = other.x - x;
        double dy = other.y - y;

        return dx * dx + dy * dy;
    }

    public static Point2D midpoint(Point2D a, Point2D b) {
        return new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2);
    }
}
