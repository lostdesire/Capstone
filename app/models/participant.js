const db = require('./db.js');
const jwt = require('./jwt.js');

const Participant = (participant) => {
	this.ROOM_NAME = participant.rid;
	this.UID = participant.uid;
	this.RSA = participant.rsa;
};

Participant.find = async (req, res) => {
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
	
	const sql = 'SELECT * FROM Participant WHERE ROOM_NAME = ?';
	let [result, fields] = await db.sql_get_val(sql, req.params.rid);

	res.status(200).json(result);
};

Participant.findBy = async (req, res) => {
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

	const sql = 'SELECT * FROM Participant WHERE ROOM_NAME = ? AND UID = ?';
	const data = [req.params.rid, req.params.uid];
        let [result, fields] = await db.sql_get_val(sql, data);

        res.status(200).json(result);
};

Participant.remove = async (req, res) => {
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
		return res.status(401).json({msg: '해당 유저에 대한 권한이 없습니다.'});
	}
	const sql = 'DELETE FROM Participant WHERE ROOM_NAME = ? AND UID = ?';
	const data = [req.params.rid, user.id];
        await db.sql_ins(sql, data);

        res.status(200).json({msg: `${data[0]} 방에서 퇴장하셨습니다.`});
	console.log(`${data[1]}님이 ${data[0]} 방에서 퇴장하셨습니다.`);
};

module.exports = Participant;
