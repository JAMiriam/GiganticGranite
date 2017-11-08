<!DOCTYPE html>
<html>
    <head>
        <title>Gigantic Granite</title>
    </head>

    <body>
        <?php
        # TODO make it look decent
        # TODO read json and shot formated data
        # TODO? use AJAX to load additional info
        if (isset($_GET[img])){
            $image = $_GET['img'];
            $image_path = sys_get_temp_dir(). "/giganticgranite_data/" . $image;
            if (file_exists($image_path)) {
                echo '<img src="data:image/jpeg;base64,' . base64_encode(file_get_contents($image_path)) . '" />';
            } else {
                echo "corrupted link";
            }
        } else {
            echo "terrible link";
            die();
        }
        ?>
    </body>
</html>