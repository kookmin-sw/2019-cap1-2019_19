<?php
    
    $link=mysqli_connect("localhost","admin","Kookmin1!", "db" );
    if (!$link)
    {
        echo "MySQL 접속 에러 : ";
        echo mysqli_connect_error();
        exit();
    }
    
    mysqli_set_charset($link,"utf8");
    
    $sql="select aIdx, name, belong from Acquaintance";
    $result=mysqli_query($link,$sql);
    $data = array();
    
    // 정상적으로 SQL문이 작동했는지 검사
    if($result){
        while($row=mysqli_fetch_array($result)){
            array_push($data,
                       array('aIdx'=>$row[0],'name'=>$row[1],
                             'belong'=>$row[2]
                             ));
        }
        
        header('Content-Type: application/json; charset=utf8');
        
        if(!empty( $data )){
            $json = json_encode(array("Acquaintances"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
    else{
        echo "SQL문 처리중 에러 발생 : ";
        echo mysqli_error($link);
    }
    mysqli_close($link);
    ?>
