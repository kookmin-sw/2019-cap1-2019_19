<?php
    $con = mysqli_connect('localhost', 'app', 'Kookmin1!', 'db');
    //안드로이드 앱으로부터 아래 값들을 받음
    $id = $_POST["id"];
    $pw = $_POST["pw"];
    $new_token = $_POST["token"];
    
    $statement = mysqli_prepare($con, "SELECT id FROM Login WHERE id = ? AND pw = ?");
    mysqli_stmt_bind_param($statement, "ss", $id, $pw);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id);
    $response = array();
    $response["success"] = false;
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["id"] = $id;
    }
    
    $old_token = mysqli_query($con, "SELECT token FROM Login WHERE id = '$id'");
    
    //토큰이 다를 경우 업데이트
    if($old_token != $new_token){
        $res = mysqli_query($con, "UPDATE Login SET token = '$new_token' WHERE id = '$id'");
    }
    
    echo json_encode($response);
    ?>
