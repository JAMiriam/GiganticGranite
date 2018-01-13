<?php
    require_once 'utils.php';
    use GuzzleHttp\Client;

    $http = new HTTP2();

try {
    $uploaddir = sys_get_temp_dir() . '/gg-data';
    if (!file_exists($uploaddir)) {
        mkdir($uploaddir, 0777, true);
    }
    $uploaddir .= '/';

//    if ($_FILES['image']['error'] == UPLOAD_ERR_OK) {
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
            $originaldir = $uploaddir . '.o';
            if (move_uploaded_file(
                $_FILES['image']['tmp_name'],
                $uploaddir
            )) {
                file_put_contents($originaldir, file_get_contents($uploaddir));
                $client = new Client();
                $response;
                if (signed_in()) {
                    $response = $client->request(
                        'POST',
                        '156.17.227.136:5000/actors/image',
                        //'127.0.0.1:5000/actors/image',
                        [
                            'multipart' => [
                                [
                                    'name'      => 'image',
                                    'contents'  => fopen($uploaddir, 'r')
                                ],
                                [
                                    'name'      => 'token',
                                    'contents'  => $_SESSION['token']
                                ]
                            ]
                        ]
                    );
                } else {
                    $response = $client->request(
                        'POST',
                        '156.17.227.136:5000/actors/image',
                        //'127.0.0.1:5000/actors/image',
                        [
                            'multipart' => [
                                [
                                    'name'      => 'image',
                                    'contents'  => fopen($uploaddir, 'r')
                                ],
                                [
                                    'name'      => 'token',
                                    'contents'  => ''
                                ]
                            ]
                        ]
                    );
                }
                $json = $response->getBody();
                $actors = json_decode($json);
                $remove = array();
                $dup = array();
                $suggestions = array();
                $i = 0;
                $size = getimagesize($uploaddir);
                $scale = 1;
                $scalestring = '" ';
                if ($size[0] < 1000 && $size[1] < 1000) {
                    $scale = 2;
                    $scalestring = '" -resize 200%% -filter Lanczos "';
                    shell_exec('convert "' . $uploaddir . $scalestring . $uploaddir . '"');
                }
                foreach($actors as $actor) {
                    $name = $actor->name;
                    if($actor->reliability === "wrong") {
                        $i += 1;
                        $name = $i . " unrecognized";
                        array_push($suggestions, $actor);
                    }
                    $top = intval($actor->top) * $scale;
                    $left = intval($actor->left) * $scale;
                    $right = intval($actor->right) * $scale;
                    $bottom = intval($actor->bottom) * $scale;
                    $cmd = 'convert "' . $uploaddir . '" -fill none -stroke red -pointsize 22 -draw "rectangle ';
                    //die(var_dump($actor));
                    $cmd .= $top . ',' . $left . ' ' . $right . ',' . $bottom . 
                            '" -font Arial-Bold -fill red -draw "text ' . $top . ',' . ($bottom + 20) . 
                            ' \'' . $name . '\'" "' . $uploaddir . '"';
                    //die($cmd);
                    shell_exec($cmd);
                    if($actor->reliability === "wrong") {
                        $key = array_search($actor, $actors, TRUE);
                        array_push($remove, $key);
                    }
                    if($actor->reliability !== "wrong") {
                        if (!isset($dup[$actor->imdb])){
                            $dup[$actor->imdb] = 1;
                        } else {
                            $key = array_search($actor, $actors, TRUE);
                            array_push($remove, $key);
                        }
                    }
                }
                $_SESSION['suggestions'] = $suggestions;
                foreach($remove as $r) {
                    unset($actors[$r]);
                }
                $actors = array_values($actors);
                $json = json_encode($actors);
                //die(var_dump($remove));
                //shell_exec('convert "' . $uploaddir . '" ' . '-fill none -stroke red -pointsize 20 -draw "rectangle 50,50 200,200" -draw "text 220,100 \'test trest\'" ' . '"' . $uploaddir . '"');
                
                file_put_contents($tmpdir . $filename . '.json', $json);
                $filename .= '.' . $ext;
            $http->redirect('info.php?id=' . $filename);
        } else {
            $http->redirect('/error.php');
        }
    } else {
        $http->redirect('/error.php');
    }
} catch (Exception $e) {
    echo $e->getMessage();
//    $http->redirect('/error.php');
}
