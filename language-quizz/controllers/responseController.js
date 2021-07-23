const connection = require('../database/connection')
exports.checkCorrect = async function (answer) {
    return new Promise(async (resolve, reject) => {
        new_answer = await exports.checkAnswerExists(answer)
        if (new_answer.length == 0) {
            answer2 = await exports.addAnswer(answer)
            answer3 = [answer2]
            resolve(answer3)
        } else {
            console.log(new_answer)
            resolve(new_answer)
        }
    })
}
exports.checkAnswerExists = function (answer) {
    return new Promise((resolve, reject) => {
        connection.query('select `id_response`,`id_exercise`,`content`,`percentage`,percentage*(select mark from `Exercises` where `id_exercise`=resp.id_exercise) as `grade` from `Responses` resp where `id_exercise`=? and `content` = ?', [answer.id_exercise, answer.content], (error, results, fields) => {
            if (error) reject(error)
            resolve(results)
        })
    })
}
exports.addAnswer = function (answer) {
    return new Promise((resolve, reject) => {
        connection.query('insert into `Responses`(`id_exercise`,`content`) values(?,?)', [answer.id_exercise, answer.content], (error, results, fields) => {
            if (error) reject(error)
            answer.id_response = results.insertId;
            resolve(answer)
        })
    })
}