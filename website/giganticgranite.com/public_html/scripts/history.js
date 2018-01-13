$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "load_history.php"
    }).done(function(result) {
       $("#loadingDiv").hide();
       let str = '<div class="jumbotron"><div class="container" id="ajax-container"><ul>';
       result = JSON.parse(result);
       let arrayLength = result.length;
       for (var i = 0; i < arrayLength; i++) {
           str += '<li>' + result[i].date + '<br>' + result[i].foundActors + '</li>';
       }
       str += '</ul></div></div>';
       $("#content").html(str);
       console.log(result);
    });
});
