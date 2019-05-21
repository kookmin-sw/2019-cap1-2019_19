<!DOCTYPE html>
<?php
session_start();
$check = $_SESSION['is_logged'];
echo $id;
if($check != "Y"){
	echo "<script>location.replace('../index.html');</script>";
}


// db 연결
$mysqli = new mysqli('localhost', 'monitor', 'Kookmin1!', 'db');
if($mysqli->connect_errno) {
  exit('Error Connecting db');
}
$mysqli->set_charset('utf8');

// select query
$result = $mysqli->query("SELECT * FROM `History`");
?>

<html>
<head>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
</head>
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

		input {
			display: fixed;

		}
		.page li {
			margin-left: 10px;
			float: left;
		}
		.header{
			display: fixed;
      position: relative;
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
			width: 37%;
			position:relative;
			bottom: 65px;
			left: 880px;
			z-index: 5;
		}
		#option{
			font-size: 50px;
			margin-left: 100px ;
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
		 	bottom: 0px; left:750px; right:0;
		 	margin:0;
		 	width: 100%;
		 	height: auto;
		 	position: absolute;
			display: table-cell;
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
	<script>
	var index;
	</script>
</head>
<body>
	<div class="header">

		Smart Interphone
    <div class="event">
      <input type="button" id="option" onclick="deselect();" value="선택 해제" />
      <input type="button" id="add" onclick="add();" value="등록" />
      <input type="button" id="delete" onclick="del();" value="삭제" />
      <input type="button" id="modify" onclick="update();" value="수정" />
    </div>

				<script>
					function deselect() {
		    		//체크 해제할 라디오버튼 불러오기
		     		var chek = document.getElementsByName("chek");
						var find = false;
						for(var i=0;  i < chek.length; i++)
		     		{
		        	//체크되어 있다면 chek[i].checked == true
		        	//true -> false로 변환 ==> 체크해제
		        	if(chek[i].checked)
		        	{
								find = true;
		          	alert("동영상을 해제했습니다.");
		          	chek[i].checked = false;
								return;
		        	}
		     		}
						if(!find) {
							alert("해제할 동영상이 없습니다.");
							return;
						}
					}
		    </script>


		    <script>
			    function add() {
						var chek = document.getElementsByName("chek");
						var find = false;
						for(var i=0;  i < chek.length; i++)
						{
							if(chek[i].checked)
							{
								find = true;
								location.href="insertPage.php?rIdx=" + index;
								chek[i].checked = false;

								return;
							}
						}
						if(!find) {
							alert("등록할 동영상이 없습니다.");
							return;
						}
			    }
		    </script>


			  <script>
				  function del() {
						var chek = document.getElementsByName("chek");
						var find = false;
						for(var i=0;  i < chek.length; i++)
		     		{
		        	if(chek[i].checked)
		        	{
								find = true;

								if(!confirm("정말 삭제하시겠습니까?"))
									return false;
								$.get( "delete.php?rIdx=" + index, function( data ) {
									$( ".result" ).html( data );
									location.reload();
								});
		          	chek[i].checked = false;
								return;
		        	}
		     		}
						if(!find) {
							alert("삭제할 동영상이 없습니다.");
							return;
						}


			}
			  </script>


				<script>
			     function update() {
						 var chek = document.getElementsByName("chek");
						 var find = false;
						 for(var i=0;  i < chek.length; i++)
						 {
							 if(chek[i].checked)
							 {
								 find = true;
								 location.href="updatePage.php?rIdx=" + index;

								 return;
							 }
						 }
						 if(!find) {
							 alert("수정할 동영상이 없습니다.");
							 return;
						 }
			     }
	      </script>

				<script>
					function check(video){
						if(video.checked == true)
							index = video.value;
							alert(index + "의 동영상을 체크했습니다.")
					}
				</script>

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
	    	<form>
        	<?php
						if(isset($_GET['page']))
							$page = $_GET['page'];
						else
							$page = 1;

						// page 당 8개
						$per_page = 8;
						$offset = ($page-1) * $per_page;


						$totalCount = $mysqli->query("SELECT * FROM `History`");
						$totalCount = $totalCount->num_rows;
						$totalPages = ceil($totalCount / $per_page);

						$result = $mysqli->query("SELECT * FROM `History` LIMIT $offset, $per_page");

	        	$i = $result->num_rows;
	        	while ($res = $result->fetch_assoc()) {
							$date = substr($res['rDate'], 0, 16);
          ?>
					<div class="container">

	         	<input type=radio name="chek" value="<?= $res['rIdx']?>" width="300" height="300" onclick="check(this)"><br>
          	<video width="220" height="220" controls="controls">
            	<source src="<?= $res['video']?>" type="video/mp4" />
          	</video>
	             <li>"<?= $date ?>" </li>
	             <li>"<?= $res['name'] ?> 방문 "</li>

			</div>
			<?php
			$i—;
			}
		?>
	     </form>
			</div>
		</div>

	</div>

	<div class="footer">

		<div class="page">
			<ul>
				<?php
					$i = $_GET['page'];
					$i =(int)$i;
					$max = $i + 4;
					$min = $i - 3;
					if($min <= 0) {
						$max += 1 - $min;
						$min = 1;
					}
					if($max > $totalPages) {
						$min -= $max - $totalPages;
						$max = $totalPages ;
						if($min <= 0) {
							$min = 1;
						}
					}

					for(; $min<=$max; $min=$min+1) {
						if($min == $_GET['page']) {
							echo "<li><a style=\"color:#FF007F;\"href=\"?page={$min}\">{$min}</a></li>";
							continue;
						}
						echo "<li><a href=\"?page={$min}\">{$min}</a></li>";
					}
				?>
			</ul>

		</div>
		2019 캡스톤 디자인 I - 19조(5정호)
	</div>
</body>
</html>
