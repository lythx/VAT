const get = async ()=>{
    const options = {
        method: "POST"
    };
    let response = await fetch("/json", options)
    if (!response.ok)
        return response.status
    else
        return JSON.stringify(await response.json()) // response.json
}

const updateCar=async(i)=>{
    const uuid = document.querySelectorAll(`.row${i}`)[1].innerHTML
    generatePrompt()
    const [model, year] = onclikcenter
    // const model = prompt('Podaj model')
    // const year = prompt('Podaj rok')
    const body = JSON.stringify({
        uuid,
        model,
        year
    })
    const options = {
        method: "POST",
        body
    };
    let response = await fetch("/update", options)
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
}

const generatePrompt = () => {

}

const deleteCar = async (i) =>{
    const body = document.querySelectorAll(`.row${i}`)[1].innerHTML
    const options = {
        method: "POST",
        body
    };
    let response = await fetch("/delete", options)
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
}

const generateTable = async () =>{
    const data = JSON.parse(await get())
    console.log(data)
    let table =  document.getElementById('table')
    table.innerHTML = '';
    const createEl = (ihtml,index,color) =>{
        let el = document.createElement('div')
        el.innerHTML = ihtml
        if(color)
            el.style.background = color
        el.classList.add(`row${index}`)
        table.appendChild(el)
    }
    for(const [i,e] of data.entries()){
        createEl(i+1,i+1)
        createEl(e.uuid,i+1)
        createEl(e.model,i+1)
        createEl(e.year,i+1)
        createEl(`${e.airbag[0].name}:${e.airbag[0].value}<br>
        ${e.airbag[1].name}:${e.airbag[1].value}<br>
        ${e.airbag[2].name}:${e.airbag[2].value}<br>
        ${e.airbag[3].name}:${e.airbag[3].value}`,i+1)
        createEl("",i+1,e.color)
        createEl(`<div class="bt" onclick="deleteCar(${i+1})">delete car</div>`,i+1)
        createEl(`<div class="bt" onclick="updateCar(${i+1})">update car</div>`,i+1)
    }
}

generateTable()