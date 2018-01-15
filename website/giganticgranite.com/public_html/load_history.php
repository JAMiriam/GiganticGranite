<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
    
    $client = new Client(['verify' => false]);
    
    $response = $client->request(
        'GET',
        'https://156.17.227.136:5000/get/history',
        //'127.0.0.1:5000/actors/image',
        [
            'query' => ['token' => $_SESSION['token']]
        ]
    );
    
    $body = $response->getBody();
    $data = json_decode($body);
    
    $max = sizeof($data);
    for ($i = 0; $i < $max; $i ++) {
        $imgpath = $data[$i]->image;
        $res = $client->request(
            'GET',
            'https://156.17.227.136:5000/' . $imgpath
        );
//        echo $res->getBody();
//        die();
        $src = 'data:image/' . 'jpg' . ';base64,' . base64_encode($res->getBody());
        $data[$i]->image = $src;
    }
    
    echo json_encode($data);
