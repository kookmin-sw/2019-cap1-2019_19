<!DOCTYPE html>
<?php
session_start();
$check = $_SESSION['is_logged'];
echo $ip;
if($check != "Y"){
echo "<script>location.replace('../index.html');</script>";
}
?>
<html>
<head>
    <meta charset="UTF-8">
    <title>Streaming</title>
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
        .streaming{
            width: 90%;
            display: table-cell;
            vertical-align: middle;
            font-size: 20px;
            text-align: center;
        }
        .video{
            border: 2px solid #FF007F;
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
                <div class="btn" style="background-color: #E4F7BA;" onclick="location.href='./history.php'">
                    <img src="history.png" width="100" height = "100"><br>History
                </div>
            </div>
            <div class="menu">
                <div class="btn" style="background-color: #CEFBC9;" onclick="location.href='../login/logout.php'">
                    <img src="logout.png" width="100" height="100"><br>Logout
                </div>
            </div>
        </div>
        <div class="streaming">
            <img src="http://<?php $ip = $_SESSION['ip']; echo $ip; ?>:8090/?action=stream" />
        </div>
    </div>
    <div class="footer">
        2019 캡스톤 디자인 I - 19조(5정호)
    </div>
</body>
</html>
