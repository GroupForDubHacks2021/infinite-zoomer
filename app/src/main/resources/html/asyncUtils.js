
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

/// Resolves after [timeout] milliseconds
function awaitTimeout(timeout)
{
    return new Promise((resolve, reject) =>
    {
        setTimeout(resolve, timeout);
    });
}

export { nextAnimationFrame, awaitTimeout };