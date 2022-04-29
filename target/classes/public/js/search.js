const get = async ()=>{
    console.log('e')
    const options = {
        method: "POST"
    };
    let response = await fetch("/json", options)
    if (!response.ok)
        return response.status
    else
        return JSON.stringify(await response.json()) // response.json
}

const generateCars=async()=>{
    const response = await fetch("/generatecars", {method:"POST"})
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
    table.style.gridTemplateColumns = `0.5fr 2fr 0.5fr 1fr 1fr 1fr 1fr 1fr 1fr`;
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
        createEl(e.date,i+1)
        createEl(e.price, i+1)
        createEl(e.vat,i+1)

    }
}

const generateAllCarsInvoice = async () =>{
    const response = await fetch("/generateinvoice", {method:"POST"})
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
}

const generateYearInvoice = async () =>{
    const body = JSON.stringify({
        year: document.getElementById('year').value
    })
    const response = await fetch("/generateyearinvoice", {method:"POST", body})
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
}

const generateCostInvoice = async () => {
    const body = JSON.stringify({
        bottom: Number(document.getElementById('costBot').value),
        top: Number(document.getElementById('costTop').value)
    })
    const response = await fetch("/generatepriceinvoice", {method:"POST", body})
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
}

window.addEventListener('DOMContentLoaded', ()=>{
    generateTable()
})