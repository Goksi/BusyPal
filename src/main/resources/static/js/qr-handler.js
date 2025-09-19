function connect() {
  let socket = new WebSocket("/ws/websocket");
  let stompClient = StompJs.Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/topic/qr', function (msg) {
      console.log(msg)
    });
  });
}