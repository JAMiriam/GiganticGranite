<?php
    require_once $_SERVER['DOCUMENT_ROOT'] . '/vendor/autoload.php';

    $http = new HTTP2();
    $http->redirect('upload/upload.html');
?>
