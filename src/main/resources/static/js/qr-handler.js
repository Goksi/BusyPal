function connect() {
  let stompClient = StompJs.Stomp.over(function () {
    return new WebSocket("/ws/websocket");
  });
  stompClient.connect({}, function (frame) {
    console.log('Connected to ws: ' + frame);
    let session = getCookie('busypal_session')
    stompClient.subscribe('/topic/qr-user' + session, handleMessage);
    stompClient.publish({destination: '/busypal/ready', body: JSON.stringify({event_type: "client_ready", cookie: session})})
  });
}

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}


function handleMessage(message) {
  let parsedMsg = JSON.parse(message.body);
  if (parsedMsg.event_type === 'wa_qr_code') {
    let qrCode = parsedMsg.qr;
    console.log("DOBIO SAM QR: " + qrCode)
  }
}

// I HATE JS