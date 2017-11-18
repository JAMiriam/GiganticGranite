<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/vendor/autoload.php';

    use GuzzleHttp\Client;

    $target_dir = sys_get_temp_dir(). "/giganticgranite_data/";
    if (!file_exists($target_dir)) {
        mkdir($target_dir);
    }
    $uniqid = uniqid("", true);
    $file_name = 'img' . $uniqid;
    $target_file = $target_dir . $file_name;
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
        echo "File exists";
        $upload_ok = 0;
    }

    if ($upload_ok == 0) {
        echo "Sorry, dunno what happened here";
    } else {
        if (move_uploaded_file($_FILES["imageToUpload"]["tmp_name"], $target_file)) {
            # TODO send request to main server so it can do its machine learning magic
            # TODO save json and modify image
            $client = new Client();
            $response = $client->request('POST', '127.0.0.1:5000/actors/image', [
                'multipart' => [
                    [
                        'name'      => 'image',
                        'contents'   => fopen($target_file, 'r')
                    ]
                ]
            ]);
            # check response (response code)?
            $json_path = $target_dir . 'bjson' . $uniqid;
            file_put_contents($json_path, $response->getBody());

            $imagick = new Imagick();
            $imagick->readImage($target_file);
            $draw = new \ImagickDraw();

            # test rectangle
            $strokeColor = new \ImagickPixel('black');
            $draw->setStrokeColor($strokeColor);
            $draw->setStrokeOpacity(1);
            $draw->setStrokeWidth(2);
            $draw->setFillOpacity(0);
            $draw->rectangle(200, 200, 400, 400);
            $draw->setFontSize(30);
            $draw->annotation(300, 440, 'Alan Rickman');
            $imagick->drawImage($draw);
            echo $imagick->writeImage();

            $http = new HTTP2();
            $http->redirect('/info/info.php?id=' . $uniqid);
        } else {
            echo "Couldn't move file";
        }
    }
?>