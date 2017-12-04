<?php
    require_once 'utils.php';
    
    $src;
    
//    if (isset($_SESSION['signed_in'])) {
//        $src = get_image($_GET['id']);
//    } else {
        $uploaddir = sys_get_temp_dir() . '/gg-data/';
        $src = encode_image($uploaddir . $_GET['id']);
//    }
?>
