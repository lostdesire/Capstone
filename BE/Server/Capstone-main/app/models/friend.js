const db = require('./db.js');
const jwt = require('./jwt.js');

const Friend = (friend) => {
	this.id = user.id;
	this.name = user.name;
	this.password = user.password;
	this.msg = user.msg;
	this.img = user.img;
};

Friend.create = async (req, res) => {
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
	
	const sql = 'INSERT INTO Friend(UID, FUID) VALUES(?, ?)';
	const data = [req.params.uid, req.params.fuid];
	let [result, fields] = await db.sql_get(sql, data);

	res.status(200).json({msg: `${uid}님이 ${fuid}님을 친구로 추가하셨습니다.`});
	console.log(`${uid}님이 ${fuid}님을 친구로 추가하셨습니다.`);
};

Friend.findBy = async (req, res) => {
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

	const sql = 'SELECT * FROM Friend WHERE UID = ?';
        const [result, fields] = await db.sql_get_val(sql, user.id);

        res.status(200).json(result);

};

Friend.remove = async (req, res) => {
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
	const sql = 'DELETE FROM Friend WHERE UID = ? AND FUID = ?';
	const data = [req.params.uid, req.params.fuid];
	await db.sql_ins(sql, data);

        res.status(200).json({msg: `${fuid} 친구를 삭제 하였습니다.`});
	console.log(`${uid}님이 ${fuid}님을 친구에서 삭제 하였습니다.`);
};

module.exports = Friend;
