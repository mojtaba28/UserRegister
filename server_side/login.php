<?php include ("db_connect.php");?>

<?php
	if(isset($_REQUEST['email']) and isset($_REQUEST['password'])){
		
		$email = $_REQUEST['email'];
		$password = $_REQUEST['password'];
		
		$sql = "SELECT * FROM users WHERE email='$email' AND password='$password'";

		mysqli_query($cnn,"SET CHARACTER SET utf8");
		
		//executing query
    $result = mysqli_query($cnn,$sql);

    //fetching result
    $check = mysqli_fetch_array($result);
    
    if(isset($check)){
    
        $record['email'] = $check['email'];
        $record['password'] = $check['password'];
       
        echo "successful";
        
    }else{
       $record['username'] = "";
		$record['email'] = "";
        echo "failed";
    }
    echo json_encode($record,true);
	
	}

?>