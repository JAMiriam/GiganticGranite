<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/../vendor/autoload.php';
    
    use GuzzleHttp\Client;

    session_start();    
        
    function signed_in() {
        return isset($_SESSION['signed_in']) && isset($_SESSION['username']);
    }

    function sign_in($username, $password) {
        $http = new HTTP2();
        $client = new Client();
        $response = $client->request(
            'POST',
            '127.0.0.1:5000/login',
                [
                    'form_params' => [
                        'username' => $username,
                        'password' => $password
                    ]
                ]
        );
        $json = json_decode($response->getBody());
        if ($json->data == "Wrong username") {
            $http->redirect("\index.php?error=1");
        } else if ($json->data == "Wrong password") {
            $http->redirect("\index.php?error=2");
        } else {
            // TODO read data from server (whatever it is)
            $_SESSION['signed_in'] = 1;
            $_SESSION['username'] = $username;
            $http->redirect("\index.php");
        }
        die($response->getBody());
    }

    function sign_up($username, $password) {
        $http = new HTTP2();
        $client = new Client();
        $response = $client->request(
            'POST',
            '127.0.0.1:5000/register',
            [
                'form_params' => [
                    'username' => $username,
                    'password' => $password
                ]
        ]);
        $json = json_decode($response->getBody());
        if ($json->data == "username is unavailable") {
            return 1;
        } else {
            $_SESSION['signed_in'] = 1;
            $_SESSION['username'] = $username;
            $http->redirect("/");
        }
    }
    
    function encode_image($path) {
        $type = pathinfo($path, PATHINFO_EXTENSION);
        $data = file_get_contents($path);
        $src = 'data:image/' . $type . ';base64,' . base64_encode($data);
        return $src;
    }
//          old database methods
//          
//    function insert_image($image_path, $actors) {
//        $con = db_connect();
//        $image_path = $con->real_escape_string($image_path);
//        $sql = "INSERT INTO images (user_id, image_path, actors) VALUES (" . $_SESSION['id'] . ", '" . $image_path . "', '" . $actors . "');";
//        $con->query($sql);
//        return $con->insert_id;
//    }
//    
//    function get_image($id) {
//        $con = db_connect();
//        $sql = "SELECT image_path FROM images WHERE image_id = " . $id . ";";
//        $result = $con->query($sql);
//        $row = $result->fetch_assoc();
//        $path = $row['image_path'];
//        return encode_image($path);
//    }
//    
//    function get_actors($id) {
//        $con = db_connect();
//        $sql = "SELECT actors FROM images WHERE image_id = " . $id . ";";
//        $result = $con->query($sql);
//        $row = $result->fetch_assoc();
//        return $row['actors'];
//    }
//
//    function db_connect() {
//        $host = 'p:localhost';
//        $port = 3306;
//        $socket = '';
//        $user = 'web';
//        $dbpassword = 'qwfJMCV7658FDJHFG';
//        $dbname = 'giganticgranite';
//
//        return new mysqli($host, $user, $dbpassword, $dbname, $port, $socket);
//    }
?>