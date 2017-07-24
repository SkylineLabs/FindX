<?php
    

include_once './db_functions.php';
include_once './GCM.php';

     
if (isset($_GET["user_name"]) && isset($_GET["message"])){


$user_name = $_GET["user_name"];
$message=$_GET["message"];

$message = array("price" => $message);

$db = new DB_Functions();
$gcm = new GCM();

$all = $db->getAllUsers();

while($row = mysql_fetch_array($all))
{
if($row["user_name"] == $user_name)
{
	$giood1=$row["gcm_regid"];
					$iood1 = array($giood1);
$roo1=$gcm->send_notification($iood1,$message);	
$fav1 = $row["fav1"];
$fav2 = $row["fav2"];
$fav3 = $row["fav3"];
$fav4 = $row["fav4"];
      
	 $allq = $db->getAllUsers();
	while($f1=mysql_fetch_array($allq))
		{
			if($f1["user_name"]==$fav1)
				{
					$gid1=$f1["gcm_regid"];
					$id1 = array($gid1);
					
					
					print_r($id1);
					print_r($message);
					$r1=$gcm->send_notification($id1,$message);			
				}
		}

		/*while($f2=mysql_fetch_array($all))
		{
			if($f2["user_name"]==$fav2)
				{
					$gid2=$f2["gcm_regid"];	
					$id2=array($gid2);

					
					$r2=$gcm->send_notification($id2,$message);			
				}
		}


		while($f3=mysql_fetch_array($all))
		{
			if($f3["user_name"]==$fav3)
				{
					$gid3=$f3["gcm_regid"];
					$id3=array($gid3);
					$r3=$gcm->send_notification($id3,$message);			
				}
		}
   
	while($f4=mysql_fetch_array($all))
		{
			if($f4["user_name"]==$fav4)
				{
					$gid4=$f4["gcm_regid"];
					$id4=array($gid4);
					$r4=$gcm->send_notification($id4,$message);			
				}
		}
*/
 
}
}



}

?>