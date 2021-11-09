package infinite_zoomer.gui;

import infinite_zoomer.model.*;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;
import infinite_zoomer.model.geometry.Rectangle;

import javax.swing.*;

//copy-pasted imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class PaintCanvas extends JComponent {
    private DrawingModel mModel;
    private Image image;
    private Graphics2D graphics;
    //new x position, new y position, old x position, old y position
    private int n_x, n_y, o_x, o_y;
    private Point2D center;
    private List<Line> currSerializedStroke;

    public PaintCanvas(DrawingModel mModel) {
        this.mModel = mModel;
        this.center = new Point2D(300, 300);
        this.currSerializedStroke = new ArrayList<>();

        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent m) {
                o_x = m.getX();
                o_y = m.getY();

                System.out.println("click detected!");
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent m) {
                n_x = m.getX();
                n_y = m.getY();

                graphics.drawLine(o_x, o_y, n_x, n_y);
                currSerializedStroke.add(new Line(new Point2D(o_x, o_y), new Point2D(n_x, n_y), new infinite_zoomer.model.Color(0.0f, 0.0f, 0.0f, 1.0f), 0.1));

                //updates canvas
                repaint();

                o_x = n_x;
                o_y = n_y;

                System.out.println("trace detected!");
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent m) {
                mModel.addObject(new Stroke(currSerializedStroke));
                mModel.getListener().update();
                //TODO: need to clear line, but have to WAIT for update
//                currSerializedStroke.clear();
            }
        });
    }



    //best I can understand is that this gets called when the frame adds this component.
    protected void paintComponent(Graphics g) {
        if (image == null) {
            // image to draw null ==> we create
            image = createImage(getSize().width, getSize().height);
            graphics = (Graphics2D) image.getGraphics();
            // enable antialiasing
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        graphics.setPaint(Color.white);
        // draw white on entire draw area to clear
        graphics.fillRect(0, 0, getSize().width, getSize().height);
        graphics.setPaint(Color.black);
        repaint();
    }

//    public void jsUpdate(ContainerNode container) {
//        List<Stroke> objects = new ArrayList<Stroke>();
//        for (SceneObject object: container) {
//
//        }
//    }

    public void update() {
        Circle boundingCircle = new Circle(center, Math.sqrt(2 * 600));
        ContainerNode container = mModel.getContainerForRegion(boundingCircle);
        List<SceneObject> strokes = container.getLeavesInRegion(boundingCircle);
        for (SceneObject stroke: strokes) {
            if (stroke instanceof Stroke) {
                for (Line line: ((Stroke) stroke).getmLines()) {
                    //TODO: resolve cast to int?
                    graphics.drawLine((int) line.start.x, (int) line.start.y, (int) line.end.x, (int) line.end.y);
                }
            }
        }
    }
}
