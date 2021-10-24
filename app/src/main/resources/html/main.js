"use strict";

import { Stroke } from "./Stroke.js";
import { Point } from "./Point.js";
import { ZoomController } from "./ZoomController.js";

/// An async function that resolves after a short amount of time.
/// It uses requestAnimationFrame, so the browser can make this take
/// longer to complete if, say, the user isn't looking at the tab.
function nextAnimationFrame()
{
    return new Promise((resolve, reject) =>
    {
        requestAnimationFrame(resolve);
    });
}

function getStrokes(){

    return new Promise(function(resolve, reject){
        var http = new XMLHttpRequest();
        var url = '/api?getstroke';
        http.open('GET', url, true);

        http.onreadystatechange = function() {//Call a function when the state changes.
            if(http.readyState == 4 && http.status == 200) {
                resolve(http.responseText);
            }
        }
        http.send();
    })
}

async function main()
{
    const canvas = document.querySelector("#mainCanvas");
    const ctx = canvas.getContext("2d");
    let stroke = null;

    let zoom = 1;
    let viewportPosition = new Point(0, 0);

    let sceneContent = [];

    // Render everything!
    const render = () => {
        // Re-size the drawing context (if the canvas size changed)
        // If we don't do this, everything will look stretched!
        if (canvas.clientWidth !== canvas.width || canvas.clientHeight !== canvas.height) {
            canvas.width = canvas.clientWidth;
            canvas.height = canvas.clientHeight;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        let transform = (point) => {
            // TODO: Transform point based on current zoom, position, etc.

            point.x += viewportPosition.x;
            point.y += viewportPosition.y;
            point.x *= zoom;
            point.y *= zoom;

            // E.g.
            // point.x *= 2;
            // point.x -= 500;
            // While we could use ctx.scale or ctx.transform, that won't work
            // if we want to be able to zoom in as far as we want.
        };

        // Render all elements!
        for (const elem of sceneContent) {
            elem.render(ctx, transform);
        }

        // Render the current stroke, too
        if (stroke) {
            stroke.render(ctx, transform);
        }
    };

    /// Given a PointerEvent, convert it to a point.
    const eventToPoint = (ev) => {
        // Get location of the target element (our canvas)
        const bbox = canvas.getBoundingClientRect();

        // x is in page coordinates, so we need to subtract the
        // canvas' distance from the left of the page
        const x = (event.clientX - bbox.left)/zoom - viewportPosition.x;
        const y = (event.clientY - bbox.top)/zoom - viewportPosition.y;

        return new Point(x, y);
    };

    const zoomTo = (newZoom, center) => {
        viewportPosition.x -= center.x / zoom;
        viewportPosition.y -= center.y / zoom;

        zoom = newZoom;

        viewportPosition.x += center.x / zoom;
        viewportPosition.y += center.y / zoom;
    };

    zoom_slider.oninput = function() {
        // Keep the view centered.

        zoomTo(zoom_slider.value / zoom_slider.max, new Point(canvas.width / 2, canvas.height / 2));

        render();
    };

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

        if (pointerDownCount == 0) {
            stroke = new Stroke();
            stroke.addPoint(eventToPoint(ev));
        } else {
            stroke = null;
            zoomGesture = new ZoomController(zoom);
            zoomGesture.onPointerMove(ev.pointerId, new Point(ev.clientX, ev.clientY));
        }

        pointerDownCount ++;
        console.log(pointerDownCount);
        render();
    });

    canvas.addEventListener("pointermove", (ev) => {
        if (pointerDownCount == 0) {
            return;
        }

        ev.preventDefault();
        let pointerLocation = eventToPoint(ev);

        if (stroke != null) {
            // We're drawing a stroke!

            stroke.addPoint(pointerLocation);
        } else {
            // We're zooming!
            let oldZoomCenter = zoomGesture.getCenter();
            let pointerCount = zoomGesture.getPointerCount();
            zoomGesture.onPointerMove(ev.pointerId, new Point(ev.clientX, ev.clientY));

            if (pointerCount >= 2) {
                let zoomCenter = zoomGesture.getCenter();

                viewportPosition.x += (zoomCenter.x - oldZoomCenter.x) / zoom;
                viewportPosition.y += (zoomCenter.y - oldZoomCenter.y) / zoom;
                zoomTo(zoomGesture.update(), zoomCenter);
            }
        }

        render();
    });

    canvas.addEventListener("pointerup", (ev) => {
        ev.preventDefault();

        // TODO: Send stroke to the server.
        if (stroke != null) {
            sceneContent.push(stroke);
            var http = new XMLHttpRequest();
            var url = '/api?addstroke';
            var params = stroke.serialize()+ '\n';
            http.open('POST', url, true);

            //Send the proper header information along with the request
            http.setRequestHeader('Content-type', 'text/plain');

            http.onreadystatechange = function() {//Call a function when the state changes.
                if(http.readyState == 4 && http.status == 200) {
                    console.log(http.responseText);
                }
            }
            http.send(params);
        } else {
            zoomGesture = null;
        }

        stroke = null;
        pointerDownCount --;

        render();
    });

    while (true)
    {

        // TODO: Ask server for new data
        // console.log(await getStrokes());
        // TODO: Re-render if there's anything new.

        await nextAnimationFrame();
    }
}

main();