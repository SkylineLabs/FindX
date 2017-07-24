<?php
 
// response json
$response= array();

if (isset($_POST["user_name"]) && isset($_POST["password"])) {
    $user_name = $_POST["user_name"];
    $password = $_POST["password"];
    
    include_once './db_functions.php';
    include_once './GCM.php';
 
    $db = new DB_Functions();
 
   $all = $db->getAllUsers();
 
   while($row = mysql_fetch_array($all))
	{
		if($row["user_name"] == $user_name)
		{
  			if($row["password"]==$password)
			{
				 $response["success"] = 1;	
				 echo json_encode($response);
			}
			else{
				 $response["success"] = 0;	
				 echo json_encode($response);
			
				}
 		}
	}
   
} else {
    $response["success"] = 0;	
				 echo json_encode($response);
}
?>