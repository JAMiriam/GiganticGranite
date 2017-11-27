<?php
    require_once 'utils.php';
    use GuzzleHttp\Client;

    $http = new HTTP2();

    if (signed_in()) {
        $uploaddir = '../';
        $uploaddir = realpath($uploaddir);
        $uploaddir .= '/uploads';
        if (!file_exists($uploaddir)) {
            mkdir($uploaddir, 0777, true);
        }
        $uploaddir .= '/';
    } else {
        $uploaddir = sys_get_temp_dir() . '/gg-data';
        if (!file_exists($uploaddir)) {
            mkdir($uploaddir, 0777, true);
        }
        $uploaddir .= '/';
    }

    if ($_FILES['image']['error'] == UPLOAD_ERR_OK) {
        $finfo = new finfo(FILEINFO_MIME_TYPE);
        if ($ext = array_search(
            $finfo->file($_FILES['image']['tmp_name']),
            array (
                'jpg' => 'image/jpeg',
                'png' => 'image/png',
                'gif' => 'image/gif'
            ),
            true
        )) {
            // choose better
            $filename = uniqid("", true);
            //$filename = sha1_file($_FILES['image']['tmp_name']);
            $tmpdir = $uploaddir;
            $uploaddir = $uploaddir . $filename . '.' . $ext;
            if (move_uploaded_file(
                $_FILES['image']['tmp_name'],
                $uploaddir
            )) {
                // DO something
                $client = new Client();
                $response = $client->request(
                    'POST',
                    '127.0.0.1:5000/actors/image',
                    [
                        'multipart' => [
                            [
                                'name'      => 'image',
                                'contents'  => fopen($uploaddir, 'r')
                            ]
                        ]
                    ]
                );
                $json = $response->getBody();
                $actors = json_decode($json);
                // TODO draw stuff onto image
                $imagick = new Imagick();
                $imagick->readImage($uploaddir);
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
                $imagick->writeImageFile(fopen($uploaddir, 'w'));
                if (signed_in()) {
                    $filename = insert_image($uploaddir, $json);
                } else {
                    file_put_contents($tmpdir . $filename . '.json', $json);
                    $filename .= '.' . $ext;
                }
                $http->redirect('info.php?id=' . $filename);
            } else {
                die('error!');
            }
        } else {
            $http->redirect('/');
        }
    } else {
        $http->redirect('/');
    }
?>