<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/../vendor/autoload.php';

    session_start();    
        
    function signed_in() {
        return isset($_SESSION['signed_in']) && isset($_SESSION['username']);
    }

    function sign_in($username, $password) {
        $con = db_connect();
        $sql = "SELECT user_id, password_hash FROM users WHERE username LIKE '" . $username . "';";
        $result = $con->query($sql);
        if ($result->num_rows == 1) {
            $row = $result->fetch_assoc();
            if (password_verify($password, $row['password_hash'])) {
                $_SESSION['signed_in'] = 1;
                $_SESSION['username'] = $username;
                $_SESSION['id'] = $row['user_id'];
                
                $http = new HTTP2();
                $http->redirect('/');
            } else {
                echo 'Wrong password';
            }
        } else {
            echo 'Wrong username';
        }
    }

    function sign_up($email, $username, $password) {
        $con = db_connect();
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