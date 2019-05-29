form enctype="multipart/form-data" method="post">
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
    //$ID = $_POST["id"];
   // mkdir($ID,0777,true);
    $location = './Users/';
    if(!is_dir($location.$file_name_id[1])){

    mkdir($location.$file_name_id[1],0777,true);
    }

if(move_uploaded_file($_FILES['myFile']['tmp_name'],"Users/$file_name_id[1]/${file_name_id[0]}".".mp4")){
}
    //echo "error: ";
    //print_r($_FILES);
 // $str = strcmp($file_name_id[0],"User");
$link=mysqli_connect("localhost","monitor","Kookmin1!","db");
 // if($str == 1){
//  echo $str;
if (!$link)
{
    echo "MySQL 접속 에러 : ";
    echo mysqli_connect_error();
    exit();
}

mysqli_set_charset($link,"utf8");
$sql="INSERT INTO Acquaintance (id,name,face) VALUES ('${file_name_id[1]}', '${file_name_id[0]}', '0')";
mysqli_query($link,$sql);

//$root_path ='./home/ubuntu/hey';
//mkdir("here",0777,true);
if(!is_dir("./face/$file_name_id[1]")){
mkdir("./face/$file_name_id[1]",0777,true);
chmod("./face/$file_name_id[1]",0777);
}
if(!is_dir("./face/$file_name_id[1]/$file_name_id[0]")){
mkdir("./face/$file_name_id[1]/$file_name_id[0]",0777,true);
//mkdir("./face/$file_name_id[1]/$file_name_id[0]",0777,true);
chmod("./face/$file_name_id[1]/$file_name_id[0]",0777);
}

$fp=fopen("./register/register_info.txt","w");
fwrite($fp,$file_name_id[1]);
//write id to file
fwrite($fp,"\n");

$index="SELECT aIdx FROM Acquaintance WHERE id = '$file_name_id[1]' AND name = '$file_name_id[0]'";
$result=mysqli_query($link,$index);
$data=mysqli_fetch_array($result);
//echo $index;
//echo ($result);
//echo $data[0];
fwrite($fp,$data[0]);
fclose($fp);

//$folder_name = "$file_name_id[1]/$file_name_id[0]";
$file_name ="./Users/$file_name_id[1]/$file_name_id[0]".".mp4";
//shell_exec("sh auto_face_detect.sh $file_name_id[1] $file_name_id[0]");
exec("python face_detect.py ./face/$file_name_id[1]/$file_name_id[0] $file_name");
$sql="UPDATE Acquaintance SET face=1 WHERE id = '$file_name_id[1]' AND name = '$file_name_id[0]'";
mysqli_query($link,$sql);
?>
