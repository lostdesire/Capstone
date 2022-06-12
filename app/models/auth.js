const db = require('./db.js');
const jwt = require('./jwt.js');
const bcrypt = require('bcrypt');

module.exports = {
        signing: async (req, res) => {
                console.log('-signin-');
                const {id, password, name, msg, img} = req.body;
                let sql = 'SELECT * FROM User WHERE USER_ID = ?';
                const [user, fields] = await db.sql_get(sql, id);
                if(user.length == 0) {
                        const hashPassword = await bcrypt.hash(password, 10);
                        sql = 'INSERT INTO USER(USER_ID, USER_PASSWORD, USER_NAME, STATUS_MSG, PROFILE_IMG) values (?, ?, ?, ?, ?)';
                        const data = [id, hashPassword, name, msg, img];
                        await db.sql_ins(sql, data);
                        console.log(`${id} 회원가입 성공`);
                        res.status(200).json({msg: '회원가입 성공'});
                } else {
                        console.log('이미 존재하는 ID');
                        res.status(401).json({msg: '이미 존재하는 ID'});
                }
        },
        login: async (req, res) => {
                console.log('-login-');
                const {id, password} = req.body;
                const sql = 'SELECT * FROM User WHERE USER_ID = ?';
                const [user, fields] = await db.sql_get_val(sql, id);

                if(user.length == 0) {
                        console.log('존재하지 않는 ID');
                        return res.status(401).json({msg: '존재하지 않는 ID'});
                } else {
                        const isEqual = await bcrypt.compareSync(password, user[0].USER_PASSWORD);
                        if(isEqual) {
                                const userToken = {id: id};
                                const jwtToken = await jwt.sign(userToken);
                                res.status(200).json({
                                        msg: '로그인 성공',
                                        token: jwtToken.token
                                });
                                console.log('로그인 성공');
                        } else {
                                res.status(400).json({msg: '로그인 실패'});
                                console.log('로그인 실패');
                        }
                }
        }
}
