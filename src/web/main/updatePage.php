<?php
session_start();
if($_SESSION['is_logged'] != 'Y')
  echo("<script>location.replace('../login/re_login.php');</script>");
  ?>

  <!DOCTYPE html>
  <html>
      <style type="text/css">
        body {
          margin:auto;
        }

        #content {
          position: absolute;
          top: 55%;
          left: 50%;
          height: 300px;
          width: 300px;
          margin: -150px 0 0 -150px;
          color: #FF007F;
          text-align: center;
        }
      </style>

    <body>

      <div id="content">
        <h1> Update DB </h1>

        <form action="update.php" method='post'>
          name : <input type="text" placeholder="이름을 입력하세요" name="name"><br/><br/>
          belong : <input type="text" placeholder="그룹을 입력하세요" name="belong"><br/><br/>
          <input type="hidden" name="rIdx" value="<?=$_GET['rIdx']?>">
          <input type="submit" value="수정">
        </form>
      </div>
    </body>
  </html>
