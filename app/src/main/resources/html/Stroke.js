
import { Point } from "./Point.js";

class Stroke {
    constructor(data) {
        this.points = [];
        if (typeof data == "string") {
            // If constructed with just a string, we're
            // deserializing the stroke.
            this.deserialize(data);
        }
    }

    addPoint(point) {
        this.points.push(point);
    }

    /// Convert this' data into a network-transferable format
    /// For example,
    /// 0.0, 0.0, 0.4
    /// 2.0, 3.0, 0.4
    /// 6.0, 9.0, 0.4
    /// 12.0, 33.0, 0.4
    /// ...where the pressure of each point in the example
    /// is 0.4.
    serialize() {
        let result = [];

        for (const point of this.points) {
            result.push(point.serialize());
        }

        return result.join("\n");
    }

    /// Initialize this' content from the given data.
    deserialize(data) {
        const lines = data.split("\n");
        for (const line of lines) {
            this.points.push(Point.fromSerialized(line));
        }
    }

    /// Render this stroke to the given drawing context.
    /// Applies [transform] to each point before rendering.
    /// [transform: function(Point) -> void]
    render(ctx, transform) {
        let lastPoint = null;
        for (let point of this.points) {
            point = point.clone();
            transform(point);

            if (lastPoint) {
                ctx.beginPath();
                ctx.lineWidth = point.size;
                ctx.moveTo(lastPoint.x, lastPoint.y);
                ctx.lineTo(point.x, point.y);
                ctx.stroke();
            }

            lastPoint = point;
        }
    }
}

export default Stroke;
export { Stroke };