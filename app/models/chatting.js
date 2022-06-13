const db = require('./db.js');
const jwt = require('./jwt.js');

const Chatting = (chatting) => {
	this.rid = chatting.rid;
	this.uid = chatting.uid;
	this.name = chatting.name;
	this.msg = chatting.msg;
	this.iv = chatting.iv;
	this.time = chatting.time;
};

Chatting.find = async (req, res) => {
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
	
	const sql = 'SELECT * FROM Chatting WHERE ROOM_NAME = ? AND TIME > ?';
	const data = [req.params.rid, req.params.time];
	const [result, fields] = await db.sql_get_val(sql, data);

	res.status(200).json(result);
};

module.exports = Chatting;
