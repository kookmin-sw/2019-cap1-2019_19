<!DOCTYPE html>
<?php
session_start();
$check = $_SESSION['is_logged'];
if($check == "Y"){
  echo "<script>location.replace('../main/main.php');</script>";
}
?>
<html>
	<head>
		<meta charset = "UTF-8">
		<title>Smart Interphone</title>
		<style type="text/css">
			html,body{
				margin: 0px;
				padding: 0px;
				height: 100%;
				width: 100%;
				min-height: 300px;
				min-width: 300px;
			}
			.header{
				display: fixed;
				top:0; left:0; right:0;
				width: 100%
				height:12%;
				background-color: #D4F4FA;
				font-size: 50px;
				text-align: center;
				color: #FF007F;
				z-index: 4;
			}
			.content{
				position: relative;
				top:-70; left:0; right:0;
				width: 100%;
				height: 85%;
				text-align: center;
				z-index: 1;
			}
			.login{
				position: absolute;
				top: 50%;
				left:50%;
				transform:translate(-50%,-50%);
				text-align: center;
				vertical-align: middle;
				width:auto; height:auto;
				z-index: 2;
			}
			.footer{
				margin:0px;
				position: fixed;
				bottom: 0; left:0; right:0;
				background-color: #D4F4FA;
				color: #FF007F;
				height: 20px;
				font-size: 20px;
				text-align: right;
				z-index: 3;
			}
			input[type="text"],input[type="password"]{
				height: 50px;
				width: 300px;
				font-size: 20px;
				border: solid 2px #FF007F;
				border-radius: 10px;
				box-shadow: none;
			}
			input[type="submit"]{
				border-radius: 10px;
				border: none;
				background-color: #FF007F;
				color: white;
				height: 50px;
				width: 200px;
			}
		</style>
	</head>
	<body>
		<div class="header">
			Smart Interphone
		</div>
		<div class="content">
			<div class = "login">
			<p style="color: red; font-size: 20px;" >
				아이디 또는 비밀번호를 잘못 입력하셨습니다.
			</p>
			<form action="login.php" method="POST">
			<p>
				<input name="id" placeholder="아이디"type="text">
			</p>
			<p>
				<input name="pw" placeholder="비밀번호" type="password"><br>
			</p>
			<p>
				<input type="submit" value="Login">
			</p>
			</form>
			</div>
		</div>
		<div class="footer">
			2019 캡스톤 디자인 I - 19조(5정호)
		</div>
	</body>
</html>
