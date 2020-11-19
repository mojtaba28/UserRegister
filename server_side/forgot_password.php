<?php
include ("db_connect.php");
if(isset($_REQUEST["email"]) && (!empty($_REQUEST["email"]))){
	$email = $_REQUEST["email"];
	$sql = "SELECT * FROM users WHERE email = '$email' ";
	mysqli_query($cnn,"SET CHARACTER SET utf8");
		
		//executing query
    $result = mysqli_query($cnn,$sql);

    //fetching result
    $check = mysqli_fetch_array($result);
	if(isset($check)){
    
        $user_email= $check['email'];
        $pass = $check['password'];
		
		$to=$user_email;
		$subject = "Password";
        $txt = "Your password is : $pass.";
        $headers = "From: mojtaba.joshaghani99@gmail.com";
                
 
	if (mail($to,$subject,$txt,$headers)) {
		echo "Email successfully sent to $to...";
	} else {
    echo "Email sending failed...";
	}
	}else{
		echo"failed";
	}
		
		
}
?>