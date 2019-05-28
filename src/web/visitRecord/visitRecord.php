<?php
    
    $link=mysqli_connect("localhost","admin","Kookmin1!", "db" );
    if (!$link)
    {
        echo "MySQL 접속 에러 : ";
        echo mysqli_connect_error();
        exit();
    }
    
    mysqli_set_charset($link,"utf8");
    
    // 카테고리 값 get
    $category = $_GET['category']    ;
    // id 값 get
    $id = $_GET['id'];
    
    if($category == "전체보기"){
        $sql="SELECT rIdx, name, rDate, belong FROM History WHERE id='${id}' ORDER BY rIdx DESC";
    }else{
        $sql="SELECT rIdx, name, rDate, belong FROM History WHERE id='${id}' AND belong='${category}' ORDER BY rIdx DESC";
    }
    $result=mysqli_query($link,$sql);
    $data = array();
    
    // 정상적으로 SQL문이 작동했는지 검사
    if($result){
        while($row=mysqli_fetch_array($result)){
            array_push($data,
                       array('rIdx'=>$row[0],'name'=>$row[1],
                             'date'=>$row[2],
                             'belong'=>$row[3]
                             ));
        }
        
        header('Content-Type: application/json; charset=utf8');
        
        if(!empty( $data )){
            $json = json_encode(array("records"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
    else{
        echo "SQL문 처리중 에러 발생 : ";
        echo mysqli_error($link);
    }
    mysqli_close($link);
    ?>
