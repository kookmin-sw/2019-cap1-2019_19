<form enctype="multipart/form-data" method="post">
<input type="hidden" name="MAX_FILE_SIZE" value="3000000"/>

<?php
    $temp_name = $_FILES['myFile']['name'];
    $file_name = URLDecode($temp_name);
    $file_size = $_FILES['myFile']['size'];
    $file_type = $_FILES['myFile']['type'];
    $tmp_name = $_FILES['myFile']['tmp_name'];
    $location = "./uploads/";
    
    if(move_uploaded_file($_FILES['myFile']['tmp_name'],"$location/${file_name}")){
        echo $_FILES['myFile']['name'];
    }else {
        echo $_FILES['myFile']['size'];
    }
    echo "error: ";
    print_r($_FILES);
    ?>
