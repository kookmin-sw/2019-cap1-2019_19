<?php
    $result = array();
    $link=mysqli_connect("localhost","admin","Kookmin1!", "db" );
    if (!$link)
    {
        echo "MySQL 접속 에러 : ";
        echo mysqli_connect_error();
        exit();
    }
    mysqli_set_charset($link,"utf8");
    $id = $_GET['id'];
    $selected_date = $_GET['selected_date'];
    $sql="SELECT COUNT(*) FROM History WHERE id='${id}' AND DATE_FORMAT(rDate, '%Y-%m-%d') = '${selected_date}'";
    $count=mysqli_query($link,$sql);
    $row=mysqli_fetch_array($count);
    $row_result=$row[0];
    $result["count"]=$row_result;
    echo json_encode($result);
    mysqli_close($link);
    ?>
