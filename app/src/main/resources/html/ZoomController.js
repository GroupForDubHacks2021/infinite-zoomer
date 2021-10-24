
class ZoomController {
    constructor(initialZoom) {
        this.pointers = {};
        this.initialZoom = initialZoom;
        this.initialCenterDist = -1.0;
    }

    onPointerMove(id, point) {
        this.pointers["p" + id] = point;
    }

    getPointerCount() {
        let res = 0;

        for (const id in this.pointers) {
            res++;
        }

        return res;
    }

    getCenter() {
        let centerPos = undefined;
        let pointerCount = this.getPointerCount();

        // Average the position of all pointers.
        for (const id in this.pointers) {
            const pointer = this.pointers[id];

            if (!centerPos) {
                centerPos = pointer.clone();
            } else {
                centerPos.x += pointer.x;
                centerPos.y += pointer.y;
            }
        }

        centerPos.x /= pointerCount;
        centerPos.y /= pointerCount;

        return centerPos;
    }

    update() {
        let centerPos = this.getCenter();

        let currentCenterDist = 0.0;
        let pointerCount = this.getPointerCount();

        // Get the total distance of pointers to the center.
        for (const id in this.pointers) {
            const pos = this.pointers[id];

            let dx = (pos.x - centerPos.x);
            let dy = (pos.y - centerPos.y);
            currentCenterDist += Math.sqrt(dx * dx + dy * dy);
        }
        currentCenterDist /= pointerCount;

        let zoom = this.initialZoom;
        if (pointerCount < 2) {
            return zoom;
        }

        if (this.initialCenterDist > 0) {
            zoom = this.initialZoom * currentCenterDist / this.initialCenterDist;
        } else {
            this.initialCenterDist = currentCenterDist;
        }

        return zoom;
    }
}

export default ZoomController;
export {ZoomController};