<?php
    $target_dir = "../../uploads/";
    $target_file = $target_dir . basename($_FILES["imageToUpload"]["tmp_name"]);
    $upload_ok = 1;

    if (isset($_POST["submit"])) {
        $check = getimagesize($_FILES["imageToUpload"]["tmp_name"]);
        if ($check !== false) {
            $upload_ok = 1;
        } else {
            echo "File is not an image";
            $upload_ok = 0;
        }
    }

    if (file_exists($target_file)) {
        echo "Flie exists (wait for new version it will be fixed)";
        $upload_ok = 0;
    }

    if ($upload_ok == 0) {
        echo "Sorry, dunno what happened here";
    } else {
        if (move_uploaded_file($_FILES["imageToUpload"]["tmp_name"], $target_file)) {
            echo "success";
            # TODO send request to main server so it can do its machine learning magic
        } else {
            echo "Couldn't move file. Maybe check uploads folder owner (should be www-data)?";
        }
    }
?>