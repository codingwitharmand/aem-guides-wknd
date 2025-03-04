let productDiv = document.querySelector('.product');

productDiv.addEventListener('click', () => {
    fetch('https://dogapi.dog/api/v2/facts?limit=5')
        .then(res => res.json())
        .then(data => {
            document.querySelector('.text-danger').innerText = data.attributes.body;
        })
})