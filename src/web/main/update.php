<?php
session_start();
if($_SESSION["is_logged"] != "Y")
echo("<script>location.replace('../login/re_login.php');</script>");
// db 연결
$mysqli = new mysqli('localhost', 'monitor', 'Kookmin1!', 'db');
if($mysqli->connect_errno) {
  exit('Error Connecting db');
}
$mysqli->set_charset('utf8');

$name = $_POST['name'];
$belong = $_POST['belong'];
$rIdx = $_POST['rIdx'];

$result = $mysqli->query("UPDATE `History` SET `name` = '{$name}', `belong` = '{$belong}' WHERE `rIdx` = {$rIdx}");

?>

<!DOCTYPE html>
<html>
  <style type="text/css">
    body {
      margin:auto;
    }

    #content {
      position: absolute;
      top: 65%;
      left: 50%;
      height: 300px;
      width: 300px;
      margin: -150px 0 0 -150px;
      color: #FF007F;
      text-align: center;
    }
  </style>
  <body>
    <div id="content">
      <h2> Updatet 완료 </h2>

      <input type="button" value="닫기" onclick="window.close();">
    </div>
  </body>
  <script>
  </script>

</html>
