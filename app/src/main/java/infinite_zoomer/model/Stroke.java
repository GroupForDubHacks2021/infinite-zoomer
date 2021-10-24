package infinite_zoomer.model;

import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stroke with pressure.
 */

public class Stroke extends SceneObject {
    private static final Color DEFAULT_COLOR = new Color(0.0f, 0.0f, 0.0f, 1.0f);

    private final List<Line> mLines;
    private Color mColor;
    private Circle mBoundingCircle;

    /**
     * Create an empty stroke.
     */
    public Stroke() {
        mLines = new ArrayList<>();
        mColor = DEFAULT_COLOR;

        updateBoundingCircle();
    }

    /**
     * Create a stroke from serialized text data.
     * @param serializedData Serialized data. Resembles .csv file data. Each line should
     *                         contain data for one point.
     */
    public Stroke(String serializedData) {
        this();

        Point2D lastPoint = null;

        for (String pointData : serializedData.split("\n")) {
            String[] parts = pointData.split(",\\S+");

            // Should have x, y, pressure
            assert(parts.length == 3);

            double x = Float.parseFloat(parts[0]);
            double y = Float.parseFloat(parts[1]);
            double thickness = Float.parseFloat(parts[2]);

            Point2D point = new Point2D(x, y);

            if (lastPoint != null) {
                Point2D start = point;
                Point2D end = lastPoint;

                mLines.add(new Line(start, end, mColor, thickness));
            }

            lastPoint = point;
        }

        updateBoundingCircle();
    }

    @Override
    public boolean isWithin(Circle r) {
        return r.intersects(mBoundingCircle);
    }

    @Override
    public List<SceneObject> getChildrenInRegion(Circle r) {
        return null;
    }

    @Override
    public Circle getBoundingCircle() {
        return mBoundingCircle;
    }

    @Override
    protected void updateBoundingCircle() {
        mBoundingCircle = null;

        for (Line l : mLines) {
            if (mBoundingCircle == null) {
                mBoundingCircle = l.getBoundingCircle();
            } else {
                mBoundingCircle = mBoundingCircle.unite(l.getBoundingCircle());
            }
        }

        if (mBoundingCircle == null) {
            mBoundingCircle = new Circle(new Point2D(0, 0), 0.0);
        }
    }
}
