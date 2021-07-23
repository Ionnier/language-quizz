function helloWorld() {
    let map = new Map()
    let inputs = document.getElementsByTagName('input');
    for (let index = 0; index < inputs.length; ++index) {
        if (map.has(inputs[index].name)) {
            map.set(inputs[index].name, map.get(inputs[index].name) + ";" + inputs[index].value)
        } else {
            map.set(inputs[index].name, inputs[index].value)
        }
    }
    var array = [];
    map.forEach((value, key) => {
        array.push({ id_exercise: key, content: value })
    })
    fetch("/api/v1/responses", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(JSON.parse(JSON.stringify(array)))
    }).then(res => res.json()).then(data => {
        let answers = data;
        for (let index = 0; index < answers.length; index++) {
            if (answers[index].percentage === null || answers[index].percentage === undefined) {
                let exista = document.getElementById("ans" + answers[index].id_exercise);
                if (exista) {
                    exista.innerHTML = "Not checked";
                } else {
                    var link = document.createElement("p");
                    link.setAttribute("id", "ans" + answers[index].id_exercise);
                    link.appendChild(document.createTextNode("Not checked"));
                    document.getElementById(answers[index].id_exercise).appendChild(link)
                }
            } else if (answers[index].percentage) {
                let exista = document.getElementById("ans" + answers[index].id_exercise);
                if (exista) {
                    exista.innerHTML = answers[index].percentage * 100 + "%";
                } else {
                    var link = document.createElement("p");
                    link.setAttribute("id", "ans" + answers[index].id_exercise);
                    link.appendChild(document.createTextNode(answers[index].percentage * 100 + "%"));
                    document.getElementById(answers[index].id_exercise).appendChild(link)
                }
            }
        }
    });
}