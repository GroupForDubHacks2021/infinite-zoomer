/**
 * Controls mouse movement
 */
package infinite_zoomer.gui;

import javax.swing.JFrame;
import java.awt.event.MouseListener;  // Detects mouse actions performed.
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;     // Indicates which actions to perform.
import java.awt.event.MouseMotionListener;

public class SwingGUI {
    private JFrame frame;

    public SwingGUI() {
        this.frame = new JFrame("Canvas");
    }

    public void initialize() {
        PaintCanvas canvas = new PaintCanvas();

        //not entirely sure if this is necessary
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set options
        frame.setSize(300, 500);
        frame.add(canvas);
        frame.setVisible(true);
    }
}
