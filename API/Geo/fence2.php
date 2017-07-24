<?php
    
$response = array();

include_once './db_functions.php';
include_once './GCM.php';


if (isset($_GET["message"])&& isset($_GET["user_name"])){


$user_name = $_GET["user_name"];
$message= $_GET["message"];

$db2 = new DB_Functions();
$result = mysql_query("SELECT *FROM Geo WHERE user_name = '$user_name'");


$gcm2 = new GCM();


$all = $db2->getAllUsers();


$result = mysql_fetch_array($result);

$current_id2 = $result ["gcm_regid"];
$registatoin_ids2 = array($current_id2);
$message2 = array("message" => $message,"user_name"   => $user_name);
 

 include_once './db_config.php';
 
        // Set POST variables
        $url = 'https://android.googleapis.com/gcm/send';
 
        $fields = array(
            'registration_ids' => $registatoin_ids2,
            'data' => $message2,
        );
 
        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        // Open connection
        $ch = curl_init();
 
        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);
 
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
 
        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
 
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 
        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
 
        // Close connection
        curl_close($ch);
        echo $result;

}

?>