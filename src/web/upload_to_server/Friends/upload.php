<form enctype="multipart/form-data" method="post">
<input type="hidden" name="MAX_FILE_SIZE" value="3000000"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<?php
    // ******* 얼굴 영상 받기 ******* //
    $temp_name = $_FILES['myFile']['name'];
    $decode_name = URLDecode($temp_name);
    // file_name_id[0] => file_name, file_name_id[1] => user_id
    $file_name_id = explode('.mp4' , $decode_name);
    $file_size = $_FILES['myFile']['size'];
    $file_type = $_FILES['myFile']['type'];
    $tmp_name = $_FILES['myFile']['tmp_name'];
    $location = "./FriendVideo/";
    
    if(move_uploaded_file($_FILES['myFile']['tmp_name'],"$location/${file_name_id[0]}".".mp4")){
        echo $_FILES['myFile']['name'];
    }else {
        echo $_FILES['myFile']['size'];
    }
    
    echo "error: ";
    print_r($_FILES);
    
    // ******** DB Insert ******* //
    $link=mysqli_connect("localhost","admin","Kookmin1!","db");
    if (!$link)
    {
        echo "MySQL 접속 에러 : ";
        echo mysqli_connect_error();
        exit();
    }
    
    mysqli_set_charset($link,"utf8");
    
    $sql="INSERT INTO Acquaintance (id, name) VALUES ('${file_name_id[1]}', '${file_name_id[0]}')";
    mysqli_query($link,$sql);
    
?>
