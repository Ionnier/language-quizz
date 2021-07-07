const express = require('express')
const router = express.Router()
const connection = require('../database/connection')

router.get('/:id', async (req, res) => {
    lessonId = req.params.id;
    limit = parseInt(req.query.limit) || 5;
    resp = await connection.promise_query('SELECT * FROM Problems join Categories using (id_category) join Lessons using (id_lesson) where id_lesson = ?', [lessonId])
    res.json(resp)
})

router.get('/', async (req, res) => {
    resp = await connection.promise_query('SELECT * FROM Lessons ')
    res.json(resp)
})

module.exports = router