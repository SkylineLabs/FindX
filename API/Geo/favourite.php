<?php




$response = array();
if ( isset($_GET["user_name"])) {
    
    $user_name = $_GET['user_name'];
    $fav_1= $_GET["fav_1"];
    $fav_2= $_GET["fav_2"];
    $fav_3= $_GET["fav_3"];
    $fav_4= $_GET["fav_4"];
    
	
	require_once __DIR__ . '/db_connect.php';

    
	$db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("UPDATE Geo SET fav1='$fav_1',fav2='$fav_2',fav3='$fav_3' WHERE user_name='$user_name'");

    
}
?>