<?php
    require_once 'utils.php';
    
    $src;
    
//    if (isset($_SESSION['signed_in'])) {
//        $src = get_image($_GET['id']);
//    } else {
        $uploaddir = sys_get_temp_dir() . '/gg-data/';
        $src = encode_image($uploaddir . $_GET['id']);
        $image_size = getimagesize($uploaddir . $_GET['id'] . '.o');
        $variables = 'var width = ' . $image_size[0] . ';var height = ' . $image_size[1] . ';';
        $suggestions = $_SESSION['suggestions'];
        if (count($suggestions) > 0) {
            $variables .= 'var coords = [';
            foreach ($suggestions as $s) {
                $variables .= '[' . $s->top . ',' . $s->left . ',' . $s->right . ',' . $s->bottom . '],';
            }
            $variables = substr($variables, 0, -1);
            $variables .= '];';
        }
//    }
?>
