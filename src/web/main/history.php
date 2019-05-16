<!DOCTYPE html>
<?php
session_start();
$check = $_SESSION['is_logged'];
echo $id;
if($check != "Y"){
	echo "<script>location.replace('../index.html');</script>";
}


// db 연결
$mysqli = new mysqli('localhost', 'admin', 'Kookmin1!', 'db');
if($mysqli->connect_errno) {
  exit('Error Connecting db');
}
$mysqli->set_charset('utf8');

// select query

$result = $mysqli->query("SELECT * FROM `SEUNGAE`")->fetch_assoc();
?>

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
      width: 100%;
			overflow: hidden;
      min-height: 300px;
      min-width: 300px;
      overflow: scroll;
    }
    a:link {
    	color: red;
    	text-decoration: none;
    }
    a:visited {
      color: black;
      text-decoration: none;
    }
    a:hover {
      color: darkgrey;
      text-decoration:underline;
    }
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
		.event{
			position:absolute;
			bottom: 15px;
			left: 550px;
			z-index: 4;
		}
		#option{
			font-size: 50px;
			margin-left: 400px ;
			color: #FF007F;
			width: 80px;
		}
		#add{
			font-size: 50px;
			text-align: center;
			color: #FF007F;
			width: 80px;
		}
		#delete{
		  font-size: 50px;
		  text-align: center;
		  color: #FF007F;
		  width: 80px;
		}
		#modify{
			font-size: 50px;
			text-align: center;
			color: #FF007F;
			width: 80px;
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
		 	margin:0;
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

		<div class="event">
			<input type="button" id="option" onclick="deselect();" value="선택 해제" />
				<script>
					function deselect() {
		    		//체크 해제할 라디오버튼 불러오기
		     		var chek = document.getElementsByName("chek");

		     		for(var i=0; chek.length; i++)
		     		{
		        	//체크되어 있다면 chek[i].checked == true
		        	//true -> false로 변환 ==> 체크해제
		        	if(chek[i].checked)
		        	{
		          	alert("동영상을 해제했습니다.")
		          	chek[i].checked = false;
		        	}
		        	// else
		        	//     alert("해제할 동영상이 없습니다.")
		     		}
		      }
		    </script>

	      <input type="button" id="add" onclick="button1_click();" value="추가" />
		      <script>
			      function button1_click() {
			        alert("추가를 눌렀습니다..");
			      }
		      </script>

	      <input type="button" id="delete" onclick="button2_click();" value="삭제" />
		      <script>
			      function button2_click() {
			        alert("삭제를 눌렀습니다.");
			      }
		      </script>

	      <input type="button" id="modify" onclick="button3_click();" value="수정" />
		      <script>
			      function button3_click() {
			        alert("수정을 눌렀습니다.");
			      }
	      	</script>
    	</div>
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

			<script>
				function check(video){
					if(video.checked == true)
					alert(video.value + "의 동영상을 체크했습니다.")
				}
			</script>

			<div class="history">
				<div class="container">
					<form>
						<input type=radio name="chek" value="<?php $name = $_SESSION['name']; echo $name; ?>" width="230" height="230" onclick="check(this)"><br>
							<iframe width="220" height="220" src="http://52.78.219.61/recordVideo/승애.mp4" frameborder='0'></iframe>
								<li>"<?= $result['rDate'] ?>" </li>
								<li>"<?= $result['name'] ?> 방문 "</li>
					</form>
				</div>


				<div class="container">
					<form>
						<input type=radio name="chek" value="<?php $id = $_SESSION['id']; echo $id; ?>" width="230" height="230" onclick="check(this)"><br>
							<iframe width="220" height="220" src="http://52.78.219.61/recordVideo/수진.mp4" frameborder='0'></iframe>
								<li> 2019-03-29 </li>
								<li> 21:17 </li>
								<li> "이수진 방문" </li>
					</form>
				</div>


				<div class="container">
					<form>
						<input type=radio name="chek" value="큐트" width="230" height="230" onclick="check(this)"><br>
							<iframe width="220" height="220" src='http://52.78.219.61/<?php $id = $_SESSION['id']; echo $id; ?>/recordVideo/cute.mp4' allow="autoplay" frameborder='0'>$
								<li> 2019-03-29 </li>
								<li> 21:17 </li>
								<li> "큐트 방문" </li>
					</form>
				</div>


				<div class="container">
					<form>
						<input type=radio name="chek" value="<?php $id = $_SESSION['id']; echo $id; ?>" width="230" height="230" onclick="check(this)"><br>
            	<input type=radio name="chek" value="<?php $id = $_SESSION['id']; echo $id; ?>" width="230" height="230" onclick="check(this)"><br>
              	<iframe width="220" height="220" src='http://52.78.219.61/<?php $id = $_SESSION['id']; echo $id; ?>/recordVideo/혜안.mp4' allow="autoplay" frameborder='0'>$
                	<li> 2019-03-29 </li>
                	<li> 21:17 </li>
                	<li> "경혜안 방문" </li>
          </form>
        </div>


        <div class="container">
        	<form>
          	<input type=radio name="chek" value="은주" width="230" height="230" onclick="check(this)"><br>
            	<iframe width="220" height="220" src='http://52.78.219.61/recordVideo/은주.mp4' allow="autoplay" frameborder='0'></iframe>
              	<li> 2019-03-29 </li>
              	<li> 21:17 </li>
              	<li> "최은주 방문" </li>
        	</form>
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
