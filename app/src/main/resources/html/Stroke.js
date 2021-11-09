
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


        // TODO: Remove. This works around a bug where the first points in
        // uploaded strokes disappear.
        if (this.points.length > 0) {
            result.push(this.points[0].serialize());
        }

        for (const point of this.points) {
            result.push(point.serialize());
        }

        return result.join("\n");
    }

    /// Initialize this' content from the given data.
    deserialize(data) {
        const lines = data.split("\n");
        for (const line of lines) {
            if (line != "") {
                this.points.push(Point.fromSerialized(line));
            }
        }
    }

    /// Render this stroke to the given drawing context.
    /// Applies [transform] to each point before rendering.
    /// [transform: function(Point) -> void]
    render(ctx, transform) {
        let buffer = [];
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

                // Draw a circle at the current point. This makes the
                // stroke look less blocky.
                ctx.beginPath();
                ctx.arc(point.x, point.y, point.size / 2, 0, Math.PI * 2.0);
                ctx.fill();
            }

            lastPoint = point;
        }
    }
}

export default Stroke;
export { Stroke };