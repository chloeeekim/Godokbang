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

function chatConnect(onConnected) {
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

function notiConnect(onConnected) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/notification.' + userId, function (notificationOutput) {
            const noti = JSON.parse(notificationOutput.body);
            addNotification(noti, true);
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
        line.innerText = msg.content;
        line.classList.add('system-message');
    } else if (msg.type == 'TEXT') {
        const sender = createElement('p', 'sender-nickname', msg.senderNickname);
        const sentAt = createElement('p', 'sent-at', msg.sentAt);

        const meta = createElement('div', 'meta-message');
        meta.append(sender, sentAt);

        const content = createElement('p', 'message-content', msg.content);

        line.classList.add('wrap-message');
        line.append(meta, content);
    } else {
        const sender = createElement('p', 'sender-nickname', msg.senderNickname);
        const sentAt = createElement('p', 'sent-at', msg.sentAt);

        const meta = createElement('div', 'meta-message');
        meta.append(sender, sentAt);

        const img = createElement('img', 'img-message');
        img.setAttribute('src', msg.content);

        line.classList.add('wrap-message');
        line.append(meta, img);
    }
    chatBox.append(line);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function createElement(tag, className, textContent) {
    const elem = document.createElement(tag);
    if (className) elem.classList.add(className);
    if (textContent) elem.textContent = textContent;
    return elem;
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

function loadNotifications(count) {
    if (!notiHasNext) return;

    notiFetching = true;
    const notis = document.querySelectorAll('#notifications > #noti-dom');
    const baseUrl = `/api/notifications`;
    let queryString;

    if (notis.length != 0) {
        const params = {
            lastCreatedAt: notis[notis.length - 1].dataset.createdAt,
            lastId: notis[notis.length - 1].dataset.id,
            size: count
        };
        queryString = new URLSearchParams(params).toString();
    }

    const url = notis.length == 0 ? baseUrl : `${baseUrl}?${queryString}`;

    fetch(url, {
        method: 'GET'
    })
    .then(res => res.json())
    .then(data => {
        if (!data.empty) {
            document.getElementById('empty-content').hidden = true;
            document.getElementById('content-main').hidden = false;

            data.content.forEach(notification => {
                addNotification(notification, false);
            });

            observeLastItem(notiObserver, '#notifications > #noti-dom');
        }
        notiFetching = false;
        notiHasNext = !data.last;
    });
}

function addNotification(noti, isNew) {
    const div = document.getElementById('notifications');
    const notiDom = document.getElementById('noti-dom');

    const clone = notiDom.cloneNode(true);
    clone.dataset.id = noti.id;
    clone.dataset.createdAt = noti.createdAt;

    clone.querySelector('.room-title > p').textContent = noti.roomTitle;
    clone.querySelector('.sender-nickname').textContent = noti.senderNickname;
    clone.querySelector('.message-content').textContent = noti.content;
    clone.querySelector('.sent-at').textContent = noti.sentAt;
    clone.querySelector('.is-read').classList.add(noti.read.toString());

    if (isNew) {
        div.prepend(clone, createElement('hr'));
    } else {
        div.append(clone, createElement('hr'));
    }
}

function updateReadStatus(btn) {
    const notiDom = btn.closest('#noti-dom');
    const notiId = notiDom.dataset.id;
    let uri;

    if (btn.classList.contains("false")) {
        uri = "read";
        btn.classList.replace("false", "true");
    } else {
        uri = "unread";
        btn.classList.replace("true", "false");
    }

    fetch(`/api/notifications/${notiId}/${uri}`, {
        method: 'PATCH'
    });
}

function deleteNotification(btn) {
    const notiDom = btn.closest('#noti-dom');
    const notiId = notiDom.dataset.id;

    fetch(`/api/notifications/${notiId}`, {
        method: 'DELETE'
    });

    notiDom.classList.add('hide');
    notiDom.addEventListener("transitionend", () => {
        notiDom.remove();
        loadNotifications(1);

        const notis = document.querySelectorAll('#notifications > #noti-dom');
        if (notis.length == 0) {
            document.getElementById('empty-content').hidden = false;
            document.getElementById('content-main').hidden = true;
        }
    }, { once: true});

    notiDom.nextSibling.remove();
}

function observeLastItem(observer, selector) {
    const items = document.querySelectorAll(selector);
    const lastItem = items[items.length - 1];

    if (!lastItem) return;
    if (observer) observer.disconnect();

    observer.observe(lastItem);
}