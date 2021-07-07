const express = require('express')
const router = express.Router()
const connection = require('../database/connection')
const responseController = require('../controllers/responseController')
const controller = require('../controllers/testController')

router.get('/random', async (req, res) => {
    limit = req.params.limit || 5
    problems = await connection.promise_query('SELECT * FROM Problems order by RAND() limit ?', [parseInt(limit)])
    for (let i = 0; i < problems.length; i++) {
        problems[i].exercitii = await connection.promise_query('SELECT * from Exercises where id_problem=? order by RAND() limit ?', [problems[i].id_problem, 5])
    }
    res.json(problems)
})
router.get('/:id', async (req, res) => {
    test = await connection.promise_query('SELECT * FROM TestContents where id_test=? order by RAND()', [parseInt(req.params.id)]);
    exercitii_test = [];
    for (let i = 0; i < test.length; i++) {
        exercitii_test.push(JSON.parse(JSON.stringify(await connection.promise_query('SELECT * FROM Problems where id_problem=?', [parseInt(test[i].id_problem)])))[0])
        exercitii_test[i].exerciseList = await connection.promise_query('SELECT * FROM Exercises where id_problem=? order by RAND() limit ?', [parseInt(test[i].id_problem), parseInt(test[i].number_of_exercises)])
    }
    res.json(exercitii_test)
})

router.post('/:id', express.json(), async (req, res) => {
    test = await connection.promise_query('SELECT * FROM Tests where id_test=? and ((expiration_date is null) or (expiration_date>=?))', [parseInt(req.params.id), new Date().toISOString()]);
    test = test.map(v => Object.assign({}, v))
    if (test.length == 0) {
        res.status(403).json({ ok: false, message: "Timpul a expirat" })
    } else {
        reply = []
        for (let i = 0; i < req.body.responses.length; i++) {
            correct = await responseController.checkCorrect(req.body.responses[i])
            correct = Object.assign({}, correct[0])
            console.log(correct.id_response)
            await controller.AddAnswer(parseInt(req.params.id), req.body.id_user, correct.id_response)
            reply.push(correct)
        }
        res.json(correct)
    }
})

router.get('/', async (req, res) => {
    if (!isNaN(parseInt(req.query.id_user))) {
        tests = await connection.promise_query('select id_test,test_name,expiration_date,restricted from Tests \
                                            left join TestParticipants using(id_test) \
                                            where restricted=false or id_user=?', [parseInt(req.query.id_user)])
    } else {
        tests = await connection.promise_query('select id_test,test_name,expiration_date,restricted from Tests \
                                            left join TestParticipants using(id_test) \
                                            where restricted=false')
    }
    res.json(tests)

})
module.exports = router