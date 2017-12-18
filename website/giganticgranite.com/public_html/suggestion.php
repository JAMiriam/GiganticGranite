<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
    
    $uploaddir = sys_get_temp_dir() . '/gg-data/';
    
    $id = $_GET['id'];
    $name = $_GET['name'];
    $left = $_GET['left'];
    $right = $_GET['right'];
    $top = $_GET['top'];
    $bottom = $_GET['bottom'];
    $array = array(
        'name' => $name,
        'left' => $left,
        'right' => $right,
        'top' => $top,
        'bottom' => $bottom
    );
    
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
                    'contents'  => json_encode($array)
                ]
            ]
        ]
    );
    