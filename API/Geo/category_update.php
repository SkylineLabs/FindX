<?php




$response = array();
if ( isset($_GET["user_name"])) {
    
    $user_name = $_GET['user_name'];
    $category= $_GET["category"];
  
    
	
	require_once __DIR__ . '/db_connect.php';

    
	$db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("UPDATE Geo SET category='$category' WHERE user_name='$user_name'");

    
}
?>