window.addEventListener("load", connect, false);
window.addEventListener("load", getId, false);


function connect() {
    var socket = new SockJS('/turning');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/turning', function (response) {
            var data = JSON.parse(response.body);
            if (data.state)
                draw("left", "Light on");
            else
                draw("left", "Light off");

        });
    });


}
let id = null;

function draw(side, text) {
    $('#results').html(text).hide().slideDown('fast');
    $('#first').html(text).hide();
}

function getId(){
    id = $("#id").val();
}
function sendMessage() {
    stompClient.send("/turning", {}, id);
}





function disconnect() {
    stompClient.disconnect();
}

