var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#messages").show();
        $("#greetings").empty();
        $("#greetings").prepend("<tr class='error'><td>" + "Connected" + "</td></tr>");
    }
    else {
        $("#messages").hide();
        $("#greetings").prepend("<tr class='error'><td>" + "Disconnected" + "</td></tr>");
    }
}

function connect() {
    // var socket = new SockJS('/ws');
    // stompClient = Stomp.over(socket);
    stompClient = Stomp.client('ws://localhost:8080/ws');
    stompClient.reconnect_delay = 5000;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat.lobby', function (frame) {
            showMessage(JSON.parse(frame.body));
            function showMessage(message) {
                switch (message.type) {
                    case 'message': return $("#greetings").prepend(
                        "<tr><td><strong>" +
                        message.username +
                        "</strong>: " +
                        message.payload +
                        "</td></tr>"
                    );
                    case 'history': return (message.payload || []).forEach(showMessage);
                    default: return $("#greetings").prepend("Unknown message type: " + message.type);
                }
            }
        });
    }, function (frame) {
        var headers = frame.headers || {};
        var message = headers.message || 'Unknown error: ' + frame;
        $("#greetings").prepend("<tr class='error'><td>" + message + "</td></tr>");
    }, function () {
        $("#greetings").prepend("<tr class='error'><td>" + 'Disconnected' + "</td></tr>");
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendApp() {
    stompClient.send("/app/chat.lobby", {}, JSON.stringify({payload: $("#name").val()}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send-app").click(function () {
        sendApp();
    });
    connect()
});

