<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/../vendor/autoload.php';
    session_start();
    session_destroy();

    $http = new HTTP2();
    $http->redirect('/');
    