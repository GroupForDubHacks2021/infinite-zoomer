/**
 * Controls mouse movement
 */
package infinite_zoomer.gui;

import infinite_zoomer.model.ContainerNode;
import infinite_zoomer.model.DrawingModel;

import javax.swing.JFrame;
import java.awt.event.MouseListener;  // Detects mouse actions performed.
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;     // Indicates which actions to perform.
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

public class SwingGUI implements AbstractGUI {
    private JFrame frame;
    private DrawingModel mModel;
    private PaintCanvas canvas;

    public SwingGUI(DrawingModel mModel) {
        this.frame = new JFrame("Canvas");
        this.mModel = mModel;
        this.canvas = new PaintCanvas(mModel);
    }

    public void initialize() {
        //set options
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(canvas);
        frame.setVisible(true);
    }

    @Override
    public void render() {

    }

    @Override
    public void setModelChangedListener(Consumer<DrawingModel> listener) {

    }

    //we need to detect when the mModel is updated
    public void update() {
        //TODO: for now, just return the current screen - implement zooming later
        canvas.update();
    }
}
