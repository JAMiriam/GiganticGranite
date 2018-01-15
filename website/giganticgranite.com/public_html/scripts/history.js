$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "load_history.php"
    }).done(function (result) {
        $("#loadingDiv").hide();
        let str = '<div class="jumbotron"><div class="container" id="ajax-container"><ul>';

        console.log(result);
        result = JSON.parse(result);
        let arrayLength = result.length;
        for (var i = 0; i < arrayLength; i++) {
            str += '<li><img src="' + result[i].image + '"></img>' + result[i].date + '<ul>';
            let actors = JSON.parse(result[i].foundActors);
            let arrayLength2 = actors.length;
            for (var j = 0; j < arrayLength2; j++) {
                str += '<li>' + actors[j] + '</li>';
            }
            str += '</ul></li>';
        }
        str += '</ul></div></div>';
        $("#content").html(str);
        console.log(result);
    });
});
