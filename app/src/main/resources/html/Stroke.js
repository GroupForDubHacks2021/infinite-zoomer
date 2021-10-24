
import { Point } from "./Point.js";

class Stroke {
    constructor(data) {
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
    }
}

export default Stroke;
export { Stroke };