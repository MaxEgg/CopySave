const {app, BrowserWindow, ipcMain} = require('electron'),
		appUrl = "ws://localhost:55588/"
		url = require('url'),
		path = require('path'),
		requestPromise = require('minimal-request-promise'),
		childProcess = require('child_process'),
		kill = require('tree-kill'),
        W3CWebSocket = require('websocket').w3cwebsocket,
        client = new W3CWebSocket(appUrl);
 

var serverProcess,
	win;

ipcMain.on('eventFromAJava', (event, args)=>{
    console.log('eventFromAJava', event, args); 
});

client.onmessage = function(e) {
    if (typeof e.data === 'string') {
        console.log("Received: '" + e.data + "'");
    }
};

client.onerror = function() {
    console.log('Connection Error');
    client = new W3CWebSocket(appUrl);
};
 
client.onopen = function() {
    console.log('WebSocket Client Connected');
    function sendNumber() {
        if (client.readyState === client.OPEN) {
            var number = Math.round(Math.random() * 0xFFFFFF);
            client.send(number.toString());
            setTimeout(sendNumber, 1000);
        }
    }
    sendNumber();
};
 
client.onclose = function() {
    console.log('echo-protocol Client Closed');
    client._connect(appUrl);
};
 
function createWindow() {
    let platform = process.platform;
    if(platform == 'win32'){
    	serverProcess = childProcess.spawn('cmd.exe', ['/c', 'cblistener.bat'],{
            cwd: app.getAppPath() + '/build/scripts/cblistener'
        });
    }else{
    	serverProcess = childProcess.spawn(app.getAppPath() + '/server/bin/cblistener');
    }
    serverProcess.stdout.on('data', (data) => {
        console.log(data.toString());
    });

    serverProcess.stderr.on('data', (data) => {
        console.log(data.toString());
    });
}

function startUp() {
    console.log(client.readyState ,  client.OPEN);
    if (client.readyState && client.readyState === client.OPEN) {
        console.log('Server started!');
        // openWindow();
    }else{
        console.log('Waiting for the server start...');
        setTimeout(function () {
            startUp();
        }, 200);
    };
}

app.on('ready', createWindow);

// function openWindow() {
//         mainWindow = new BrowserWindow({
//             title: 'Copybitch',
//             width: 640,
//             height: 480
//         });

//         mainWindow.loadURL(url.format({
//             pathname: path.join(__dirname, 'index.html'),
//             protocol: 'file:',
//             slashes: true
//         }));

//         //mainWindow.loadURL(appUrl);


//         mainWindow.on('closed', function () {
//             mainWindow = null;
//         });

//         mainWindow.on('close', function (e) {
//             if (serverProcess) {
//                 e.preventDefault();

//                 // kill Java executable
//                 const kill = require('tree-kill');
//                 kill(serverProcess.pid, 'SIGTERM', function () {
//                     console.log('Server process killed');

//                     serverProcess = null;

//                     mainWindow.close();
//                 });
//             }
//         });
//     };


// app.on('ready', startUp);


app.on('quit', (e) => {
    console.log('QUIT', serverProcess.pid);
    if (serverProcess) {
        e.preventDefault();

        // kill Java executable
        kill(serverProcess.pid, 'SIGTERM', function () {
            console.log('Server process killed');
            serverProcess = null;
            if(mainWindow){
                mainWindow.close();
            }
        });
    }
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
});

// app.on('activate', () => {
//     if (win === null) {
//         createWindow()
//     }
// });