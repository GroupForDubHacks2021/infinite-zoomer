
import { awaitTimeout } from "./asyncUtils.js";
import { Point } from "./Point.js";

/**
 * Handles storage/syncing of lines & other scene objects.
 */

const MIN_FETCH_INTERVAL_MS = 200;

class Scene {
    /**
     * Create a new scene.
     */
    constructor(renderCallback) {
        // If we call this.render(), we'll re-render.
        this.render = renderCallback;
        this.zoom = 1;
        this.viewportPosition = new Point(0, 0);

        // List of all scene objects we know about.
        this.entities = [];
    }

    /// Get a list of everything in the scene.
    /// Do not directly modify the returned list.
    getElems() {
        return this.entities;
    }

    addStroke(stroke) {
        this.entities.push(stroke);

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

    getServerData() {
        const zoom = this.zoom;
        const viewportPosition = this.viewportPosition;

        return new Promise(function(resolve, reject){
            let http = new XMLHttpRequest();
            let url = '/api?refresh:' + zoom + "," + viewportPosition.x + "," + viewportPosition.y;
            http.open('GET', url, true);

            http.onreadystatechange = function(){//Call a function when the state changes.
                if (http.readyState == 4 && http.status == 200) {
                    resolve(http.responseText);
                }
                else if (http.readyState == 4 && http.status != 200) {
                    // reject is like resolve, but for failure (throws an error)
                    reject(http.responseText);
                }
            }
            http.send();
        })
    }

    async updateLoop() {
        while (true) {
            // Wait some amount of time before syncing with the server.
            await awaitTimeout(MIN_FETCH_INTERVAL_MS);

            // TODO: SYNC!!!
        }
    }
}

export default Scene;
export { Scene };