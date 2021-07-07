const express = require('express')
const router = express.Router()
const controller = require('../controllers/responseController')
router.post('/', express.json(), async (req, res) => {
    answers = req.body
    reply = []
    for (const answer of answers) {
        correct = await controller.checkCorrect(answer)
        reply.push(correct[0])
    }
    res.json(reply)
})
module.exports = router