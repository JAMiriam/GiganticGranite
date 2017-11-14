<?php require('info_logic.php') ?>

<!DOCTYPE html>
<html>
    <head>
        <title>Gigantic Granite</title>
    </head>

    <body onload="downloadDetails()">
        <h1><?php echo $page_data->name ?></h1>
        <image src="<?php echo $page_data->image_src ?>"></image>

        <div id="additional_info"></div>

        <script src="info_AJAX.js"></script>
    </body>
</html>