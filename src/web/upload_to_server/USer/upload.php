

<form enctype="multipart/form-data" method="post">
<input type="hidden" name="MAX_FILE_SIZE" value="3000000"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<?php
    $file_name = $_FILES['myFile']['name'];
    $file_size = $_FILES['myFile']['size'];
    $file_type = $_FILES['myFile']['type'];
    $temp_name = $_FILES['myFile']['tmp_name'];
    $location = "./Users/";
    
    if(move_uploaded_file($_FILES['myFile']['tmp_name'],"$location/$file_name")$
       echo $_FILES['myFile']['name'];
       }else {
       echo $_FILES['myFile']['size'];
       }
       
       echo "      check this  :";
       print_r($_FILES);
    
