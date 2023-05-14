var stompClient = null;

function setConnected(connected) {
  var connectButton = document.querySelector('#connect');
  var disconnectButton = document.querySelector('#disconnect');
  var nameInput = document.querySelector('#name');
  var greetingsTextarea = document.querySelector('#greetings');

  if (connectButton) {
    connectButton.disabled = connected;
  }

  if (disconnectButton) {
    disconnectButton.disabled = !connected;
  }

  if (nameInput) {
    nameInput.disabled = connected;
  }

  if (greetingsTextarea) {
    greetingsTextarea.innerHTML = '';
  }
}

function connect() {
  var socket = new SockJS('/chat');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/broadcast/broadcast-message', function(greeting) {
      showGreeting(JSON.parse(greeting.body).message);
    });
  }, function(error) {
    console.log('Error: ' + error);
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log('Disconnected');
}

function sendName() {
  var name = document.querySelector('#clientName').value;
  stompClient.send('/app/client-message', {}, JSON.stringify({'clientName': name}));
}

function sendMessage() {
  var fromInput = document.querySelector('#clientName');
  var textInput = document.querySelector('#clientMsg');
  var from = fromInput.value.trim(); // Remove any leading or trailing white space
  var text = textInput.value.trim(); // Remove any leading or trailing white space
  if (from && text && stompClient) {
    var message = {'clientName': from, 'clientMsg': text}; // Create the ClientMessageDTO object using 'from' and 'text'
    stompClient.send('/app/client-message', {}, JSON.stringify(message)); // Send the message to the server
    fromInput.value = ''; // Clear the input field
    textInput.value = ''; // Clear the input field
  }
}

function showGreeting(message) {
  console.log(message)
  var textarea = document.querySelector('#greetings');
  textarea.innerHTML += message + '\n';
}

setConnected(false);