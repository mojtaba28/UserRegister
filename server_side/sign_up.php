<?php include ("db_connect.php");?>
<?php
if(isset($_REQUEST['email']) and isset($_REQUEST['password']) and isset($_REQUEST['name'])){
	
	$email = $_REQUEST['email'];
    $password = $_REQUEST['password'];
	$name = $_REQUEST['name'];
	
	
	$sql = "SELECT * FROM users WHERE email = '$email' ";
	
	$result = mysqli_query($cnn,$sql);
 
	$check = mysqli_fetch_array($result);
	
	mysqli_query($cnn,"SET CHARACTER SET utf8");
	
	if(isset($check)){
		echo "already exist";
	}else{
		$sql2 = "INSERT INTO users (email , password , name) VALUES ('$email','$password','$name') ";
		
		$result2 = mysqli_query($cnn,$sql2);
 
		if($result2 != ""){
			echo "successful";
		}else{
			echo"failed";
		}
	}
	
}else{
	echo"please first set email,password and name";
}

?>