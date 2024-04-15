const db = require('./db.js');
const jwt = require('./jwt.js');

const Room = (room) => {
	this.rid = room.rid;
	this.uid = room.uid;
};

Room.find = async (req, res) => {
	if (!req.headers.authorization) {
		return res.status(403).json({msg: '토큰이 없습니다.'});
	}
	const token = req.headers.authorization.split(' ')[1];
	let user;
	try {
		user = await jwt.verify(token);
	} catch (err) {
		return res.status(403).json({msg: '권한이 없습니다.'});
	}
	
	const sql = 'SELECT ROOM_NAME FROM Room WHERE ROOM_NAME = ?';
	let [result, fields] = await db.sql_get_val(sql, req.params.rid);

	res.status(200).json(result);
};

Room.remove = async (req, res) => {
	if (!req.headers.authorization) {
                return res.status(403).json({msg: '토큰이 없습니다.'});
        }
        const token = req.headers.authorization.split(' ')[1];
	let user;
        try {
                user = await jwt.verify(token);
        } catch (err) {
                return res.status(403).json({msg: '권한이 없습니다.'});
        }

	if (req.params.uid != user.id) {
		return res.status(401).json({msg: '해당 권한이 없습니다.'});
	}
	let sql = 'DELETE FROM Room WHERE ROOM_NAME = ? AND USER_ID = ?';
	let data = [req.params.rid, req.params.uid];
        await db.sql_ins(sql, data);
	sql = 'DELETE FROM Participant WHERE ROOM_NAME = ?';
	await db.sql_ins(sql, data[0]);
	sql = 'DELETE FROM Chatting WHERE ROOM_NAME = ?';
	await db.sql_ins(sql, data[0]);

        res.status(200).json({msg: '방을 삭제하였습니다.'});
	console.log(`${data[0]} 방이 삭제되었습니다.`);
};

module.exports = Room;
