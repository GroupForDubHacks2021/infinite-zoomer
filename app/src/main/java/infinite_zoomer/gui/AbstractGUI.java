package infinite_zoomer.gui;

import infinite_zoomer.model.DrawingModel;

import java.util.function.Consumer;

public interface AbstractGUI {
    void render();
    void setModelChangedListener(Consumer<DrawingModel> listener);
}
