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

function connect(onConnected) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/chatroom.' + roomId, function (messageOutput) {
            const msg = JSON.parse(messageOutput.body);
            addMessage(msg);
        });

        if (typeof onConnected == 'function') {
            onConnected();
        }
    });
}

function addMessage(msg) {
    const chatBox = document.getElementById('chat-box');
    const line = document.createElement('div');
    if (msg.type == 'ENTER' || msg.type == 'LEAVE' || msg.type == 'CREATE') {
        line.innerText = `${msg.message}`;
        line.classList.add('system-message');
    } else {
        line.innerText = `${msg.senderNickname} : ${msg.message}`;
    }
    chatBox.appendChild(line);
}

function getPreviousMessages() {
    fetch(`/chat/${roomId}/message`, {
        method: 'GET'
    })
    .then(res => res.json())
    .then(data => {
        if (Array.isArray(data) && data.length > 0) {
            data.forEach(message => {
                addMessage(message);
            });
        }
    });
}