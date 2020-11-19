<?php 

   define('DB_HOST', "localhost");
   define('DB_NAME', "register");
   define('DB_USER', "root");
   define('DB_PASS', "");
	
	$cnn = mysqli_connect(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to oconnect');

?>