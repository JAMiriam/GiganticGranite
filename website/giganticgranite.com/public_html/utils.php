<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/../vendor/autoload.php';

    session_start();    
        
    function signed_in() {
        return isset($_SESSION['signed_in']) && isset($_SESSION['username']);
    }

    function sign_in($username, $password) {
        $con = db_connect();
        
        $username = $con->real_escape_string($username);
        $password = $con->real_escape_string($password);

        $sql = "SELECT user_id, password_hash FROM users WHERE username LIKE '" . $username . "';";
        $result = $con->query($sql);
        $http = new HTTP2();        
        if ($result->num_rows == 1) {
            $row = $result->fetch_assoc();
            if (password_verify($password, $row['password_hash'])) {
                $_SESSION['signed_in'] = 1;
                $_SESSION['username'] = $username;
                $_SESSION['id'] = $row['user_id'];
                
                $http->redirect('/');
            } else {
                $http->redirect('index.php?error=2');
            }
        } else {
            $http->redirect('index.php?error=1');
        }
    }

    function sign_up($email, $username, $password) {
        $con = db_connect();

        $email = $con->real_escape_string($email);
        $username = $con->real_escape_string($username);
        $password = $con->real_escape_string($password);
        
        $sql1 = "SELECT * FROM users WHERE email LIKE '" . $email . "';";
        $sql2 = "SELECT * FROM users WHERE username LIKE '" . $username . "';";
        $result = $con->query($sql1);
        if ($result->num_rows > 0) {
            return 1;
        }
        $result = $con->query($sql2);
        if ($result->num_rows > 0) {
            return 2;
        }
        $hash = password_hash($password, PASSWORD_DEFAULT);
        $sql3 = "INSERT INTO users (email, username, password_hash) VALUES ('" . $email . "', '" . $username . "', '" . $hash . "');";
        $result = $con->query($sql3); 
        if ($result) {
            return 0;
        } else {
            return 3;
        }
    }

    function insert_image($image_path, $actors) {
        $con = db_connect();
        $image_path = $con->real_escape_string($image_path);
        $sql = "INSERT INTO images (user_id, image_path, actors) VALUES (" . $_SESSION['id'] . ", '" . $image_path . "', '" . $actors . "');";
        $con->query($sql);
        return $con->insert_id;
    }
    
    function encode_image($path) {
        $type = pathinfo($path, PATHINFO_EXTENSION);
        $data = file_get_contents($path);
        $src = 'data:image/' . $type . ';base64,' . base64_encode($data);
        return $src;
    }
    
    function get_image($id) {
        $con = db_connect();
        $sql = "SELECT image_path FROM images WHERE image_id = " . $id . ";";
        $result = $con->query($sql);
        $row = $result->fetch_assoc();
        $path = $row['image_path'];
        return encode_image($path);
    }
    
    function get_actors($id) {
        $con = db_connect();
        $sql = "SELECT actors FROM images WHERE image_id = " . $id . ";";
        $result = $con->query($sql);
        $row = $result->fetch_assoc();
        return $row['actors'];
    }

    function db_connect() {
        $host = 'p:localhost';
        $port = 3306;
        $socket = '';
        $user = 'web';
        $dbpassword = 'qwfJMCV7658FDJHFG';
        $dbname = 'giganticgranite';

        return new mysqli($host, $user, $dbpassword, $dbname, $port, $socket);
    }
?>