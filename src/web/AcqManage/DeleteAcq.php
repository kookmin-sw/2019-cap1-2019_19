<?php
    
    $link=mysqli_connect("localhost","admin","Kookmin1!", "db" );
    if (!$link)
    {
        echo "MySQL 접속 에러 : ";
        echo mysqli_connect_error();
        exit();
    }
    
    mysqli_set_charset($link,"utf8");
    
    // get 방식으로 카테고리 파라미터 받기
    $s_aIdx = $_GET['aIdx'];
    $aIdx = (int)$s_aIdx;
    
    $sql="DELETE FROM Acquaintance WHERE aIdx='${aIdx}'";
    $result=mysqli_query($link,$sql);
    $data = array();
    
    // 정상적으로 SQL문이 작동했는지 검사
    if(!($result)){
        echo "SQL문 처리중 에러 발생 : ";
        echo mysqli_error($link);
        
    }
    
    mysqli_close($link);
    
    ?>
