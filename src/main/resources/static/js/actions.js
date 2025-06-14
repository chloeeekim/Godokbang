function sendMessage() {
    const chatInput = document.getElementById('chat-input');
    const body = {
        roomId: roomId,
        content: chatInput.value,
        type: "TEXT"
    };

    fetch('/chat/message', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    });

    chatInput.value = "";
    chatInput.style.height = 'auto';
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
        line.innerText = `${msg.content}`;
        line.classList.add('system-message');
    } else if (msg.type == 'TEXT') {
        const sender = document.createElement('p');
        sender.innerText = `${msg.senderNickname}`;
        sender.classList.add('sender-nickname');

        const sentAt = document.createElement('p');
        sentAt.innerText = `${msg.sentAt}`;
        sentAt.classList.add('sent-at');

        const div = document.createElement('div');
        div.classList.add('meta-message');
        div.appendChild(sender);
        div.appendChild(sentAt);

        const content = document.createElement('p');
        content.innerText = `${msg.content}`;
        content.classList.add('message-content');

        line.appendChild(div);
        line.appendChild(content);
        line.classList.add('wrap-message');
    } else {
        const sender = document.createElement('p');
        sender.innerText = `${msg.senderNickname}`;
        sender.classList.add('sender-nickname');

        const sentAt = document.createElement('p');
        sentAt.innerText = `${msg.sentAt}`;
        sentAt.classList.add('sent-at');

        const div = document.createElement('div');
        div.classList.add('meta-message');
        div.appendChild(sender);
        div.appendChild(sentAt);

        const img = document.createElement('img');
        img.setAttribute('src', `${msg.content}`);
        img.classList.add('img-message');

        line.appendChild(div);
        line.appendChild(img);
        line.classList.add('wrap-message');
    }
    chatBox.appendChild(line);
    chatBox.scrollTop = chatBox.scrollHeight;
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

function uploadFile(input) {
    const formData = new FormData();
    formData.append("file", input.files[0]);
    formData.append("request", new Blob([JSON.stringify({
        roomId: roomId,
        type: "IMAGE"
    })], { type: "application/json"}));

    fetch('/chat/upload', {
        method: 'POST',
        body: formData
    });
}