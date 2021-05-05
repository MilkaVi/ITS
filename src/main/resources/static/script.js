window.addEventListener("load", connect, false);
window.addEventListener("load", getId, false);


function connect() {
    var socket = new SockJS('/turning');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        sendMessage()
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

    // console.log("drawing...");
    // var $message;
    // $message = $($('.message_template').clone().html());
    // $message.addClass(side).find('.text').html(text);
    // $('.messages').append($message);
    // return setTimeout(function () {
    //     return $message.addClass('appeared');
    // }, 0);

}


function disconnect() {
    stompClient.disconnect();
}

function getId(){
    id = $("#id").val();
}

function sendMessage() {


    console.log("essss ".concat(id))


    stompClient.send("/turning", {}, id);
}