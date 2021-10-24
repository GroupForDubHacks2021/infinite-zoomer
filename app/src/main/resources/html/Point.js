
import { assert_eq } from "./testUtils.js";

/**
 * Represents a point with pressure (and perhaps color!)
 */
class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.pressure = 0.5;
    }

    serialize() {
        return `{this.x}, {this.y}, {this.pressure}`;
    }
}

/// Create a point from a string. For example,
/// Point.fromSerialized("1, 2, 3") creates a point
/// at (1, 2), with pressure 3.
Point.fromSerialized = (data) => {
    let parts = data.split(", ");
    if (parts.length !== 3) {
        throw new Error(`parts ({parts}) is not of length 3!!!`);
    }

    let result = new Point();
    result.x = parseFloat(parts[0]);
    result.y = parseFloat(parts[1]);
    result.pressure = parseFloat(parts[2]);

    return result;
};

{
    let testPoint = Point.fromSerialized("1, 2, 3.3");
    assert_eq(testPoint.x, 1);
    assert_eq(testPoint.y, 2);
    assert_eq(testPoint.pressure, 3.3);
}

export default Point;
export { Point };