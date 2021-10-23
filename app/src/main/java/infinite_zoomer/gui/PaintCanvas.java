package infinite_zoomer.gui;

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

public class PaintCanvas extends JComponent {
    private Image image;
    private Graphics2D graphics;
    //new x position, new y position, old x position, old y position
    private int n_x, n_y, o_x, o_y;

    public PaintCanvas() {
        this.image = createImage(getWidth(), getHeight());
        this.graphics = (Graphics2D) this.image.getGraphics();
        graphics.setPaint(Color.black);
        
        addMouseListener(new MouseAdapter() {
            public void pressDown(MouseEvent m) {
                o_x = m.getX();
                o_y = m.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void trace(MouseEvent m) {
                n_x = m.getX();
                n_y = m.getY();

                graphics.drawLine(o_x, o_y, n_x, n_y);

                //updates canvas
                repaint();

                o_x = n_x;
                o_y = n_y;
            }
        });
    }
}
