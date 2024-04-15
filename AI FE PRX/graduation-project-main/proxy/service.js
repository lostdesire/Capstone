const request = require('request-promise-native')
const torchEngine = require('./params')

const getMeme = async function (message){
    try{
        var options = {
            // uri : torchEngine,
            uri: 'http://ark10806.iptime.org:6006/meme',
            method: 'GET',
            body: JSON.stringify({'message': message}),
            headers: {'Content-Type': 'application/json'}
        }

        var result = await request(options)
        result = JSON.parse(result)
        console.log(result)
        return result
    } catch (error){
        // console.log(error)
    }
}

module.exports = getMeme