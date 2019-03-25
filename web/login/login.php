<!DOCTYPE html>
<html>
<head>
	<title>Login Check</title>
	<meta content="text/html"/>
</head>
<body>
<?php
	include "./conn.php";

	$id = $_POST['id'];
	$pw = $_POST['pw'];

	$filter = ['id' => $id, 'pw' => $pw];
	$options = [
		'projection' => ['_id' => 0]
	];

	$query = new MongoDB\Driver\Query($filter, $options);
	$rows = $mongo->executeQuery("Login.users", $query);
	$login = current($rows->toArray());

	if(!empty($login)){
		if(($id == $login->id) && ($pw == $login->pw)){
			if($id == "admin"){
				echo "Welcome Admin!<br><br>";
			}
                        session_start();
			$_SESSION["is_logged"] = "Y";
			echo("<script>location.replace('../main/main.php');</script>");
		}
	}else{
		echo("<script>location.replace('./re_login.html');</script>");
	}
?>
</body>
</html>
