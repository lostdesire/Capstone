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

io.on("connection", (socket) => {
	console.log(`${socket.id} 소켓 연결됨`);
	socket.emit('msg', `socket 연결 : ${socket.id}`);

	socket.on('enter', async (data) => {
		const roomData = JSON.parse(data);
		const cuid = roomData.cuid;
		const uid = roomData.uid;
		const rid = roomData.rid;
		const rsa = roomData.rsa;

		socket.join(`${rid}`);
		
		let sql = 'SELECT * FROM Room WHERE ROOM_NAME = ?';
		let arr = [rid, uid, rsa];
		let [user, fields] = await db.sql_get_val(sql, rid);
		if (user.length == 0) {
			sql = 'INSERT INTO Room(ROOM_NAME, UID) VALUES(?, ?)';
			db.sql_ins(sql, arr.slice(0, 2));
		}
		sql = 'SELECT * FROM Participant WHERE ROOM_NAME = ? AND UID = ?';
		[user, fields] = await db.sql_get_val(sql, arr.slice(0, 2));
		if (user.length == 0) {
			sql = 'INSERT INTO Participant(ROOM_NAME, UID, RSA) VALUES(?, ?, ?)';
                        db.sql_ins(sql, arr);
		} else {
			console.log(`${uid}님은 이미 ${rid}에 참가 중입니다.`);
		}
		
		const enterData = {
			type: 'ENTER',
			content: `${uid}님이 입장하셨습니다.`,
			rsa: rsa
		};

		io.to(`${rid}`).emit('ENTER', JSON.stringify(enterData));
		console.log(`${uid}님이 ${rid}방에 입장하셨습니다.`);
	});

	socket.on('leave', async (data) => {
		const roomData = JSON.parse(data);
		const uid = roomData.uid;
		const rid = roomData.rid;

		socket.leave(`${rid}`);
		
		let sql = 'DELETE FROM Participant WHERE ROOM_NAME = ? AND UID = ?';
		let arr = [rid, uid];
		await db.sql_ins(sql, arr);

		sql = 'SELECT * FROM Participant WHERE ROOM_NAME = ? AND UID = ?';
		let [user, fields] = await db.sql_get_val(sql, arr);
		if (user.length == 0) {
			sql = 'DELETE FROM Room WHERE ROOM_NAME = ? AND UID = ?';
			db.sql_ins(sql, arr);
			sql = 'DELETE FROM Chatting WHERE ROOM_NAME = ?';
			db.sql_ins(sql, rid);
		}

		const leaveData = {
			type: 'LEAVE',
			content: `${uid}님이 퇴장하셨습니다.`
		};

		io.to(`${rid}`).emit('LEAVE', JSON.stringify(leaveData));
		console.log(`${uid}님이 ${rid}에서 퇴장하셨습니다.`);
	});

	socket.on('msg', async (data) => {
		const roomData = JSON.parse(data);
		const uid = roomData.uid;
		const name = roomData.name;
		const rid = roomData.rid;
		const msg = roomData.msg;
		const time = new Date();
		const IV = roomData.iv;
		
		const sql = 'INSERT INTO Chatting(ROOM_NAME, UID, USER_NAME, MESSAGE, TIME, IV) VALUES(?, ?, ?, ?, ?, ?)';
		const arr = [rid, uid, name, msg, time, IV];
		db.sql_ins(sql, arr);

		const msgData = {
			type: 'msg',
			sender: uid,
			name: name,
			content: msg,
			time: time,
			iv: IV
		};

		io.to(`${rid}`).emit('update', JSON.stringify(msgData));
		console.log(`${uid}님이 ${rid}에 메세지를 전송하셨습니다.`);
	});

	socket.on('disconnect', () => {
		console.log(`${socket.id} 소켓 연결 해제`);
	});
});

server.listen(3000, () => {
        console.log('-서버 연결-');
});
