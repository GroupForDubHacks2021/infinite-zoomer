"use strict";

import { Stroke } from "./Stroke.js";
import { Point } from "./Point.js";
import { ZoomController } from "./ZoomController.js";
import { Scene } from "./Scene.js";

const WHEEL_DELTA_MODE_LINE = 1;
const WHEEL_DELTA_MODE_PAGE = 2;


/// Get the scene for the current zoom (if it's changed).
function getStrokes(viewportPosition, zoom) {

}

async function main()
{
    const canvas = document.querySelector("#mainCanvas");
    const ctx = canvas.getContext("2d");
    let stroke = null;

    let sceneContent;
    let penSize = 1;

    // Render everything!
    const render = () => {
        // Re-size the drawing context (if the canvas size changed)
        // If we don't do this, everything will look stretched!
        if (canvas.clientWidth !== canvas.width || canvas.clientHeight !== canvas.height) {
            canvas.width = canvas.clientWidth;
            canvas.height = canvas.clientHeight;

            sceneContent.updateViewportSize(canvas.width, canvas.height);
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        const zoom = sceneContent.getZoom();
        const sceneTranslation = sceneContent.sceneTranslation;

        let transform = (point) => {
            // TODO: Transform point based on current zoom, position, etc.

            point.x += sceneTranslation.x;
            point.y += sceneTranslation.y;
            point.x *= zoom;
            point.y *= zoom;
            point.size *= zoom;

            // E.g.
            // point.x *= 2;
            // point.x -= 500;
            // While we could use ctx.scale or ctx.transform, that won't work
            // if we want to be able to zoom in as far as we want.
        };

        // Render all elements!
        for (const elem of sceneContent.getElems()) {
            elem.render(ctx, transform);
        }

        // Render the current stroke, too
        if (stroke) {
            stroke.render(ctx, transform);
        }
    };

    const eventToUnzoomedPoint = (ev) => {
        // Get location of the target element (our canvas)
        const bbox = canvas.getBoundingClientRect();

        // x is in page coordinates, so we need to subtract the
        // canvas' distance from the left of the page
        const x = ev.clientX - bbox.left;
        const y = ev.clientY - bbox.top;

        return new Point(x, y, (ev.pressure || 0.5));
    };

    /// Given a PointerEvent, convert it to a point in the zoomed canvas
    const eventToPoint = (ev) => {
        let result = eventToUnzoomedPoint(ev);
        // Get location of the target element (our canvas)
        const bbox = canvas.getBoundingClientRect();
        const zoom = sceneContent.getZoom();
        const sceneTranslation = sceneContent.sceneTranslation;

        // x is in page coordinates, so we need to subtract the
        // canvas' distance from the left of the page
        result.x = result.x/zoom - sceneTranslation.x;
        result.y = result.y/zoom - sceneTranslation.y;
        result.size /= zoom;

        return result;
    };

    sceneContent = new Scene(render);

    size_slider.oninput = function() {
        penSize = size_slider.value;
    };
    size_slider.value = penSize;

    canvas.addEventListener("wheel", (ev) => {
        ev.preventDefault();

        let delta = -ev.deltaY;

        if (ev.deltaMode == WHEEL_DELTA_MODE_LINE) {
            delta *= 3;
        } else if (ev.deltaMode == WHEEL_DELTA_MODE_PAGE) {
            delta *= 6;
        } else {
            delta /= 100;
        }

        if (delta == 0) {
            return;
        }

        console.log(delta);

        if (delta < 0) {
            delta = Math.max(1/(1 + Math.abs(delta)), 0.7);
        } else if (delta < 1) {
            delta += 1;
        }

        sceneContent.zoomTo(sceneContent.getZoom() * delta, eventToUnzoomedPoint(ev));
        render();
    });

    // For multi-touch
    let zoomGesture = new ZoomController();

    let pointerDownCount = 0;

    // Documentation: https://developer.mozilla.org/en-US/docs/Web/API/GlobalEventHandlers/onpointerdown
    canvas.addEventListener("pointerdown", (ev) => {
        // Do stuff with [ev] here.
        // I think ev has a property called something like "primary".
        // If ev.primary is false, then it's a secondary pointer (like the second
        // finger in a two-finger pinch-zoom gesture).
        //
        // If it's a secondary pointer, we probably want to switch from drawing to
        // zooming.
        ev.preventDefault();

        const shouldMousePan = ev.pointerType == "mouse" && ev.button == 2;

        if (pointerDownCount == 0 && !shouldMousePan) {
            stroke = new Stroke();
            stroke.addPoint(eventToPoint(ev));

            canvas.setPointerCapture(ev.pointerId);
        } else {
            stroke = null;
            zoomGesture = new ZoomController(sceneContent.getZoom());
            zoomGesture.onPointerMove(ev.pointerId, eventToUnzoomedPoint(ev));
        }

        pointerDownCount ++;
        console.log(pointerDownCount);
        render();

        return true;
    }, false);

    canvas.addEventListener("pointermove", (ev) => {
        if (pointerDownCount == 0) {
            return;
        }

        ev.preventDefault();
        let pointerLocation = eventToPoint(ev);

        if (stroke != null) {
            // We're drawing a stroke!
            pointerLocation.size *= penSize;
            stroke.addPoint(pointerLocation);
        } else if (zoomGesture) {
            // We're zooming!
            let oldZoomCenter = zoomGesture.getCenter();
            let pointerCount = zoomGesture.getPointerCount();
            zoomGesture.onPointerMove(ev.pointerId, eventToUnzoomedPoint(ev));

            if (pointerCount >= Math.min(2, pointerDownCount)) {
                let zoomCenter = zoomGesture.getCenter();

                sceneContent.moveViewport(
                    -(zoomCenter.x - oldZoomCenter.x) / sceneContent.getZoom(),
                    -(zoomCenter.y - oldZoomCenter.y) / sceneContent.getZoom()
                );
                sceneContent.zoomTo(zoomGesture.update(), zoomCenter);
            }
        }

        render();
    }, false);

    canvas.addEventListener("pointerup", (ev) => {
        ev.preventDefault();

        if (stroke != null) {
            sceneContent.addStroke(stroke);
        } else {
            zoomGesture = null;
        }

        stroke = null;
        pointerDownCount --;

        render();
    }, false);

    canvas.addEventListener("pointercancel", (ev) => {
        ev.preventDefault();
        canvas.releasePointerCapture(ev.pointerId);
        pointerDownCount --;
    }, false);

    canvas.addEventListener("contextmenu", (ev) => {
        ev.preventDefault();
    });


    sceneContent.updateViewportSize(canvas.width, canvas.height);
    await sceneContent.updateLoop();
}

main();
