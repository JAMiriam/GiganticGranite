var chosen;

function imgClick(event) {
    var rect = $(".big-image")[0].getBoundingClientRect();
    var realwidth = rect.right - rect.left;
    var realheight = rect.bottom - rect.top;

    var scaleX = width / realwidth;
    var scaleY = height / realheight;
    
    var x = (event.clientX - rect.left) * scaleX;
    var y = (event.clientY - rect.top) * scaleY;
    
    for (i = 0; i < coords.length; i++) {
        if (x > coords[i][0] && x < coords[i][2] && y > coords[i][1] && y < coords[i][3]) {
            chosen = coords[i];
            let tmp = i + 1;
            $("#number").html(tmp);
            $("#myModal").modal();
            break;
        }
    }
}

function sendSuggestion() {
    let text = $("#actorNameInput").val();
    let image_id = window.location.search.substring(4);
    if (text !== '') {
        $.ajax({
            type: "GET",
            url: "suggestion.php",
            data: {
                id: image_id,
                name: text,
                left: chosen[0],
                top: chosen[1],
                right: chosen[2],
                bottom: chosen[3]
            }
        });
    }
    $("#actorNameInput").val("");
    chosen = null;
}
