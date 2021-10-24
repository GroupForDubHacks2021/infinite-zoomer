
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

export default Point;
export { Point };