
const server='http://35.228.215.40:9093'

function login(url) {

    // fetch(`http://35.228.215.40:9093/auth/login`, {
    fetch(`http://localhost:8082/auth/login`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
                emailORPhone: '+998830242342',
                password: '+998830242342'
            }
        )
    }).then(response => response.json())
        .then(responseData => {
                console.log(responseData)
            }
        );


}

function changeStatus() {
    fetch(`http://35.228.215.40:9093/auth/changeStatus?code=12345`,{
        method : 'PATCH'
        }
    ).then(response => response.json())
        .then(responseData => {
                console.log(responseData)
            }
        );


}
// login()
// login('localhost:8082')
changeStatus()