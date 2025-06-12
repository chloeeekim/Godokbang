function sendMessage() {
    const body = {
        roomId: roomId,
        message: document.getElementById('chat-input').value,
        type: "TEXT"
    };

    fetch('/chat/message', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    });
}

function joinRoom(button) {
    const roomId = button.getAttribute('data-id');
    fetch(`/chat/room/${roomId}/join`, {
        method: 'POST'
    }).then(res => {
        if (res.redirected) {
            window.location.href = res.url;
        }
    });
}

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/chatroom.' + roomId, function (messageOutput) {
            const msg = JSON.parse(messageOutput.body);
            const chatBox = document.getElementById('chat-box');
            const line = document.createElement('div');
            line.innerText = `[${msg.senderNickname}] ${msg.message}`;
            chatBox.appendChild(line);
        });
    });
}