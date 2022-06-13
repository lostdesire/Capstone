const jwt = require('jsonwebtoken');
const secretKey = require('../config/config.js').PASSWORD;
const options = require('../config/config.js').options;

const TOKEN_EXPIRED = 419;
const TOKEN_INVALID = 401;

module.exports = {
        sign: async (user) => {
                const payload = {
                        id: user.id
                };
                const result = {
                        token: jwt.sign(payload, secretKey, options),
                };
                return result;
        },
        verify: async (token) => {
                let decoded;
                try {
                        decoded = jwt.verify(token, secretKey);
                } catch (err) {
                        if (err.message == 'jwt expired') {
                                console.log('토큰 만료');
                                return TOKEN_EXPIRED;
                        } else if (err.message == 'invalid token') {
                                console.log('유효하지 않은 토큰');
                                return TOKEN_INVALID;
                        } else {
                                console.log('유효하지않은 토큰');
                                return TOKEN_INVALID;
                        }
                }
                return decoded;
        }
}
