<?php
     
    /*
     * Following code will create a new product row
     * All product details are read from HTTP Post Request
     */
     
    // array for JSON response
    $response = array();
if (isset($_GET["user_name"]) && isset($_GET["latitude"]) && isset($_GET["longitude"])&&isset($_GET['time'])&&isset($_GET['status'])){

    $latitude = $_GET['latitude'];
    $longitude = $_GET['longitude'];
    $user_name = $_GET['user_name'];
    $status = $_GET['status'];
    $time = $_GET['time'];
     
     
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
     
    // connecting to db
    $db = new DB_CONNECT();

       $sql = "INSERT INTO $user_name (latitude,longitude,time,status)
VALUES ('$latitude','$longitude','$time','$status')";

mysql_query($sql);
     
 
    }

else{
  $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
    
    ?>