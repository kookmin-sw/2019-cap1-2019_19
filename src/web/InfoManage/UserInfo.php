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
    $id = $_GET['id'];
    
    $sql="select ip, i_ip, m_ip, pw from Login where id='${id}'";
    
    $result=mysqli_query($link,$sql);
    $data = array();
    
    // 정상적으로 SQL문이 작동했는지 검사
    if($result){
        while($row=mysqli_fetch_array($result)){
            array_push($data,
                       array('ip'=>$row[0],
                             'i_ip'=>$row[1],
                             'm_ip'=>$row[2],
                             'cur_pw'=>$row[3]
                             ));
        }
        
        header('Content-Type: application/json; charset=utf8');
        
        // 데이터가 있는 경우만 JSON 데이터 반환
        if(!empty( $data )){
            $json = json_encode(array("info"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
        
    }
    else{
        echo "SQL문 처리중 에러 발생 : ";
        echo mysqli_error($link);
    }
    mysqli_close($link);
    
    ?>
