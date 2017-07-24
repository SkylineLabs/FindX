<?php
    

include_once './db_functions.php';
include_once './GCM.php';

     
if (isset($_GET["user_name"]) && isset($_GET["message"])){


$user_name = $_GET["user_name"];
$message=$_GET["message"];

$message = array("price" => $message);

$dbw = new DB_Functions();
$gcmw = new GCM();

$allw = $dbw->getAllUsers();

while($roww = mysql_fetch_array($allw))
{
if($roww["user_name"] == $user_name)
{
	$giood1=$roww["gcm_regid"];
					$iood1 = array($giood1);
$roo1=$gcmw->send_notification($iood1,$message);	
}
}



}

?>