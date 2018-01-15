<?php

    require_once 'utils.php';
    
    $http = new HTTP2();

    if (isset($_GET['u']) && isset($_GET['t'])) {
        $_SESSION['signed_in'] = 1;
        $_SESSION['username'] = $_GET['u'];
        $_SESSION['token'] = $_GET['t'];
        $http->redirect("\history.php");
    } else {
        $http->redirect("\index.php");
    }
    