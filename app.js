const express = require('express');
const http = require('http');
const bodyParser = require('body-parser');

const app = express();
const server = http.createServer(app);
const io = require('socket.io')(server);

const db = require('./app/models/db.js');
const jwt = require('./app/models/jwt.js');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

app.get('/', (req, res) => {
	res.json({msg: 'This is JJalTok'});
});

require('./app/routes/routes.js')(app);

io.sockets.on('connection', (socket) => {
	console.log(`${socket.id} 소켓 연결됨`);

	socket.on('enter', async (data) => {
		const roomData = JSON.parse(data);
		const cuid = roomData.cuid;
		const uid = roomData.uid;
		const rid = roomData.rid;
		const rsa = roomData.rsa;
		//
		
		const enterData = {
			type: 'ENTER',
			content: `${uid}님이 입장하셨습니다.`,
			rsa: rsa
		};

		socket.to(`${rid}`).emit('ENTER', JSON.stringify(enterData));
	});

	socket.on('leave', async (data) => {
		const roomData = JSON.parse(data);
		const uid = roomData.uid;
		const rid = roomData.rid;
		//

		const leaveData = {
			type: 'LEAVE',
			content: `${uid}님이 퇴장하셨습니다.`
		}

		socket.to(`${rid}`).emit('LEAVE', JSON.stringify(leaveData));
	});

	socket.on('msg', async (data) => {
		const roomData = JSON.parse(data);
		const uid = roomData.uid;
		const rid = roomData.rid;
		const msg = roomData.msg;
		const time = new Date();
		const IV = roomData.iv;
		//

		const msgData = {
			type: 'msg',
			sender: uid,
			content: msg,
			time: time,
			iv: IV
		}

		socket.to(`${rid}`).emit('update', JSON.stringify(msgData));
	});

	socket.on('disconnect', () => {
		console.log(`${socket.id} 소켓 연결 해제`);
	});
});

app.listen(3000, () => {
	console.log('-서버 연결-');
});
