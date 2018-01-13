var chosen;
var bigImage;
var number;
var myModal;
var actorNameInput;

var rect;
var realwidth;
var realheight;
var scaleX;
var scaleY;

$(document).ready(function() {
    bigImage = $(".big-image")[0];
    number = $("#number");
    myModal = $("#myModal");
    actorNameInput = $("#actorNameInput");

    rect = bigImage.getBoundingClientRect();
    realwidth = rect.right - rect.left;
    realheight = rect.bottom - rect.top;
    scaleX = width / realwidth;
    scaleY = height / realheight;
   
    $(window).resize(function() {
        rect = bigImage.getBoundingClientRect();
        realwidth = rect.right - rect.left;
        realheight = rect.bottom - rect.top;
        scaleX = width / realwidth;
        scaleY = height / realheight;
    });
});

function imgClick(event) {    
    let x = (event.clientX - rect.left) * scaleX;
    let y = (event.clientY - rect.top) * scaleY;
    
    for (i = 0; i < coords.length; i++) {
        if (x > coords[i][0] && x < coords[i][2] && y > coords[i][1] && y < coords[i][3]) {
            chosen = coords[i];
            let tmp = i + 1;
            number.html(tmp);
            myModal.modal();
            break;
        }
    }
}

function sendSuggestion() {
    let text = actorNameInput.val();
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
    actorNameInput.val("");
    chosen = null;
}
