<?php
    

include_once './db_functions.php';
include_once './GCM.php';


$db = new DB_Functions();
$gcm = new GCM();

$title =  $_GET["title"];
$messageC=  $_GET["message"];
$user_name=  $_GET["user_name"];
$alert = "alert";

$all = $db->getAllUsers();



while($row = mysql_fetch_array($all))
{
$current_id = $row["gcm_regid"];
$registatoin_ids = array($current_id);

 $message = array("title" => $title,"messageC" =>$messageC, "user_name" =>  $user_name,"type"   => $alert);

 $result = $gcm->send_notification($registatoin_ids, $message);

}


?>