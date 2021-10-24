package infinite.zoomer;

import infinite_zoomer.App;
import infinite_zoomer.model.ContainerNode;
import infinite_zoomer.model.DrawingModel;
import infinite_zoomer.model.Stroke;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;
import infinite_zoomer.model.geometry.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModelTest {
    @Test public void testDrawingModel() {
        DrawingModel model = new DrawingModel();
        ContainerNode node = model.getContainerForRegion(new Rectangle(0.0, 0.0, 5.0, 5.0));
        assertNotNull(node);

        Stroke stroke = new Stroke("0, 0, 1\n" +
                "2, 3, 4\n" +
                "4, 5, 6\n");
        assertTrue((new Circle(new Point2D(0, 0), 20.0)).intersects(stroke.getBoundingCircle()));
    }

    @Test public void testCircleMath() {
        Circle circle = new Circle(new Point2D(0.0, 0.0), 3.0);
        assertEquals((int) circle.distanceSquared(new Point2D(0.0, 0.0)), 0);
        assertEquals((int) circle.distanceSquared(new Point2D(3.0, 0.0)), 9);
        assertTrue(circle.extendsOutside(new Circle(new Point2D(1.0, 1.0), 0.1)));
        assertFalse(circle.extendsOutside(new Circle(new Point2D(1.0, 1.0), 100)));
        //assertTrue(circle.extendsOutside(new Circle(new Point2D())));
    }
}
