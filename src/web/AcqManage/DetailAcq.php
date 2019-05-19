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
    
    $sql="SELECT name, belong, alarm FROM Acquaintance WHERE aIdx='${aIdx}'";
    
    $result=mysqli_query($link,$sql);
    $data = array();
    
    // 정상적으로 SQL문이 작동했는지 검사
    if($result){
        while($row=mysqli_fetch_array($result)){
            array_push($data,
                       array('name'=>$row[0],
                             'belong'=>$row[1],
                             'alarm'=>$row[2]
                             ));
        }
        
        header('Content-Type: application/json; charset=utf8');
        
        // 데이터가 있는 경우만 JSON 데이터 반환
        if(!empty( $data )){
            $json = json_encode(array("Acquaintance"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
    else{
        echo "SQL문 처리중 에러 발생 : ";
        echo mysqli_error($link);
    }
    mysqli_close($link);
    
    ?>
