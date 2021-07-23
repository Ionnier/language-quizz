const express = require('express')
const morgan = require('morgan')
const app = express()
const port = process.env.PORT || 3000
const exercisesRouter = require('./routers/exercisesRouter')
const responsesRouter = require('./routers/responsesRouter')
const problemsRouter = require('./routers/problemsRouter')
const lessonsRouter = require('./routers/lessonsRouter')
const testsRouter = require('./routers/testsRouter')
const loginRouter = require('./routers/loginRouter')
const viewsRouter = require('./routers/viewsRouter')
const connection = require('./database/connection')
app.set('view engine', 'pug')


app.use(morgan('combined'))
app.use('/api/v1/exercises/', exercisesRouter)
app.use('/api/v1/responses/', responsesRouter)
app.use('/api/v1/problems/', problemsRouter)
app.use('/api/v1/lessons/', lessonsRouter)
app.use('/api/v1/tests/', testsRouter)
app.use('/api/v1/login/', loginRouter)
app.use('/', viewsRouter)

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})
