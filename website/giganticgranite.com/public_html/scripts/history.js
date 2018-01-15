$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "load_history.php"
    }).done(function (result) {
        $("#loadingDiv").hide();
        let str = '<div class="jumbotron"><div class="container" id="ajax-container">';

        console.log(result);
        result = JSON.parse(result);
        let arrayLength = result.length;
        str += '<div class="row">';
        for (var i = arrayLength - 1; i >= 0; i--) {
            str += '<div class="col-md-4 text-center"><div><h3>' + result[i].date + '</h3></div><img src="' + result[i].image + '"></img>' + '<ul class="mylist">';
            let actors = JSON.parse(result[i].foundActors);
            let arrayLength2 = actors.length;
            for (var j = 0; j < arrayLength2; j++) {
                str += '<li>' + actors[j] + '</li>';
            }
            str += '</ul></div>';
        }
        str += '</div></div></div>';
        $("#content").html(str);
        console.log(result);
    });
});
