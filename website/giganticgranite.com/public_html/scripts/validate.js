function form_submit() {
    var pass1 = document.getElementById('inputPassword').value;
    var pass2 = document.getElementById('inputPasswordRepeat').value;

    console.log(pass1);
    console.log(pass2);

    if (pass1 === pass2) {
        return true;
    }
    var div = document.getElementById("divPasswordRepeat");
    var span = document.createElement('span');
    span.className += ' help-block';
    span.innerHTML = "Passwords don't match!";
    div.appendChild(span);
    return false;
}