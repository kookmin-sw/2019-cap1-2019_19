<?php
$con=mysqli_connect("localhost","app","Kookmin1!","db");

if (mysqli_connect_errno($con))
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$ID = $_POST["id"];

$result = mysqli_query($con,"SELECT ip FROM Login where id='$ID'");
$row = mysqli_fetch_array($result);
$data = $row[0];
echo json_encode($row);

mysqli_close($con);
?>
