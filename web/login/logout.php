<?php
  session_start();
  session_destroy();
  echo "<script>location.replace('../index.html');</script>";
?>
