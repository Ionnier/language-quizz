const connection = require('../database/connection')

exports.AddAnswer = async function (test, user, answer) {
    return new Promise(async (resolve, reject) => {
        console.log(test, user, answer)
        response = await connection.promise_query('insert into TestResponses(`id_test`,`id_response`,`id_user`) values(?,?,?)', [test, answer, user])
        resolve(response)
    })
}