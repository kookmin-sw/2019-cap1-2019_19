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

$rIdx = $_GET['rIdx'];

$result = $mysqli->query("DELETE FROM `SEUNGAE` WHERE rIdx = {$rIdx}");

$mysqli->query($result);


echo "Delete 완료";
?>
