function showAlert(msg) {
    alert(msg);
}

var eventSinkSocket = undefined;
var eventMap = new Map();
var application = undefined;

function setupEventChannel(event_sink_ws, jwtToken, applicationName, callback) {
    eventSinkSocket = new WebSocket(event_sink_ws, ["access_token", jwtToken]);
    eventSinkSocket.onopen = callback;
    application = applicationName;

    setInterval(() => eventSinkSocket.send("keepalive"), 50000);
}

function setupFeedback(e) {
    e.innerHTML = "<input type='button' class='btn btn-warning' value='give feedback'></input>"
    e.addEventListener("click", function () {
        alert("feedback provided");
    });

}

HTMLDivElement.prototype.logged = function (eventType, payload, htmlEventType, callback) {
    this.addEventListener(htmlEventType, function () {
        logEvent(eventType, payload, application);
        callback(this);
    });
}


function logEvent(eventType, payload, grace_delay) {
    if (grace_delay == undefined) {
        return logEvent(eventType, payload, 0);
    } else {
        if (eventSinkSocket && eventSinkSocket.readyState == WebSocket.OPEN) {
            let event = JSON.stringify({
                "application": application,
                "type": eventType,
                "payload": payload
            });
            let sendEvent = () => {
                eventSinkSocket.send(event);
            };
            if (grace_delay > 0) {
                if (eventMap.has(eventType)) {
                    window.clearTimeout(eventMap.get(eventType));

                }
                eventMap.set(eventType, setTimeout(sendEvent, grace_delay));
            } else {
                sendEvent();
            }

        } else {
            console.warn("failed to use event sink");
        }
    }
}

export {showAlert, setupFeedback, logEvent, setupEventChannel}