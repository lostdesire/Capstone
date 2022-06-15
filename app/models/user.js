const db = require('./db.js');
const jwt = require('./jwt.js');

const User = (user) => {
	this.id = user.id;
	this.name = user.name;
	this.password = user.password;
	this.msg = user.msg;
	this.img = user.img;
};

User.find = async (req, res) => {
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
	
	const sql = 'SELECT USER_ID FROM User';
	let [result, fields] = await db.sql_get(sql);

	res.status(200).json(result);
};

User.findBy = async (req, res) => {
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
	const sql = 'SELECT USER_ID, USER_NAME, STATUS_MSG, PROFILE_IMG FROM User WHERE USER_ID = ?';
        let [result, fields] = await db.sql_get_val(sql, user.id);
        res.status(200).json(result[0]);
};

User.update = async (req, res) => {
	if (!req.headers.authorization) {
		console.log('토큰이 없습니다.');
                return res.status(403).json({msg: '토큰이 없습니다.'});
        }
        const token = req.headers.authorization.split(' ')[1];
	let user;
        try {
                user = await jwt.verify(token);
        } catch (err) {
		console.log('권한이 없습니다.');
                return res.status(403).json({msg: '권한이 없습니다.'});
        }
	if (req.params.uid != user.id) {
		console.log('해당 유저에 대한 권한이 없습니다.');
		return res.status(401).json({msg: '해당 유저에 대한 권한이 없습니다.'});
	}
	let sql = 'UPDATE User SET USER_NAME = ?, STATUS_MSG = ?, PROFILE_IMG = ? WHERE USER_ID = ?';
	let data = [req.body.name, req.body.msg, req.body.img, user.id];
	await db.sql_ins(sql, data);
	sql = 'UPDATE Chatting SET USER_NAME = ? WHERE UID = ?';
	data = [req.body.name, user.id];
        await db.sql_ins(sql, data);

        res.status(200).json({msg: '회원정보 수정 완료'});
	console.log(`${user.id}님이 회원정보를 수정하셨습니다.`);
};

User.remove = async (req, res) => {
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
	let sql = 'DELETE FROM User WHERE USER_ID = ?';
        await db.sql_ins(sql, user.id);
	sql = 'DELETE FROM Room WHERE UID = ?';
	await db.sql_ins(sql, user.id);
	sql = 'DELETE FROM Participant WHERE UID = ?';
	await db.sql_ins(sql, user.id);
	sql = 'DELETE FROM Friend WHERE UID = ?';
	await db.sql_ins(sql, user.id);

        res.status(200).json({msg: '회원탈퇴 하였습니다.'});
	console.log(`${user.id}님이 회원탈퇴 하셨습니다.`);
};

module.exports = User;
