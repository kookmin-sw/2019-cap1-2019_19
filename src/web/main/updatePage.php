<?php
session_start();
if($_SESSION['is_logged'] != 'Y')
  echo("<script>location.replace('../re_login.php');</script>");
  ?>
<!DOCTYPE html>
<html>
  <body>
    <form action="update.php" method='post'>
      name : <input type="text" name="name">
      belong : <input type="text" name="belong">
      <input type="hidden" name="rIdx" value="<?=$_GET['rIdx']?>">
      <input type="submit" value="수정">
    </form>
  </body>
</html>
