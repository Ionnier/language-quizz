const express = require('express')
const router = express.Router()
const connection = require('../database/connection')

router.post('/', express.json(), async (req, res) => {
    try {
        resp = await connection.promise_query('SELECT * from Users where email=? and password=?', [req.body.email, req.body.password])
        if (resp.length == 0) {
            throw Error("Nu s-a gasit")
        }
        console.log(resp.length)
        res.json(resp[0])
    } catch {
        res.status(400).json({ ok: false });
    }
})

module.exports = router