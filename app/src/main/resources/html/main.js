"use strict";

import { Stroke } from "./Stroke.js";
import { Point } from "./Point.js";

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

    zoom_slider.oninput = function(){
        zoom = zoom_slider.value/zoom_slider.max;
        console.log(zoom);
        render();
    }

    /// Given a PointerEvent, convert it to a point.
    const eventToPoint = (ev) => {
        // Get location of the target element (our canvas)
        const bbox = canvas.getBoundingClientRect();

        // x is in page coordinates, so we need to subtract the
        // canvas' distance from the left of the page
        const x = (event.clientX - bbox.left)/zoom;
        const y = (event.clientY - bbox.top)/zoom;

        return new Point(x, y);
    };

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

        if (ev.isPrimary) {
            stroke = new Stroke();
            stroke.addPoint(eventToPoint(ev));
        } else {
            // Initialize some zoom object?
        }

        render();
    });

    canvas.addEventListener("pointermove", (ev) => {
        ev.preventDefault();

        if (stroke != null) {
            stroke.addPoint(eventToPoint(ev));
        } else {
            // Zoom???
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
        }

        stroke = null;

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