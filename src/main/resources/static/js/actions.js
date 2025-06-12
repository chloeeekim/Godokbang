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