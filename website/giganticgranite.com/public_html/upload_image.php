<?php
    require_once 'utils.php';
    use GuzzleHttp\Client;

    $http = new HTTP2();

//    if (signed_in()) {
//        $uploaddir = '../';
//        $uploaddir = realpath($uploaddir);
//        $uploaddir .= '/uploads';
//        if (!file_exists($uploaddir)) {
//            mkdir($uploaddir, 0777, true);
//        }
//        $uploaddir .= '/';
//    } else {
        $uploaddir = sys_get_temp_dir() . '/gg-data';
        if (!file_exists($uploaddir)) {
            mkdir($uploaddir, 0777, true);
        }
        $uploaddir .= '/';
//    }

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
                    '156.17.227.136:5000/actors/image',
                    //'127.0.0.1:5000/actors/image',
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
                $remove = array();
                foreach($actors as $actor) {
                    $name = $actor->name;
                    if($actor->reliability === "wrong") {
                        $name = "unrecognized";
                    }
                    $top = intval($actor->top);
                    $left = intval($actor->left);
                    $right = intval($actor->right);
                    $bottom = intval($actor->bottom);
                    $cmd = 'convert "' . $uploaddir . '" ' . '-fill none -stroke red -pointsize 20 -draw "rectangle ';
                    //die(var_dump($actor));
                    $cmd .= $top . ',' . $left . ' ' . $right . ',' . $bottom . 
                            '" -draw "text ' . $top . ',' . ($bottom + 20) . 
                            ' \'' . $name . '\'" "' . $uploaddir . '"';
                    //die($cmd);
                    shell_exec($cmd);
                    if($actor->reliability === "wrong") {
                        $key = array_search($actor, $actors, TRUE);
                        array_push($remove, $key);
                    }
                }
                foreach($remove as $r) {
                    unset($actors[$r]);
                }
                $actors = array_values($actors);
                $json = json_encode($actors);
                //die(var_dump($remove));
                //shell_exec('convert "' . $uploaddir . '" ' . '-fill none -stroke red -pointsize 20 -draw "rectangle 50,50 200,200" -draw "text 220,100 \'test trest\'" ' . '"' . $uploaddir . '"');
                
//                if (signed_in()) {
//                    $filename = insert_image($uploaddir, $json);
//                } else {
                    file_put_contents($tmpdir . $filename . '.json', $json);
                    $filename .= '.' . $ext;
//                }
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