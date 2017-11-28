<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
    
    $uploaddir = sys_get_temp_dir() . '/gg-data/';
    
    $id = $_GET['id'];
    $actors_json;
    if (signed_in()) {
        $actors_json = get_actors($id);
    } else {
        $real_id = preg_replace('/\\.[^.\\s]{3,4}$/', '', $id);
        $actors_json = file_get_contents($uploaddir . $real_id . '.json');
    }
    
    // echo $actors_json;
    $actors = json_decode($actors_json);
    $details = '';
    $client = new Client();
    foreach ($actors as $actor) {
        $imdb = $actor->{'imdb'};
        $response = $client->request('GET', '127.0.0.1:5000/actordetails/' . $imdb);
        $details .= $response->getBody();
    }
    
    echo $details;
    