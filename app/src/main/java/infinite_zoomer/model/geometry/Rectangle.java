package infinite_zoomer.model.geometry;

public class Rectangle {
    public double x, y, w, h;

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Point2D getCenter() {
        return new Point2D(this.x + this.w / 2, this.y + this.h / 2);
    }

    public Circle getBoundingCircle() {
        Point2D center = this.getCenter();
        double maxDim = Math.max(this.w, this.h) / 2;

        return new Circle(center, maxDim);
    }
}
