function connect() {
  let stompClient = StompJs.Stomp.over(function () {
    return new WebSocket("/ws/websocket");
  });
  stompClient.connect({}, function (frame) {
    console.log('Connected to ws: ' + frame);
    let cookie = getCookie('busypal_session')
    stompClient.subscribe('/topic/qr-user' + cookie, function (msg) {
      console.log('Received QR data: ' + msg.body)
    });
  });
}

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

// I HATE JS