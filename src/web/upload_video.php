<?php
// 폴더 설정
$uploads_dir = './video';

// 변수 정리
$error = $_FILES['myfile']['error'];
$name = $_FILES['myfile']['name'];
$ext = array_pop(explode('.', $name));

// 오류 확인
if( $error != UPLOAD_ERR_OK ) {
        switch( $error ) {
                case UPLOAD_ERR_INI_SIZE:
                case UPLOAD_ERR_FORM_SIZE:
                        echo "전송실패 : 파일이 너무 큽니다. ($error)";
                        break;
                case UPLOAD_ERR_NO_FILE:
                        echo "전송실패 : 파일이 첨부되지 않았습니다. ($error)";
                        break;
                default:
                        echo "전송실패 : 파일이 제대로 업로드되지 않았습니다. ($error)";
        }
        exit;
}


// 파일 이동
move_uploaded_file( $_FILES['myfile']['tmp_name'], "$uploads_dir/$name");
echo "촬영종료";
// 파일 정보 출력
echo " ";
echo "<h3>파일 정보</h3>
<ul>
    <li>파일명: $name</li>
    <li>확장자: $ext</li>
    <li>파일형식: {$_FILES['myfile']['type']}</li>
    <li>파일크기: {$_FILES['myfile']['size']} 바이트</li>
</ul>";

