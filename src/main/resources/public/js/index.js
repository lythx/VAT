const add = async () => {
    const data = JSON.stringify({
        model:document.getElementById('model').value,
        year: document.getElementById('year').value,
        airbag: [
            {name:'kier', value:document.getElementById('kier').checked},
            {name:'pas', value:document.getElementById('pas').checked},
            {name:'tyl', value:document.getElementById('tyl').checked},
            {name:'bok', value:document.getElementById('bok').checked},
        ],
        color:document.getElementById('color').value
    })
    const options = {
        method: "POST",
        body: data,
    };
    let response = await fetch("/add", options)
    if (!response.ok)
        return response.status
    else
        alert(JSON.stringify(await response.json()), null, 4) // response.json
}
