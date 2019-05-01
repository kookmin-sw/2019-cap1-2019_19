<?php
    $con = mysqli_connect('localhost', 'app', 'Kookmin1!', 'db');
     //안드로이드 앱으로부터 아래 값들을 받음
     $id = $_POST['id'];
     $pw = $_POST['pw'];
     $tel = $_POST['tel'];
     $ip = NULL;
     $m_ip = NULL;
     //insert 쿼리문을 실행함
     $statement = mysqli_prepare($con, "INSERT INTO Login VALUES (?, ?, ?, ?, ?)");
     mysqli_stmt_bind_param($statement, "sssss", $id, $pw, $ip, $tel, $m_ip);
     mysqli_stmt_execute($statement);
     $response = array();
     $response["success"] = true;
     //회원 가입 성공을 알려주기 위한 부분임
     echo json_encode($response);
?>