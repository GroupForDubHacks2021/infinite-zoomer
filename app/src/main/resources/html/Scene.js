
import { awaitTimeout } from "./asyncUtils.js";
import { Point } from "./Point.js";
import { Stroke } from "./Stroke.js";

/**
 * Handles storage/syncing of lines & other scene objects.
 */

const MIN_FETCH_INTERVAL_MS = 500;
const ERR_PAUSE_BEFORE_RETRY_MS = 4500;

class Scene {
    /**
     * Create a new scene.
     */
    constructor(renderCallback) {
        // If we call this.render(), we'll re-render.
        this.render = renderCallback;
        this.zoom = 1;
        this.sceneTranslation = new Point(0, 0);

        // List of all scene objects we know about.
        this.entities = [];
    }

    /// Get a list of everything in the scene.
    /// Do not directly modify the returned list.
    getElems() {
        return this.entities;
    }

    updateViewportSize(width, height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    addStroke(stroke) {
        this.entities.push(stroke);

        if (this.haveBackend()) {
            var http = new XMLHttpRequest();
            var url = '/api?addstroke';
            var params = stroke.serialize()+ '\n';
            http.open('POST', url, true);

            //Send the proper header information along with the request
            http.setRequestHeader('Content-type', 'text/plain');

            // TODO: May want to re-try on failure.
            http.onreadystatechange = function() {//Call a function when the state changes.
                if(http.readyState == 4 && http.status == 200) {
                    console.log("Added stroke: ", http.responseText);
                }
            };

            http.send(params);
        }
    }

    setZoom(zoom) {
        this.zoom = zoom;
    }

    getZoom() {
        return this.zoom;
    }

    zoomTo(newZoom, center) {
        this.sceneTranslation.x -= center.x / this.zoom;
        this.sceneTranslation.y -= center.y / this.zoom;

        this.setZoom(newZoom);

        this.sceneTranslation.x += center.x / this.zoom;
        this.sceneTranslation.y += center.y / this.zoom;
    }

    moveViewport(deltaX, deltaY) {
        this.sceneTranslation.x -= deltaX;
        this.sceneTranslation.y -= deltaY;
    }

    getServerData() {
        // If we're being hosted statically, we can't
        // get data from the server.
        if (!this.haveBackend()) {
            return;
        }

        const zoom = this.zoom;
        const sceneTranslation = this.sceneTranslation;

        let centerPosition = new Point(
            sceneTranslation.x  * this.zoom + this.viewportWidth / 2 * this.zoom,
            sceneTranslation.y  * this.zoom + this.viewportHeight / 2 * this.zoom
        );
        let radius = Math.max(this.viewportWidth / 2 * this.zoom, this.viewportHeight / 2 * this.zoom) * 2.0;

        return new Promise(function(resolve, reject){
            let http = new XMLHttpRequest();
            let url = '/api?refresh:' + radius + "," + centerPosition.x + "," + centerPosition.y;
            http.open('GET', url, true);

            http.onreadystatechange = function(){//Call a function when the state changes.
                if (http.readyState == 4 && http.status == 200) {
                    resolve(http.responseText);
                }
                else if (http.readyState == 4 && http.status != 200) {
                    // reject is like resolve, but for failure (throws an error)
                    console.error("Error: ", http.responseText);
                    reject(http.responseText);
                }
            }
            http.send();
        })
    }

    /// Return false iff we're being hosted statically.
    haveBackend() {
        return window.location.href.indexOf("github.io") == -1;
    }

    async refreshScene() {
        let serverTxt = await this.getServerData();
        let strokes = serverTxt.split(";;");
        this.entities = [];

        for (let strokeData of strokes) {
            this.entities.push(new Stroke(strokeData));
        }
    }

    async updateLoop() {
        // If we're being hosted statically, just return.
        if (!this.haveBackend()) {
            return;
        }

        while (true) {
            // Wait some amount of time before syncing with the server.
            await awaitTimeout(MIN_FETCH_INTERVAL_MS);

            try {
                await this.refreshScene();
                this.render();
            } catch (e) {
                console.error("ERROR: Error fetching from server: ", e);
                await awaitTimeout(ERR_PAUSE_BEFORE_RETRY_MS);
            }
        }
    }
}

export default Scene;
export { Scene };
