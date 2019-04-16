<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>History</title>
	<style type="text/css">
			html,body{
				font-family: Andale Mono;
				margin: 0px;
				padding: 0px;
				height: 100%;
				overflow: hidden;
				width: 100%;
				min-height: 300px;
				min-width: 300px;
				overflow: scroll;
			}
			a:link { color: red; text-decoration: none;}
 			a:visited { color: black; text-decoration: none;}
 			a:hover { color: darkgrey; text-decoration:underline;}
			li{
   				list-style:none;
   				padding-left:0px;
   			}
			 ol {
			    list-style-type:none;
			    margin:0;
			    padding:0;
			}
			.page li {
				margin-left: 10px;
			    float: left;
			}
			.header{
				display: fixed;
				top:0; left:0; right:0;
				width: 100%;
				height:60px;
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
				height: 100%;
				text-align: center;
				z-index: 1;
				display: table;
			}
			.left{
				width: 100%;
				height: 100%;
				text-align: center;
			}
			.menu{
				width: 100%;
				height: 50%;
				display: table;
				text-align: center;
				vertical-align: middle;
			}
			.btn{
				width: 100%;
				height: 100%;
				display: table-cell;
				text-align: center;
				vertical-align: middle;
			}
			.history{
				width: 90%;
				display: table-cell;
				vertical-align: top;
				font-size: 20px;
				text-align: center;
			}
			.container{
				float: left;
				margin: 5px;
				width: 300px;
				height: 50%;
			}
			.page{
				float: left;
				bottom: -60px; left:740px; right:0;
				margin:0px;
				width: 100px;
				height: 100px;
				position: fixed;
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

		</style>
</head>
<body>
	<div class="header">
		Smart Interphone
	</div>
	<div class="content">
		<div class="left">
			<div class="menu">
				<div class="btn" style="background-color: #E4F7BA;" onclick="location.href='./streaming.php'">
					<img src="streaming.png" width="100" height = "100"><br>Streaming
				</div>
			</div>
			<div class="menu">
				<div class="btn" style="background-color: #CEFBC9;" onclick="location.href='../login/logout.php'">
					<img src="logout.png" width="100" height="100"><br>Logout
				</div>
			</div>
		</div>
		<div class="history">
			<div class="container">
				<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>
			<div class="container">
					<iframe width="220" height="220" src='https://www.youtube.com/embed/4HG_CJzyX6A' allow="autoplay" frameborder='0'></iframe>
						<li> 2019-03-29 </li>
						<li> 21:17 </li>
						<li> "황승애 방문" </li>
			</div>

			</div>
			<div class="page">
				<ol>
					<li class=""><a href="./history.php" class="this"><span style="color:pink"><</span></a></li>
					<li class=""><a href="./history.php" class="this"><span style="color:pink">1</span></a></li>
					<li ><a href="./history2.php" class="this"><span style="color:pink">2</span></a></li>
					<li ><a href="./history2.php" class="this"><span style="color:pink">></span></a></li>

				</ol>
			</div>
		</div>
	</div>
	<div class="footer">
		2019 캡스톤 디자인 I - 19조(5정호)
	</div>
</body>
</html>
