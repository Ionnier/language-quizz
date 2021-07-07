const mysql = require('mysql2');
const pool = mysql.createPool({
    connectionLimit: 10,
    host: 'db',
    user: 'root',
    password: 'example',
    database: 'lgquizz'
});
module.exports = pool

module.exports.promise_query = function (sql, params) {
    return new Promise((resolve, reject) => {
        pool.query(sql, params, (error, results) => {
            if (error) reject(error)
            resolve(results)
        })
    })

}