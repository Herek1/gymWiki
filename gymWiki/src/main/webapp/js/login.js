document.querySelector('form').addEventListener('submit', function(event) {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (username === '' || password === '') {
        document.getElementById('login-message').textContent = 'Proszę wypełnić wszystkie pola.';
        event.preventDefault(); 
    } else {
        document.getElementById('login-message').textContent = 'Logowanie zakończone powodzeniem.';
    }
});
