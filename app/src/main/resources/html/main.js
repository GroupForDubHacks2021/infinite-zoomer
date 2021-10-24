"use strict";

import { Stroke } from "./Stroke.js";

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

    // Documentation: https://developer.mozilla.org/en-US/docs/Web/API/GlobalEventHandlers/onpointerdown
    canvas.addEventListener("pointerdown", (ev) => {
        // Do stuff with [ev] here.
        // I think ev has a property called something like "primary".
        // If ev.primary is false, then it's a secondary pointer (like the second
        // finger in a two-finger pinch-zoom gesture).
        //
        // If it's a secondary pointer, we probably want to switch from drawing to
        // zooming.

        if (ev.primary) {
            stroke = new Stroke();
            storke.addPoint(new Point(ev.clientX, ev.clientY));
        }
    });

    canvas.addEventListener("pointermove", (ev) => {
        if (stroke != null) {
            ;;
        } else {
            // Zoom???
        }
    });

    canvas.addEventListener("pointerup", (ev) => {
        // TODO: Send stroke to the server.
        if (stroke != null) {
            ;;
        }

        stroke = null;
    });

    while (true)
    {
        // TODO: Ask server for new data
        // TODO: Re-render if there's anything new.

        await nextAnimationFrame();
    }
}

main();