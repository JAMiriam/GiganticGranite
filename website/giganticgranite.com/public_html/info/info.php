<?php require('info_logic.php') ?>

<!DOCTYPE html>
<html>
    <head>
        <title>Gigantic Granite</title>
    </head>

    <body>
        <h1><?php echo $page_data->imdb ?></h1>
        <h1><?php echo $page_data->name ?></h1>
        <image src="<?php echo $page_data->image_src ?>"></image>

        <script src="info_AJAX.js"></script>
    </body>
</html>