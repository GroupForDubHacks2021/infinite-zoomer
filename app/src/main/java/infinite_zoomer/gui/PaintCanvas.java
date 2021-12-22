package infinite_zoomer.gui;

import infinite_zoomer.model.*;
import infinite_zoomer.model.Color;
import infinite_zoomer.model.Stroke;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;
import infinite_zoomer.model.geometry.Rectangle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//copy-pasted imports
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class PaintCanvas extends JPanel {
    private DrawingModel mModel;
    private Image image;
    private Graphics2D graphics;
    //new x position, new y position, old x position, old y position
    private int n_x, n_y, o_x, o_y;
    private Point2D center;
    private List<Line> currSerializedStroke;
    private int thickness;

    public PaintCanvas(DrawingModel mModel) {
        this.mModel = mModel;
        this.center = new Point2D(800, 500);
        this.currSerializedStroke = new ArrayList<>();
        this.thickness = 1;

        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent m) {
                o_x = m.getX();
                o_y = m.getY();

                System.out.println("click detected!");
            }
        });

        //TODO: design own drawLine command? Swing's Drawing is far too blocky.
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent m) {
                n_x = m.getX();
                n_y = m.getY();

//                graphics.drawLine(o_x, o_y, n_x, n_y);
                //draws a circle at every "joint" to make the lines less blocky
                graphics.fillArc(n_x - thickness/2,  n_y - thickness/2, (int) (thickness/Math.sqrt(2)), (int) (thickness/Math.sqrt(2)), 0, 360);
                graphics.drawLine(o_x, o_y, n_x, n_y);
                currSerializedStroke.add(new Line(new Point2D(o_x, o_y), new Point2D(n_x, n_y), new infinite_zoomer.model.Color(0.0f, 0.0f, 0.0f, 1.0f), thickness));

                //updates canvas
                repaint();

                o_x = n_x;
                o_y = n_y;
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent m) {
                if (!currSerializedStroke.isEmpty()) {
                    mModel.addObject(new Stroke(currSerializedStroke));
                    mModel.getListener().update();
                    currSerializedStroke.clear();
                }
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

    public void setThickness(int thickness) {
        this.thickness = thickness;
        graphics.setStroke(new BasicStroke(thickness));
    }

    public void clear() {
        graphics.setPaint(java.awt.Color.white);
        // draw white on entire draw area to clear
        graphics.fillRect(0, 0, getSize().width, getSize().height);
        graphics.setPaint(java.awt.Color.black);
        repaint();
    }

    public void update() {
        Circle boundingCircle = new Circle(center, Math.sqrt(Math.pow(800, 2) + Math.pow(500, 2)));
        ContainerNode container = mModel.getContainerForRegion(boundingCircle);
        List<SceneObject> strokes = container.getLeavesInRegion(boundingCircle);
        java.awt.Stroke oStroke = graphics.getStroke();
        for (SceneObject stroke: strokes) {
            if (stroke instanceof Stroke) {
                for (Line line: ((Stroke) stroke).getLines()) {
                    //TODO: resolve cast to int?
                    graphics.setStroke(new BasicStroke((float) line.thickness));
                    graphics.drawLine((int) line.start.x, (int) line.start.y, (int) line.end.x, (int) line.end.y);
                }
            }
        }
        graphics.setStroke(oStroke);
        repaint();
    }
}
