<?php
    $con = mysqli_connect('localhost', 'app', 'Kookmin1!', 'db');
     //안드로이드 앱으로부터 아래 값들을 받음
    $id = $_POST["id"];
    $pw = $_POST["pw"];
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
    echo json_encode($response);
?>