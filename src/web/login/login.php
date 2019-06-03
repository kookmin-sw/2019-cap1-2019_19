<?php
    // db 연결
    include "./connect.php";

    // session 생성
    session_start();

    // 입력받은 id, pw
    $id = $_POST['id'];
    $pw = $_POST['pw'];

    $query="SELECT * FROM Login WHERE id='$id' and pw='$pw'";
    $result = mysqli_query($conn,$query);
    $row = mysqli_fetch_array($result);

    if($id == $row['id'] && $pw == $row['pw']){
        $_SESSION["is_logged"] = "Y";
        $_SESSION["id"] = $row['id'];
        $_SESSION["ip"] = $row["ip"];
        echo("<script>location.replace('../main/main.php');</script>");
    }else{
        echo("<script>location.replace('./re_login.php');</script>");
    }
?>