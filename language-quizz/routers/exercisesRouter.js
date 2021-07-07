const express = require('express')
const router = express.Router()
const connection = require('../database/connection')
router.get('/', function (req, res) {
    connection.query('SELECT * from Exercises', (error, results, fields) => {
        if (error) throw error
        res.json(results)
    })
})

router.get('/:id', function (req, res) {
    connection.query('SELECT * from Exercises', (error, results, fields) => {
        if (error) throw error
        res.json(results)
    })
})

module.exports = router