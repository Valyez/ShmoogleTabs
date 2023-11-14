sendRequest('GET', 'http://localhost:8080/json')//.then(data => constructTable(data))

async function sendRequest(method, url, body = null) {
    const response = await fetch(url)
    const data = await response.json()
    constructTable(data)
}

function constructTable(data) {
    document.getElementById("table").innerHTML = `<table class ="tab1"></table>`;
    for (const row of data.table) {
        let tr = document.createElement('tr')
        for (const cell of row) {
            let td = document.createElement('td');
            td.id = cell.formula
            td.textContent = cell.integer

            tr.appendChild(td);
        }
        document.getElementById("table").appendChild(tr)
    }
}
