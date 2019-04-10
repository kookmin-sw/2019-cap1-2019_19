<!DOCTYPE html>
<html>
  <?php
    session_start();
    $check = $_SESSION['is_logged'];
    if($check != "Y"){
      echo("<script>location.replace('../index.html');</script>"); 
    }
  ?>
  <head>
    <meta charset = "UTF-8">
    <title>Main Page</title>
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
        width: 100%;
        height: 60px;
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
        height: 90%;
        text-align: center;
        z-index: 1;
      }
      .opt{
        position: relative;
        width: 50%;
        height:100%;
        text-align: center;
        font-size: 100px;
      }
      .footer{
        margin:0px;
        position: fixed;
        bottom: 0; left:0; right:0;
        background-color: #D4F4FA;
        color: #FF007F;
        height: 30px;
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
      <div class="opt" style="float: left; background-color: #E4F7BA;" onclick="location.href='./streaming.php'">
        <br><br><br>Streaming
      </div>
      <div class="opt" style="float: right; background-color: #CEFBC9;" onclick="location.href='./history.php'">
        <br><br><br>History
      </div>
    </div>
    <div class="footer">
      2019 캡스톤 디자인 I - 19조(5정호)
    </div>
  </body>
</html>
