document.getElementById('dataForm').addEventListener('submit', function (e) {
    e.preventDefault();

    var data = {
        title: document.getElementById('title').value,
        text: document.getElementById('text').value
    };

    fetch('/admin/mail', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
});
