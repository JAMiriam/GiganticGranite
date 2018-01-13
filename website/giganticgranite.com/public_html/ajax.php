<?php
    require_once 'utils.php';
    
    use GuzzleHttp\Client;
try {
    $uploaddir = sys_get_temp_dir() . '/gg-data/';
    
    $id = $_GET['id'];
    $actors_json;
//    if (signed_in()) {
//        $actors_json = get_actors($id);
//    } else {
        $real_id = preg_replace('/\\.[^.\\s]{3,4}$/', '', $id);
        $actors_json = file_get_contents($uploaddir . $real_id . '.json');
//    }
    
    // echo $actors_json;
    $actors = json_decode($actors_json);
    $details = '<div class="row">';
    $client = new Client();
    foreach ($actors as $actor) {
        $imdb = $actor->{'imdb'};
        $response = $client->request('GET', '156.17.227.136:5000/actordetails/' . $imdb);
        //$response = $client->request('GET', '127.0.0.1:5000/actordetails/' . $imdb);
        $body = $response->getBody();
        $json = json_decode($body)[0];
        $details .= '<div class="col-md-6"><img class="profile-image" src="' . 
                $json->{'images'}[0] . 
                '"></img><div style="position: relative;"><div class="main-info"><h3>' . $json->{'name'} . 
                '</h3><h4>Birthday:</h4><p><b>' . $json->{'birthday'} . 
                '</b></p>';
        if (isset($json->{'deathday'})) {
            $details .= '<h4>Deathday:</h4><p><b>' . $json->{'deathday'} . '</b></p>';
        }
        $details .= '</div><div style="clear: both"></div></div><div class="biography"><h4>Biography:</h4><p>' . 
                $json->{'biography'} . '</p></div>' . 
                '<div class="filmography"><h4>Filmography:</h4><ul>'; // . '</ul></div>';
        $i = 0;
        foreach ($json->{'movie_credits'} as $movie) {
            $i++;
            $details .= '<li>' . $movie->{'title'} . '</li>';
            if ($i >= 10) {
                break;
            }
        }
        $details .= '</ul></div></div>';
    }
    $details .= '</div>';
    echo $details;
} catch (Exception $e) {
    $http = new HTTP2();
    $http->redirect("/error2.php");
}
    