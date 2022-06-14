const auth = require('../models/auth.js');
const user = require('../models/user.js');
const room = require('../models/room.js');
const participant = require('../models/participant.js');
const chatting = require('../models/chatting.js');
const friend = require('../models/friend.js');

module.exports = app => {
        app.post('/auth/signin', auth.signin);
        app.post('/auth/login', auth.login);
        
        app.get('/user/', user.find);
        app.get('/user/:uid', user.findBy);
        app.put('/user/:uid', user.update);
        app.delete('/user/:uid', user.remove);
	
        app.get('/room/:rid', room.find);
        app.delete('/room/:rid/:uid', room.remove);
	
        app.get('/participant/:rid', participant.find);
        app.get('/participant/:rid/:uid', participant.findBy);
        app.delete('participant/:rid/:uid', participant.remove);
	
        app.get('/chatting/:rid/:time', chatting.find);

        app.post('/friend/:uid/:fuid', friend.create);
        app.get('/friend/:uid', friend.findBy);
        app.delete('/friend/:uid/:fuid', friend.remove);
}

