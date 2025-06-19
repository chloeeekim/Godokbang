function sendMessage() {
    const chatInput = document.getElementById('chat-input');
    const body = {
        roomId: roomId,
        content: chatInput.value,
        type: "TEXT"
    };

    fetch('/api/chat/message', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    });

    chatInput.value = "";
    chatInput.style.height = 'auto';
}

function joinRoom(button) {
    const roomId = button.closest('#room-dom').dataset.id;
    fetch(`/chat/room/${roomId}/join`, {
        method: 'POST'
    }).then(res => {
        if (res.redirected) {
            window.location.href = res.url;
        }
    });
}

function openRoom(button) {
    const roomId = button.closest('#room-dom').dataset.id;
    fetch(`/chat/room/${roomId}`, {
        method: 'GET'
    }).then(res => {
        window.location.href = res.url;
    });
}

function chatConnect(onConnected) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/chatroom.' + roomId, function (messageOutput) {
            const msg = JSON.parse(messageOutput.body);
            addMessage(msg, true);
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

function addMessage(msg, isNew, isFirstFetch) {
    const chatBox = document.getElementById('chat-box');
    const chatDom = document.getElementById('chat-dom');

    const clone = chatDom.cloneNode(true);
    clone.dataset.id = msg.id;
    clone.dataset.sentAt = msg.sentAt;

    const scrollTop = chatBox.scrollTop;
    const clientHeight = chatBox.clientHeight;
    const scrollHeight = chatBox.scrollHeight;
    const isScrollBottom = (scrollTop + clientHeight >= scrollHeight);

    if (msg.type == 'ENTER' || msg.type == 'LEAVE' || msg.type == 'CREATE') {
        const line = createElement('p', 'system-message', msg.content);
        clone.innerHTML = "";
        clone.append(line);
    } else if (msg.type == 'TEXT') {
        clone.querySelector('.profile-img > img').setAttribute('src', msg.senderProfileImage);
        clone.querySelector('.sender-nickname').textContent = msg.senderNickname;
        clone.querySelector('.sent-at').textContent = msg.sentAt;
        const content = clone.querySelector('.message-content');
        content.textContent = msg.content;
        content.hidden = false;
    } else {
        clone.querySelector('.profile-img > img').setAttribute('src', msg.senderProfileImage);
        clone.querySelector('.sender-nickname').textContent = msg.senderNickname;
        clone.querySelector('.sent-at').textContent = msg.sentAt;
        const img = clone.querySelector('.img-message');
        img.setAttribute('src', msg.content);
        img.hidden = false;
    }

    if (isNew) {
        chatBox.append(clone);
    } else {
        chatBox.prepend(clone);
    }

    if (isFirstFetch || (isScrollBottom && isNew)) {
        chatBox.scrollTop = chatBox.scrollHeight;
    }
}

function createElement(tag, className, textContent) {
    const elem = document.createElement(tag);
    if (className) elem.classList.add(className);
    if (textContent) elem.textContent = textContent;
    return elem;
}

function loadMessages(isFirstFetch) {
    if (!chatHasNext) return;

    chatFetching = true;
    const firstItem = document.querySelector('#chat-box > #chat-dom');
    const baseUrl = `/api/chat/${roomId}`;
    let queryString;

    if (firstItem) {
        const params = {
            lastSentAt: firstItem.dataset.sentAt.replace(" ", "T"),
            lastId: firstItem.dataset.id
        };
        queryString = new URLSearchParams(params).toString();
    }

    const url = !firstItem ? baseUrl : `${baseUrl}?${queryString}`;

    fetch(url, {
        method: 'GET'
    })
    .then(res => res.json())
    .then(data => {
        if (!data.empty) {
            data.content.forEach(message => {
                addMessage(message, false, isFirstFetch);
            });

            observeFirstItem(chatObserver, '#chat-box > #chat-dom');
        }
        chatFetching = false;
        chatHasNext = !data.last;
    });
}

function uploadFile(input) {
    const formData = new FormData();
    formData.append("file", input.files[0]);
    formData.append("request", new Blob([JSON.stringify({
        roomId: roomId,
        type: "IMAGE"
    })], { type: "application/json"}));

    fetch('/api/chat/upload', {
        method: 'POST',
        body: formData
    });
}

function loadNotifications(count) {
    if (!notiHasNext) return;

    notiFetching = true;
    const notis = document.querySelectorAll('#notifications > #noti-dom');
    const lastItem = notis[notis.length - 1];
    const baseUrl = `/api/notifications`;
    let queryString;

    if (notis.length != 0) {
        const params = {
            lastCreatedAt: lastItem.dataset.createdAt,
            lastId: lastItem.dataset.id,
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

function observeFirstItem(observer, selector) {
    const firstItem = document.querySelector(selector);

    if (!firstItem) return;
    if (observer) observer.disconnect();

    observer.observe(firstItem);
}

function loadDiscoverList() {
    if (!discoverHasNext) return;

    discoverFetching = true;
    const rooms = document.querySelectorAll('#chatrooms > #room-dom');
    const lastItem = rooms[rooms.length - 1];
    const baseUrl = `/api/chat/discover`;
    let queryString;
    const keyword = document.querySelector('.search-bar > .search-input').value;

    if (rooms.length != 0) {
        const params = {
            lastAt: lastItem.dataset.messageAt,
            lastId: lastItem.dataset.id,
            keyword: keyword
        };
        queryString = new URLSearchParams(params).toString();
    } else {
        if (isSearched) {
            const params = {
                keyword: keyword
            };
            queryString = new URLSearchParams(params).toString();
        }
    }

    const url = queryString == null ? baseUrl : `${baseUrl}?${queryString}`;

    fetch(url, {
        method: 'GET'
    })
    .then(res => res.json())
    .then(data => {
        if (!data.empty) {
            document.getElementById('empty-content').hidden = true;
            document.getElementById('content-main').hidden = false;

            data.content.forEach(room => {
                addDiscoverRoom(room);
            });

            observeLastItem(discoverObserver, '#chatrooms > #room-dom');
        } else {
            document.getElementById('empty-content').hidden = false;
            document.getElementById('content-main').hidden = true;
        }
        discoverFetching = false;
        discoverHasNext = !data.last;
    });
}

function addDiscoverRoom(room) {
    const div = document.getElementById('chatrooms');
    const roomDom = document.getElementById('room-dom');

    const clone = roomDom.cloneNode(true);
    clone.dataset.id = room.id;
    clone.dataset.messageAt = room.latestMsgAt;
    clone.dataset.createdAt = room.createdAt;

    clone.querySelector('.list-title').textContent = room.title;
    clone.querySelector('.list-description').textContent = room.description;
    clone.querySelector('.list-user-count > p').textContent = `${room.userCount} / ${room.maxUser}`;

    if (room.joined) {
        clone.querySelector('.btn-open').hidden = false;
        clone.querySelector('.btn-join').hidden = true;
    } else {
        clone.querySelector('.btn-join').hidden = false;
        clone.querySelector('.btn-open').hidden = true;
    }

    div.append(clone, createElement('hr'));
}

function searchChatRooms() {
    document.getElementById('chatrooms').innerHTML = "";
    discoverHasNext = true;
    isSearched = true;

    loadDiscoverList();
}