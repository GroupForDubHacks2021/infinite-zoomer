package infinite_zoomer.gui;

import infinite_zoomer.model.ContainerNode;
import infinite_zoomer.model.DrawingModel;
import infinite_zoomer.model.SceneObject;
import infinite_zoomer.model.Stroke;
import infinite_zoomer.model.geometry.Circle;
import infinite_zoomer.model.geometry.Point2D;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Server-side logic for the HTML GUI.
 */

public class HTMLGUI implements AbstractGUI {
    private static final Pattern MODEL_REQUEST_PATTERN = Pattern.compile("^refresh:([-]?\\d+\\.?\\d*),([-]?\\d+\\.?\\d*),([-]?\\d+\\.?\\d*)");

    private final DrawingModel mModel;

    public HTMLGUI(DrawingModel model) {
        mModel = model;
    }

    @Override
    public void render() {

    }

    @Override
    public void setModelChangedListener(Consumer<DrawingModel> listener) {

    }

    @Override
    public void update() {

    }

    /**
     * Converts the region of this' model for a specific request
     * to a machine-readable string.
     *
     * @param request Description of the region, etc to render
     * @return String representation of data
     */
    private String serializeModel(String request) {
        Matcher matcher = MODEL_REQUEST_PATTERN.matcher(request);
        boolean found = matcher.find();
        assert(found);

        double radius = Double.parseDouble(matcher.group(1));
        double x = Double.parseDouble(matcher.group(2));
        double y = Double.parseDouble(matcher.group(3));

        Circle c = new Circle(new Point2D(x, y), radius);
        StringBuilder result = new StringBuilder();

        // TODO: Get points.
        ContainerNode container = mModel.getContainerForRegion(c);



        for (SceneObject s : container.getLeavesInRegion(c)) {
            // TODO: Handle non-strokes
            if (s instanceof Stroke stroke) {
                result.append(stroke.serialize()).append(";;");
            }
        }

        return result.toString();
    }

    public String apiRequest(String request, String data) {
        if (request.startsWith("refresh:")) {
            return serializeModel(request);
        }
        else if (request.startsWith("addstroke")) {
            Stroke stroke = new Stroke(data);
            mModel.addObject(stroke);
            return "OKAY!";
        }

        System.err.printf("Warning: Received unknown API request: %s with data %s%n", request, data);
        return "UNKNOWN REQUEST: " + request + ", WITH DATA: " + data;
    }
}
