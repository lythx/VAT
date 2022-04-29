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

const generateVat = async(i)=>{
    const body = document.querySelectorAll(`.row${i}`)[1].innerHTML
    const options = {
        method: "POST",
        body
    };
    let response = await fetch("/invoice", options)
    if (!response.ok)
        return response.status
    else{
        await response
        generateTable()
    }
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
        createEl(`<div class="bt" onclick="generateVat(${i+1})">generuj fakturę VAT</div>`,i+1)
        createEl(e.invoice?`<a href="/invoices?uuid=${e.uuid}">Pobierz fakturę vat</a>`:"",i+1)
    }
}

generateTable()