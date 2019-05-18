<?php
session_start();
if($_SESSION["is_logged"] != "Y")
echo("<script>location.replace('../login/re_login.php');</script>");
// db 연결
$mysqli = new mysqli('localhost', 'admin', 'Kookmin1!', 'db');
if($mysqli->connect_errno) {
  exit('Error Connecting db');
}
$mysqli->set_charset('utf8');

$name = $_POST['name'];
$belong = $_POST['belong'];
$rIdx = $_POST['rIdx'];

$result = $mysqli->query("UPDATE `SEUNGAE` SET `name` = '{$name}', `belong` = '{$belong}' WHERE `rIdx` = {$rIdx}");

echo " ";
echo "Update 완료";
?>
