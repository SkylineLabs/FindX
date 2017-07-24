<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

if (isset($_POST['latitude']) && isset($_POST['longitude'])  && isset($_POST['gcm_regid']) && isset($_POST['contact']) && isset($_POST['email'])  && isset($_POST['category']) && isset($_POST['user_name']) && isset($_POST['password']) && isset($_POST['IMEI'])) {
    
    $latitude = $_POST['latitude'];
    $IMEI = $_POST['IMEI'];
    $category= $_POST['category'];
    $longitude = $_POST['longitude'];
    $user_name = $_POST['user_name'];
    $password = $_POST['password'];
    $sharing_on = $_POST['sharing_on'];
    $gcm = $_POST['gcm_regid'];
    $contact= $_POST['contact'];
    $email= $_POST['email'];
    $time = $_POST['time'];
   

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
   $result = mysql_query("UPDATE Geo SET time ='$time',  longitude = '$longitude',category= '$category',IMEI = $IMEI,email = $email, latitude = '$latitude', gcm_regid = '$gcm', time='$time' , history = '$history' , sharing_on = '$sharing_on', password='$password' WHERE user_name= '$user_name'");
     $result = mysql_fetch_array($result);
    if (($result)) {
     

         
     $response["success"] = 1;
      $response["message"] = "Product successfully created.";
      
      
    
    // echoing JSON response
    echo json_encode($response);
}
 else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>