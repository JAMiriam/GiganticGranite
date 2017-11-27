$(document).ready(function() {
    var id = window.location.search.substring(4);
    getActorDetails(id);
});

function getActorDetails(image_id) {
    $.ajax({
        type: "GET",
        url: "ajax.php",
        data: {
            id: image_id
        }
    }).done(function(result) {
        $("#ajax-container").html(result);
    });
}
