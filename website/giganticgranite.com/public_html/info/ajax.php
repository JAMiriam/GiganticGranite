<?php
    require_once 'GuzzleHttp/autoload.php';

    $target_dir = sys_get_temp_dir(). "/giganticgranite_data/";
    $uniqid = $_GET['id'];
    $bjson_path = $target_dir . 'bjson' . $uniqid;
    $bjson = file_get_contents($bjson_path);
    $basic_info = json_decode(file_get_contents($bjson_path));
    # TODO check ALL actors
    $basic_info = $basic_info[0];
    $imdb = $basic_info->imdb;
    $client = new GuzzleHTTP\Client();
    $response = $client->request('GET', '127.0.0.1:5000/actordetails/' . $imdb);

    # TODO format this data
    # test json
    echo $response->getBody();
?>