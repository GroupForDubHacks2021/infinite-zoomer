"use strict";

import { Stroke } from "./stroke.js";

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
    const canvas = document.querySelector("#main");
    const ctx = canvas.getContext("2d");

    // TODO: Setup onpointermove, onpointerdown, onpointerup events.
    // Documentation: https://developer.mozilla.org/en-US/docs/Web/API/GlobalEventHandlers/onpointerdown
    function moveHandler(ev) {

    }

    canvas.addEventListener("pointerdown", (ev) => {
        // Do stuff with [ev] here.
        // I think ev has a property called something like "primary".
        // If ev.primary is false, then it's a secondary pointer (like the second
        // finger in a two-finger pinch-zoom gesture).
        //
        // If it's a secondary pointer, we probably want to switch from drawing to
        // zooming.
    });

    canvas.addEventListener("pointermove", (ev) => {

    });

    canvas.addEventListener("pointerup", (ev) => {

    });

    while (true)
    {
        // TODO: Ask server for new data
        // TODO: Re-render if there's anything new.

        await nextAnimationFrame();
    }
}

main();