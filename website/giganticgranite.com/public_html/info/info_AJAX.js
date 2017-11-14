// downloads additional details in background
function downloadDetails() {
    var id = window.location.search.substring(4)
    var xhr = new XMLHttpRequest()
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("additional_info").innerHTML = this.responseText
        }
    };
    xhr.open("GET", "ajax.php?id=" + id)
    xhr.send()
}