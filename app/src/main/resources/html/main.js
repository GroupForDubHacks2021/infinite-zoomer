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

async function main()
{
    const canvas = document.querySelector("#mainCanvas");
    const ctx = canvas.getContext("2d");
    let stroke = null;

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
            stroke.addPoint(new Point(ev.clientX, ev.clientY));
        } else {
            // Initialize some zoom object?
        }

        render();
    });

    canvas.addEventListener("pointermove", (ev) => {
        ev.preventDefault();

        if (stroke != null) {
            stroke.addPoint(new Point(ev.clientX, ev.clientY));
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
            console.log("DONE");
        }

        stroke = null;

        render();
    });

    while (true)
    {
        // TODO: Ask server for new data
        // TODO: Re-render if there's anything new.

        await nextAnimationFrame();
    }
}

main();