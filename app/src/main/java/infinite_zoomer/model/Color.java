package infinite_zoomer.model;

public class Color {
    public float r, g, b, a;

    /**
     * Create a color from a four-tuple.
     *
     * All components should range from zero to one.
     *
     * @param r Red value of color: [0.0, 1.0]
     * @param g Green component of color
     * @param b Blue component
     * @param a Transparency (alpha). 1 -> opaque, 0 -> fully transparent
     */
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
