var ws;

function connect() {
    var username = document.getElementById("username").value;
    
    var host = document.location.host;
    var port = document.location.port;
    if(port === null || port === "") {
    		host =  host + ":80";
    }
    var pathname = document.location.pathname;
    
    ws = new WebSocket("ws://" +host  + pathname + "realtime/" + username);

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