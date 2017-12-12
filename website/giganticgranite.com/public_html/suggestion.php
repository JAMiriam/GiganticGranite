<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
    
    $uploaddir = sys_get_temp_dir() . '/gg-data/';
    
    $id = $_GET['id'];
    $name = $_GET['name'];
    
    $client = new Client();
    $response = $client->request(
        'POST',
        '156.17.227.136:5000/actors/suggestion',
        [
            'multipart' => [
                [
                    'name'      => 'image',
                    'contents'  => fopen($uploaddir . $id . '.o', 'r')
                ],
                [
                    'name'      => 'complaint',
                    'contents'  => 'Not implemented yet!'
                ]
            ]
        ]
    );
    