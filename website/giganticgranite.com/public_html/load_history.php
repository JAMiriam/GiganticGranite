<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
    
    $client = new Client();
    
    $response = $client->request(
        'GET',
        '156.17.227.136:5000/get/history',
        //'127.0.0.1:5000/actors/image',
        [
            'query' => ['token' => $_SESSION['token']]
        ]
    );
    
    echo $response->getBody();
