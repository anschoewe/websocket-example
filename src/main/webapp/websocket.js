var ws;

function connect() {
    var username = document.getElementById("username").value;
    
    var protocol = document.location.protocol;
    var host = document.location.host;
    var pathname = document.location.pathname;
    var websocketProtocol = "ws:";
    
    if(protocol === "https:") {
    		websocketProtocol = "wss:";
    }
    
    ws = new WebSocket(websocketProtocol + "//" +host  + pathname + "realtime/" + username);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}