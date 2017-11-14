<?php
    $page_data = (object)(array());
    if (isset($_GET['id'])){
        $target_dir = sys_get_temp_dir(). "/giganticgranite_data/";
        $id = $_GET['id'];
        $image_path = $target_dir . 'img' . $id;
        $bjson_path = $target_dir . 'bjson' . $id;
        if (file_exists($image_path) && file_exists($bjson_path)) {
            $page_data->image_src = 'data:image/jpeg;base64,' . base64_encode(file_get_contents($image_path));
            $basic_info = json_decode(file_get_contents($bjson_path));
            # TODO check ALL actors
            $basic_info = $basic_info[0];

            # temp
            $page_data->name = $basic_info->name;
        } else {
            echo "corrupted link";
        }
    } else {
        echo "terrible link";
        die();
    }
?>