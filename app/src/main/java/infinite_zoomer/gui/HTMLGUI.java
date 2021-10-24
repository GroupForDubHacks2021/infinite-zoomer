package infinite_zoomer.gui;

import infinite_zoomer.model.DrawingModel;

import java.util.function.Consumer;

/***
 * Server-side logic for the HTML GUI.
 */

public class HTMLGUI implements AbstractGUI {
    private DrawingModel mModel;

    public HTMLGUI(DrawingModel model) {
        mModel = model;
    }

    @Override
    public void render() {

    }

    @Override
    public void setModelChangedListener(Consumer<DrawingModel> listener) {

    }

    public String apiRequest(String request, String data) {
        if (request.startsWith("model?")) {
            // TODO: Return model data.
            System.out.println("Model request!");
        }

        System.out.println("API request!");
        System.out.println(request);

        return "You sent for " + request + " with " + data;
    }
}
