const express = require('express')
const router = express.Router()
const connection = require('../database/connection')

router.get('/:id', async (req, res) => {
    problemId = req.params.id;
    limit = parseInt(req.query.limit) || 5;
    resp = await connection.promise_query('SELECT * FROM Problems join Categories using (id_category) join Lessons using (id_lesson) where id_problem = ?', [problemId])
    resp = resp
    resp[0].exerciseList = await connection.promise_query('SELECT * FROM Exercises where id_problem = ? ORDER BY RAND() LIMIT ?', [problemId, limit])
    res.json(resp)
})

router.get('/', async (req, res) => {
    resp = await connection.promise_query('SELECT * FROM Problems join Categories using (id_category) join Lessons using (id_lesson)')
    res.json(resp)
})

module.exports = router