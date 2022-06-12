const auth = require('../models/auth.js');
const user = require('../models/user.js');
//const room = require('../models/room.js');
//const participant = require('../models/participant.js');
//const chatting = require('../models/chatting.js');
//const friend = require('../models/friend.js');

module.exports = app => {

        app.post('/auth/signin', auth.signin);
        app.post('/auth/login', auth.login);
        
        app.get('/user/', user.find);
        app.get('/user/:uid', user.findBy);
        app.post('/user', user.update);
        app.delete('/user/:uid', user.remove);
	/*
        app.get('/room/:rid', room.find);
        app.delete('/room/:rid', room.remove);

        app.get('/participant/:rid', participant.find);
        app.get('/participant/:rid/:uid', participant.findBy);
        app.remove('participant/:rid', participant.remove);

        app.get('/chatting/:rid/:uid/:time', chatting.find);

        app.post('/friend/:uid/:fuid', friend.create);
        app.get('/friend/:uid', friend.findBy);
        app.delete('/friend/:uid/:fuid', friend.remove);
        */
}

