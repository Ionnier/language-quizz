const express = require('express')
const router = express.Router()
const connection = require('../database/connection')
router.get('/', async (req, res) => {
    res.render('index')
})
router.get('/lessons', async (req, res) => {
    lessons = await connection.promise_query('SELECT id_lesson,lesson_name,lesson_description FROM Lessons')
    console.log(lessons);
    res.render('lessons', { lessons })
})
router.get('/lesson/:id', async (req, res) => {
    lesson = await connection.promise_query('SELECT lesson_name,lesson_description from Lessons where id_lesson=?', [req.params.id])
    problems = await connection.promise_query('SELECT id_problem,problem_text from Problems where id_lesson=?', [req.params.id])
    console.log(lesson);
    res.render('lesson', { title: lesson[0].lesson_name, description: lesson[0].lesson_description, problems });
})
router.get('/problem/:id', async (req, res) => {
    problemId = req.params.id;
    limit = parseInt(req.query.limit) || 5;
    resp = await connection.promise_query('SELECT * FROM Problems join Categories using (id_category) join Lessons using (id_lesson) where id_problem = ?', [problemId])
    resp = resp
    resp[0].exerciseList = await connection.promise_query('SELECT id_exercise,id_category,id_problem,content FROM Exercises join Problems using(id_problem) where id_problem = ? ORDER BY RAND() LIMIT ?', [problemId, limit])
    console.log(resp[0].exerciseList)
    res.render('problem', { title: resp[0].lesson_name, description: resp[0].problem_text, exercises: resp[0].exerciseList })
})
router.get('/tests', async (req, res) => {
    if (!isNaN(parseInt(req.query.id_user))) {
        tests = await connection.promise_query('select id_test,test_name,expiration_date,restricted from Tests \
                                            left join TestParticipants using(id_test) \
                                            where restricted=false or id_user=?', [parseInt(req.query.id_user)])
    } else {
        tests = await connection.promise_query('select id_test,test_name,expiration_date,restricted from Tests \
                                            left join TestParticipants using(id_test) \
                                            where restricted=false')
    }
    res.render('tests', { tests })
})
router.get('/tests/:id', async (req, res) => {
    let name = await connection.promise_query('SELECT * FROM Tests where id_test=?', [parseInt(req.params.id)])
    let test = await connection.promise_query('SELECT * FROM TestContents where id_test=? order by RAND()', [parseInt(req.params.id)]);
    let exercitii_test = [];
    for (let i = 0; i < test.length; i++) {
        exercitii_test.push(JSON.parse(JSON.stringify(await connection.promise_query('SELECT * FROM Problems where id_problem=?', [parseInt(test[i].id_problem)])))[0])
        exercitii_test[i].exerciseList = await connection.promise_query('SELECT id_exercise,content,id_category FROM Exercises join Problems using(id_problem) where id_problem=? order by RAND() limit ?', [parseInt(test[i].id_problem), parseInt(test[i].number_of_exercises)])
    }
    res.render('test', { title: name[0].test_name, problems: exercitii_test })
})
router.get('/test/random', async (req, res) => {
    limit = req.params.limit || 5
    problems = await connection.promise_query('SELECT * FROM Problems order by RAND() limit ?', [parseInt(limit)])
    for (let i = 0; i < problems.length; i++) {
        problems[i].exerciseList = await connection.promise_query('SELECT id_exercise,content,id_category from Exercises join Problems using(id_problem) where id_problem=? order by RAND() limit ?', [problems[i].id_problem, 5])
    }
    res.render('test', { title: "Random test", problems })
})
module.exports = router